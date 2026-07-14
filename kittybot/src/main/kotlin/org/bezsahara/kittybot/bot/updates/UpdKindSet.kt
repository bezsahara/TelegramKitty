package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.other.KotlinHelpers
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update._telegramUpdateKinds

class UpdKindSet internal constructor(
    private val bits: Int
) : AbstractSet<UpdKind>() {
    constructor(el: Array<UpdKind>) : this(KotlinHelpers.updKindArrayToInt(el))

    constructor(el: Collection<UpdKind>) : this(KotlinHelpers.updKindArrayToInt(el))

    constructor() : this(0)

    override val size: Int get() = bits.countOneBits()

    override fun contains(element: UpdKind): Boolean {
        return (bits and (1 shl element.ordinal)) != 0
    }

    internal fun has(ordinal: Int): Boolean {
        return (bits and (1 shl ordinal)) != 0
    }

    override fun iterator(): Iterator<UpdKind> {
        return Itr(bits)
    }

    class Itr(bits: Int) : Iterator<UpdKind> {
        private var remaining = bits

        override fun hasNext(): Boolean = remaining != 0

        override fun next(): UpdKind {
            if (remaining == 0) {
                throw NoSuchElementException()
            }

            val index = remaining.countTrailingZeroBits()

            // Remove this bit from the iterator's remaining work.
            remaining = remaining and (remaining - 1)

            return _telegramUpdateKinds[index]
        }
    }

    override fun isEmpty(): Boolean {
        return bits == 0
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is UpdKindSet -> bits == other.bits
            is UpdKindMutSet -> bits == other.accessBits
            is Set<*> -> other.size == size && other.containsAll(this)
            else -> false
        }
    }

    override fun hashCode(): Int {
        return bits.sumSetBitIndexes()
    }

    override fun toArray(): Array<Any?> {
        return toArray(arrayOfNulls<Any>(size))
    }

    override fun <T: Any?> toArray(a: Array<T>): Array<T> {
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
        return array as Array<T>
    }

    fun toUpdKindMutSet(): UpdKindMutSet {
        return UpdKindMutSet(bits)
    }

    internal val accessBits get() = bits
}

fun Array<UpdKind>.toUpdKindSet(): UpdKindSet {
    return UpdKindSet(this)
}

fun Collection<UpdKind>.toUpdKindSet(): UpdKindSet {
    if (this is UpdKindSet) return this
    return UpdKindSet(this)
}