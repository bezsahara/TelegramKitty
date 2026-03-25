package org.bezsahara.kittybot.telegram.client.opt

import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max
import kotlin.math.min

/**
 * Per-endpoint adaptive buffer size predictor.
 *
 * One instance per API method (e.g. sendMessage, editMessageText, etc.).
 *
 * Stores all state in a single AtomicLong:
 *   [ mode(2 bits) | largeCap(31 bits) | smallCap(31 bits) ]
 *
 * - smallCap: "normal" size this method usually needs
 * - largeCap: "escalated" size when small isn't enough
 * - mode:
 *      0,1 -> prefer smallCap
 *      2,3 -> prefer largeCap
 *
 * Thread-safe, lock-free (CAS loop). Hints do not poison learning.
 */
class BufferSizePredictor(
    /**
     * Minimum allowed buffer size (bytes) for this endpoint.
     * All predictions are clamped to >= minCap.
     * Choose something like 256 or 512.
     */
    private val minCap: Int,

    /**
     * Maximum allowed buffer size (bytes) this predictor will "learn" up to.
     * - If actualLen > maxCap, that call is treated as an outlier: handled but NOT learned from.
     * - Prevents a few extreme requests from blowing up steady-state buffer sizes.
     */
    private val maxCap: Int,

    /**
     * Initial guess for "small" typical payload size (bytes).
     * - Will be rounded up to next power-of-two.
     * - Clamped into [minCap, maxCap].
     */
    initialSmall: Int,

    /**
     * Initial guess for "large" payload size (bytes).
     * - Will be rounded up to next power-of-two.
     * - Forced to be >= 2 * smallCap (if you give something smaller it will be corrected).
     * - Clamped into [smallCap, maxCap].
     */
    initialLarge: Int
) {

    // Packed state:
    // [ mode(2) | large(31) | small(31) ]
    private val state: AtomicLong

    init {
        require(minCap > 0) { "minCap must be > 0" }
        require(maxCap >= minCap) { "maxCap must be >= minCap" }

        val small = toCap(initialSmall, minCap, maxCap)

        val largeSeed = max(initialLarge, small shl 1)
        val large = toCap(largeSeed, small, maxCap).coerceAtLeast(small)

        state = AtomicLong(pack(mode = 0, small = small, large = large))
    }

    /**
     * Decide buffer capacity for this call.
     *
     * Result is in [minCap, maxCap].
     */
    fun decideCapacity(): Int {
        val s = state.get()
        val small = smallOf(s)
        val large = largeOf(s)
        val mode = modeOf(s)

        val predicted = if (mode <= 1) small else large

//        val wanted = maxOf(predicted, minCap)
        return min(max(predicted, minCap), maxCap)
//        return if (wanted > maxCap) maxCap else wanted
    }

    /**
     * Record actual usage for this call so future predictions can adapt.
     *
     * @param actualLen       how many bytes were actually written
     * @param initialCapacity the capacity returned by decideCapacity() that you started with
     *
     * Rules:
     * - If actualLen <= 0: ignored.
     * - If actualLen > maxCap: treated as outlier, ignored for learning.
     * - Otherwise:
     *      - If it overflowed initialCapacity -> we bias toward larger sizes.
     *      - If it comfortably fits smallCap -> we bias toward smaller sizes, may slowly shrink largeCap.
     *      - If it's between smallCap and largeCap -> bias toward largeCap, maybe increase largeCap.
     */
    fun record(actualLen: Int, initialCapacity: Int) {
//        if (actualLen <= 0) return

        // Don't learn from pathological giants; they shouldn't distort steady-state.
        if (actualLen > maxCap) return

        val need = toCap(actualLen, minCap, maxCap)
        val overflow = actualLen > initialCapacity

        while (true) {
            val s = state.get()
            var small = smallOf(s)
            var large = largeOf(s)
            var mode = modeOf(s) // 0..3

            if (overflow) {
                // Our chosen capacity (predicted + hint) was too small.
                // -> push predictor toward "large", and grow largeCap if needed.
                if (mode < 3) {
                    mode++       // saturating up
                }
                if (need > large) {
                    large = need // within [minCap, maxCap] by toCap()
                }
            } else {
                // No overflow: actualLen <= initialCapacity.
                if (actualLen <= small) {
                    // Strong evidence smallCap is fine.
                    if (mode > 0) {
                        mode--   // drift toward preferring small
                    }

                    // Optionally shrink large if it's absurdly larger than small.
                    if (large > (small shl 2)) {
                        // Keep large at least ~2x small, but let it decay.
                        val shrunk = max(small shl 1, large ushr 1)
                        large = min(shrunk, maxCap)
                    }
                } else {
                    // actualLen is > small but still <= initialCapacity:
                    // evidence that larger sizes are relevant.
                    if (mode < 3) {
                        mode++   // drift toward preferring large
                    }
                    if (need > large) {
                        large = need
                    }
                }
            }

            // Restore invariants.
            if (small < minCap) small = minCap
            if (large < small) large = small
            if (large > maxCap) large = maxCap

            val ns = pack(mode, small, large)
            if (ns == s || state.compareAndSet(s, ns)) {
                return // success or no-op if nothing changed
            }
            // else: lost the race, retry with fresh state
        }
    }

    // -------- internal helpers --------

    // Clamp x into [minCap, maxCap] and round up to next power of two.
    private fun toCap(x: Int, minCap: Int, maxCap: Int): Int {
        val clamped = when {
            x < minCap -> minCap
            x > maxCap -> maxCap
            else -> x
        }
        val pow2 = ceilingPow2(clamped)
        return pow2.coerceIn(minCap, maxCap)
    }

    private fun ceilingPow2(x: Int): Int {
        var v = x
        if (v <= 1) return 1
        v--
        v = v or (v ushr 1)
        v = v or (v ushr 2)
        v = v or (v ushr 4)
        v = v or (v ushr 8)
        v = v or (v ushr 16)
        v++
        // If overflow wraps negative, clamp.
        return if (v <= 0) Int.MAX_VALUE else v
    }

    private fun pack(mode: Int, small: Int, large: Int): Long {
        val m = (mode and 0b11).toLong()
        val s = (small.toLong() and MASK31)
        val l = (large.toLong() and MASK31) shl 31
        return (m shl 62) or l or s
    }

    private fun smallOf(state: Long): Int =
        (state and MASK31).toInt()

    private fun largeOf(state: Long): Int =
        ((state ushr 31) and MASK31).toInt()

    private fun modeOf(state: Long): Int =
        ((state ushr 62) and 0b11).toInt()

    companion object {
        private const val MASK31: Long = (1L shl 31) - 1L
    }
}
