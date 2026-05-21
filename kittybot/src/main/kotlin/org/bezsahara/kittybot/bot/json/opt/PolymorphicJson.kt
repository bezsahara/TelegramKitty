package org.bezsahara.kittybot.bot.json.opt

import kotlinx.serialization.SealedSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind


@OptIn(SealedSerializationApi::class)
internal fun deferSerial(deferred: () -> SerialDescriptor): SerialDescriptor = object : SerialDescriptor {

    private val original: SerialDescriptor by lazy(deferred)

    override val serialName: String
        get() = original.serialName
    override val kind: SerialKind
        get() = original.kind
    override val elementsCount: Int
        get() = original.elementsCount

    override fun getElementName(index: Int): String = original.getElementName(index)
    override fun getElementIndex(name: String): Int = original.getElementIndex(name)
    override fun getElementAnnotations(index: Int): List<Annotation> = original.getElementAnnotations(index)
    override fun getElementDescriptor(index: Int): SerialDescriptor = original.getElementDescriptor(index)
    override fun isElementOptional(index: Int): Boolean = original.isElementOptional(index)
}

@Suppress("NOTHING_TO_INLINE")
@JvmInline
value class IntMask(val bits: Int) {

    inline fun setBit(bitNumber: Int): IntMask {
        return IntMask(bits or (1 shl bitNumber))
    }

    inline fun has(bitNumber: Int): Boolean {
        return (bits and (1 shl bitNumber)) != 0
    }

    inline fun clearBit(bitNumber: Int): IntMask {
        return IntMask(bits and (1 shl bitNumber).inv())
    }

    inline fun hasAll(mask: Int): Boolean {
        return (bits and mask) == mask
    }

    inline fun hasAny(mask: Int): Boolean {
        return (bits and mask) != 0
    }

    companion object {
        val EMPTY = IntMask(0)

        fun bit(bitNumber: Int): IntMask {
            require(bitNumber in 0..31) {
                "bitNumber must be in 0..31, got $bitNumber"
            }

            return IntMask(1 shl bitNumber)
        }
    }
}
