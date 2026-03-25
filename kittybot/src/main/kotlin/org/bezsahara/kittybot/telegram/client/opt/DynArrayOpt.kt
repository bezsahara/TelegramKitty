package org.bezsahara.kittybot.telegram.client.opt

import java.util.concurrent.atomic.AtomicIntegerArray


// ===== Bit packing: top bit = lock, lower 31 bits = size =====
private const val LOCK_BIT  = 1 shl 31        // 0x8000_0000
private const val SIZE_MASK = LOCK_BIT.inv()  // 0x7FFF_FFFF

// ===== Policy knobs (tune here) =====
private const val MIN_CAP          = 64                         // small floor
private const val MAX_CAP          = 16 * 1024 * 1024           // 16 MiB ceiling (set what makes sense for you)
private const val HEADROOM_SHIFT   = 3                          // +12.5% headroom
private const val HEADROOM_CONST   = 32                         // +32 bytes
private const val SPIKE_CLAMP_NUM  = 3                          // clamp spikes to <= 1.5x baseline (3/2)
private const val SPIKE_CLAMP_DEN  = 2
private const val GROW_EXTRA       = 64                         // extra when growing to avoid immediate re-hit
private const val SHRINK_UTIL_QTR  = 4                          // shrink only if <= 25% utilized
private const val SHRINK_SLACK_MIN = 1024                       // and > 1 KiB slack

// EMA config (fixed-point Q4: value*16). Alpha = 1/8 (smoothing).
private const val Q_SHIFT          = 4                          // Q4 fixed point
private const val EMA_ALPHA_SHIFT  = 3                          // alpha = 1/8

// Peak decay: subtract peak/16 per update.
private const val PEAK_DECAY_SHIFT = 4                          // faster -> larger number

// ===== Helpers: encode/decode lock =====
fun getActualNumber(packed: Int): Int = packed and SIZE_MASK
fun sizeLock(size: Int): Int = size or LOCK_BIT
fun sizeUnlock(packed: Int): Int = packed and SIZE_MASK
fun isLockedPacked(packed: Int): Boolean = (packed and LOCK_BIT) != 0

// ===== Next power of two (clamped) =====
private fun roundUpPow2(x: Int): Int {
    if (x <= 1) return 1
    var v = x - 1
    v = v or (v ushr 1)
    v = v or (v ushr 2)
    v = v or (v ushr 4)
    v = v or (v ushr 8)
    v = v or (v ushr 16)
    val r = v + 1
    return if (r < 0) Int.MAX_VALUE and SIZE_MASK else r // avoid 1<<31 negative
}

// ===== The container with robust tuning =====
class DynArrayOpt(size: Int) {
    // Packed capacities (size + lock flag) with volatile semantics.
    private val packedSizes = AtomicIntegerArray(size)
    // EMA of "needed" (Q4 fixed point).
    private val emaQ4       = AtomicIntegerArray(size)
    // Decayed peak of "needed" (plain int).
    private val peak        = AtomicIntegerArray(size)

    /** Get current capacity hint (0 if unset). */
    fun getSize(id: Int): Int = getActualNumber(packedSizes.get(id))
    fun isLocked(id: Int): Boolean = isLockedPacked(packedSizes.get(id))

    fun lock(id: Int, newSize: Int) {
        packedSizes.set(id, sizeLock(newSize))
    }

    fun unlock(id: Int) {
        val cur = packedSizes.get(id)
        packedSizes.set(id, sizeUnlock(cur))
    }

    /**
     * Teach the tuner from this call:
     * - guessUsed = buffer you actually used this time (e.g., buf.size)
     * - actualUsed = exact bytes required/written
     *
     * Stores a *new* capacity hint using a spike-resistant policy.
     * No-ops if locked.
     */
    fun updateValue(id: Int, guessUsed: Int, actualUsed: Int) {
        val curPacked = packedSizes.get(id)
        if (isLockedPacked(curPacked)) return

        // 1) Compute "needed" with headroom and clamp
        var needed = actualUsed + (actualUsed ushr HEADROOM_SHIFT) + HEADROOM_CONST
        if (needed < MIN_CAP) needed = MIN_CAP
        if (needed > MAX_CAP) needed = MAX_CAP

        // 2) Update EMA (Q4) and decayed peak (volatile reads/writes, last-writer-wins)
        val emaOld = emaQ4.get(id)
        val targetQ4 = needed shl Q_SHIFT
        val emaNew = emaOld + ((targetQ4 - emaOld) shr EMA_ALPHA_SHIFT) // ema += alpha*(x-ema)
        emaQ4.set(id, emaNew)

        val peakOld = peak.get(id)
        val peakDecayed = peakOld - (peakOld ushr PEAK_DECAY_SHIFT)
        val peakNew = if (needed > peakDecayed) needed else peakDecayed
        peak.set(id, peakNew)

        // 3) Baseline from EMA, spike-aware cap from decayed peak
        val baseline = emaNew ushr Q_SHIFT // typical recent size
        val clampMax = minOf(peakNew, (baseline * SPIKE_CLAMP_NUM) / SPIKE_CLAMP_DEN)

        // Proposed next capacity before hysteresis
        var proposed = maxOf(baseline, clampMax)
        if (proposed < MIN_CAP) proposed = MIN_CAP
        if (proposed > MAX_CAP) proposed = MAX_CAP

        // 4) Hysteresis: grow generously; shrink only when truly underutilized
        val next =
            if (proposed > guessUsed) {
                // don’t inch upward: ensure at least ~1.5x step to amortize
                val grow = guessUsed + (guessUsed ushr 1) + GROW_EXTRA
                roundUpPow2(maxOf(proposed, grow, MIN_CAP).coerceAtMost(MAX_CAP))
            } else {
                // shrink only if <=25% utilized and with decent slack
                val tooBig = guessUsed >= proposed * SHRINK_UTIL_QTR
                val slack  = guessUsed - proposed
                if (tooBig && slack > SHRINK_SLACK_MIN) {
                    roundUpPow2(proposed.coerceAtLeast(MIN_CAP).coerceAtMost(MAX_CAP))
                } else {
                    guessUsed // keep as-is to avoid flapping
                }
            }

        packedSizes.set(id, next) // publish
    }
}