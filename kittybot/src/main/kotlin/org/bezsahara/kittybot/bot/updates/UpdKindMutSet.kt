package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.other.KotlinHelpers
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update._telegramUpdateKinds

class UpdKindMutSet internal constructor(
    private var bits: Int
) : AbstractMutableSet<UpdKind>() {
    constructor(el: Array<UpdKind>) : this(KotlinHelpers.updKindArrayToInt(el))

    constructor(el: Collection<UpdKind>) : this(KotlinHelpers.updKindArrayToInt(el))

    constructor() : this(0)

    override val size: Int get() = bits.countOneBits()

    override fun add(element: UpdKind): Boolean {
        val bit = 1 shl element.ordinal
        val bits = bits
        if ((bits and bit) != 0) {
            return false
        } else {
            this.bits = bits or bit
            return true
        }
    }

    override fun contains(element: UpdKind): Boolean {
        return (bits and (1 shl element.ordinal)) != 0
    }

    override fun remove(element: UpdKind): Boolean {
        val ordinal = element.ordinal
        val bits = bits
        val present = (bits and (1 shl ordinal)) != 0
        this.bits = bits and (1 shl ordinal).inv()
        return present
    }

    internal fun insert(ordinal: Int) {
        bits = bits or (1 shl ordinal)
    }

    override fun iterator(): MutableIterator<UpdKind> {
        return Itr()
    }

    inner class Itr : MutableIterator<UpdKind> {
        private var remaining = bits
        private var lastReturnedBit = 0

        override fun hasNext(): Boolean = remaining != 0

        override fun next(): UpdKind {
            if (remaining == 0) {
                throw NoSuchElementException()
            }

            val index = remaining.countTrailingZeroBits()
            val bit = 1 shl index

            // Remove this bit from the iterator's remaining work.
            remaining = remaining and (remaining - 1)

            lastReturnedBit = bit
            return _telegramUpdateKinds[index]
        }

        override fun remove() {
            if (lastReturnedBit == 0) {
                throw IllegalStateException(
                    "next() must be called before remove(), and remove() may only be called once per element"
                )
            }

            bits = bits and lastReturnedBit.inv()
            lastReturnedBit = 0
        }
    }

    override fun isEmpty(): Boolean {
        return bits == 0
    }

    override fun clear() {
        bits = 0
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is UpdKindSet -> bits == other.accessBits
            is UpdKindMutSet -> bits == other.bits
            is Set<*> -> other.size == size && other.containsAll(this)
            else -> false
        }
    }

    override fun hashCode(): Int {
        return bits.sumSetBitIndexes()
    }

    override fun toArray(): Array<out Any> {
        return toArray(arrayOfNulls<Any?>(size))
    }

    override fun <T : Any?> toArray(a: Array<out T?>): Array<out T> {
        val size1 = size
        val size2 = a.size
        val array = (if (size2 < size1) {
            java.lang.reflect.Array.newInstance(a.javaClass.componentType, size1)
        } else if (size2 > size1) {
            (a as Array<Any?>)[size1] = null
            a
        } else {
            a
        }) as Array<Any?>

        var remaining = bits
        var index = 0
        while (remaining != 0) {
            val ukIndex = remaining.countTrailingZeroBits()
            array[index++] = _telegramUpdateKinds[ukIndex]

            remaining = remaining and (remaining - 1)
        }
        return array as Array<out T>
    }

    fun toUpdKindSet(): UpdKindSet {
        return UpdKindSet(bits)
    }

    fun copy(): UpdKindMutSet {
        return UpdKindMutSet(bits)
    }

    internal val accessBits get() = bits
}

fun Array<UpdKind>.toUpdKindMutSet(): UpdKindMutSet {
    return UpdKindMutSet(this)
}

fun Collection<UpdKind>.toUpdKindMutSet(): UpdKindMutSet {
    return UpdKindMutSet(this)
}

internal inline fun Int.scanBits(block: (Int) -> Unit) {
    var remaining = this
    while (remaining != 0) {
        val index = remaining.countTrailingZeroBits()
        block(index)
        remaining = remaining and (remaining - 1)
    }
}

internal fun Int.sumSetBitIndexes(): Int =
    (this and 0xAAAA_AAAA.toInt()).countOneBits() +
            (this and 0xCCCC_CCCC.toInt()).countOneBits() * 2 +
            (this and 0xF0F0_F0F0.toInt()).countOneBits() * 4 +
            (this and 0xFF00_FF00.toInt()).countOneBits() * 8 +
            (this and 0xFFFF_0000.toInt()).countOneBits() * 16