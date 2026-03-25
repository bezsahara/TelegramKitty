package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import java.sql.SQLXML
import java.util.Arrays
import kotlin.collections.set
import kotlin.math.max
import kotlin.random.Random
import kotlin.time.measureTime


// Checks if handler identity call returns different identifies on separate occasions.
// If it does it might mean that identity was implemented as a getter - which is incorrect.
fun Handler.testAndGetIdentity(): HandlerIdentity? {
    val id1 = identity
    if (identity != id1) {
        error("Your handler returns different identities! Check if you implemented handler correctly. Handler: $this")
    }
    return id1
}

/**
 * Fast int -> int hash map (no boxing).
 *
 * Design:
 * - Open addressing + linear probing (cache-friendly).
 * - Power-of-two capacity; index = mix(key) & mask.
 * - Empty slot sentinel is key == 0; so key 0 is stored in a dedicated field.
 * - Deletion uses backward-shift deletion (no tombstones).
 *
 * Semantics:
 * - get(k) returns missingValue if absent (so choose a missingValue you won't store if you need disambiguation).
 * - put/remove return previous value or missingValue if absent.
 */
class IntIntHashMap(
    expectedSize: Int = 16,
    private val missingValue: Int = 0,
    loadFactor: Double = 0.6,
) {
    init {
        require(loadFactor > 0.0 && loadFactor < 1.0) { "loadFactor must be in (0,1)" }
        require(expectedSize >= 0) { "expectedSize must be >= 0" }
    }

    private val lf: Double = loadFactor

    private var keys: IntArray
    private var values: IntArray
    private var mask: Int = 0
    private var maxFill: Int = 0

    // number of non-zero keys stored in keys[]/values[]
    private var used: Int = 0

    // special handling for key == 0 (because 0 is the empty-slot sentinel)
    private var hasZeroKey: Boolean = false
    private var zeroValue: Int = missingValue

    val size: Int
        get() = used + (if (hasZeroKey) 1 else 0)

    init {
        val cap = capacityFor(expectedSize, lf)
        keys = IntArray(cap)
        values = IntArray(cap)
        mask = cap - 1
        maxFill = calcMaxFill(cap, lf)
    }

    fun isEmpty(): Boolean = size == 0

    fun containsKey(key: Int): Boolean {
        if (key == 0) return hasZeroKey
        return findIndex(key) >= 0
    }

    operator fun get(key: Int): Int {
        if (key == 0) return if (hasZeroKey) zeroValue else missingValue
        val idx = findIndex(key)
        return if (idx >= 0) values[idx] else missingValue
    }

    fun getOrDefault(key: Int, defaultValue: Int): Int {
        if (key == 0) return if (hasZeroKey) zeroValue else defaultValue
        val idx = findIndex(key)
        return if (idx >= 0) values[idx] else defaultValue
    }

    operator fun set(key: Int, value: Int): Int {
        if (key == 0) {
            val old = if (hasZeroKey) zeroValue else missingValue
            if (!hasZeroKey) hasZeroKey = true
            zeroValue = value
            return old
        }

        // Grow BEFORE inserting.
        if (used + 1 > maxFill) rehash(keys.size shl 1)

        var pos = mix32(key) and mask
        while (true) {
            val k = keys[pos]
            if (k == 0) {
                keys[pos] = key
                values[pos] = value
                used++
                return missingValue
            }
            if (k == key) {
                val old = values[pos]
                values[pos] = value
                return old
            }
            pos = (pos + 1) and mask
        }
    }

    fun putIfAbsent(key: Int, value: Int): Int {
        if (key == 0) {
            if (hasZeroKey) return zeroValue
            hasZeroKey = true
            zeroValue = value
            return missingValue
        }

        if (used + 1 > maxFill) rehash(keys.size shl 1)

        var pos = mix32(key) and mask
        while (true) {
            val k = keys[pos]
            if (k == 0) {
                keys[pos] = key
                values[pos] = value
                used++
                return missingValue
            }
            if (k == key) {
                return values[pos]
            }
            pos = (pos + 1) and mask
        }
    }

    fun remove(key: Int): Int {
        if (key == 0) {
            if (!hasZeroKey) return missingValue
            hasZeroKey = false
            val old = zeroValue
            zeroValue = missingValue
            return old
        }

        val idx = findIndex(key)
        if (idx < 0) return missingValue

        val old = values[idx]
        shiftDelete(idx)
        used--

        // Optional downsize (usually good for long-lived maps with churn).
        // Keep it conservative to avoid resize thrash.
        val cap = keys.size
        if (cap > 16 && used < (maxFill ushr 2)) {
            rehash(cap ushr 1)
        }

        return old
    }

    fun clear() {
        Arrays.fill(keys, 0)
        // values[] doesn't need clearing for correctness since keys[] gates visibility.
        used = 0
        hasZeroKey = false
        zeroValue = missingValue
    }

    // --- internals ---

    private fun findIndex(key: Int): Int {
        var pos = mix32(key) and mask
        while (true) {
            val k = keys[pos]
            if (k == 0) return -1
            if (k == key) return pos
            pos = (pos + 1) and mask
        }
    }

    /**
     * Backward-shift deletion (no tombstones).
     *
     * Intuition: keep sliding later entries backward into the hole
     * whenever that hole lies inside their probe sequence.
     */
    private fun shiftDelete(deletePos: Int) {
        var hole = deletePos
        var pos = (hole + 1) and mask

        while (true) {
            val k = keys[pos]
            if (k == 0) {
                keys[hole] = 0
                return
            }

            val home = mix32(k) and mask

            // Distances on a ring:
            val distPos = (pos - home) and mask
            val distHole = (hole - home) and mask

            // If the hole is closer to 'home' than current position, move entry backward.
            if (distHole < distPos) {
                keys[hole] = k
                values[hole] = values[pos]
                hole = pos
            }

            pos = (pos + 1) and mask
        }
    }

    private fun rehash(newCapacityRaw: Int) {
        val newCap = max(16, nextPowerOfTwo(newCapacityRaw))
        val oldKeys = keys
        val oldVals = values

        keys = IntArray(newCap)
        values = IntArray(newCap)
        mask = newCap - 1
        maxFill = calcMaxFill(newCap, lf)

        val oldHasZero = hasZeroKey
        val oldZeroVal = zeroValue

        used = 0
        hasZeroKey = false
        zeroValue = missingValue

        // reinsert non-zero keys
        var i = 0
        while (i < oldKeys.size) {
            val k = oldKeys[i]
            if (k != 0) {
                var pos = mix32(k) and mask
                while (keys[pos] != 0) {
                    pos = (pos + 1) and mask
                }
                keys[pos] = k
                values[pos] = oldVals[i]
                used++
            }
            i++
        }

        // restore zero key
        if (oldHasZero) {
            hasZeroKey = true
            zeroValue = oldZeroVal
        }
    }
}

internal fun calcMaxFill(cap: Int, lf: Double): Int {
    // keep at least one empty slot
    val mf = (cap * lf).toInt()
    return minOf(mf, cap - 1)
}

internal fun capacityFor(expectedSize: Int, lf: Double): Int {
    // expected non-zero keys are at most expectedSize
    val needed = (expectedSize / lf).toInt() + 1
    return max(16, nextPowerOfTwo(needed))
}

internal fun nextPowerOfTwo(x0: Int): Int {
    var x = x0
    var v = x - 1
    v = v or (v ushr 1)
    v = v or (v ushr 2)
    v = v or (v ushr 4)
    v = v or (v ushr 8)
    v = v or (v ushr 16)
    x = v + 1
    return if (x <= 0) 1 shl 30 else x
}

/**
 * MurmurHash3 32-bit finalizer (good avalanche for ints).
 * Linear probing is sensitive to poor hash distributions, so mixing matters.
 */
internal fun mix32(x0: Int): Int {
    var x = x0
    x = x xor (x ushr 16)
    x *= 0x85ebca6b.toInt()
    x = x xor (x ushr 13)
    x *= 0xc2b2ae35.toInt()
    x = x xor (x ushr 16)
    return x
}

fun main() {
    println()
}