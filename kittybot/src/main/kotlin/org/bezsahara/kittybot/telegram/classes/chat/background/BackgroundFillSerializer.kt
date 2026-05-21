package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask


internal object BackgroundFillSerializer : KSerializer<BackgroundFill> {
    private val listSerializer0 = ListSerializer(Long.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BackgroundFill") {
        element<String>("type")
        element<Long>("color", isOptional = true)
        element<Long>("top_color", isOptional = true)
        element<Long>("bottom_color", isOptional = true)
        element<Long>("rotation_angle", isOptional = true)
        element<List<Long>>("colors", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: BackgroundFill) {
        when (value) {
            is BackgroundFillSolid -> BackgroundFillSolid.serializer().serialize(encoder, value)
            is BackgroundFillGradient -> BackgroundFillGradient.serializer().serialize(encoder, value)
            is BackgroundFillFreeformGradient -> BackgroundFillFreeformGradient.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): BackgroundFill {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var color: Long = 0L
        var topColor: Long = 0L
        var bottomColor: Long = 0L
        var rotationAngle: Long = 0L
        var colors: List<Long>? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> {
                    color = dec.decodeLongElement(descriptor, 1)
                    nativeMask = nativeMask.setBit(0)
                }
                2 -> {
                    topColor = dec.decodeLongElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(1)
                }
                3 -> {
                    bottomColor = dec.decodeLongElement(descriptor, 3)
                    nativeMask = nativeMask.setBit(2)
                }
                4 -> {
                    rotationAngle = dec.decodeLongElement(descriptor, 4)
                    nativeMask = nativeMask.setBit(3)
                }
                5 -> colors = dec.decodeSerializableElement(descriptor, 5, listSerializer0)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing BackgroundFill")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "solid" -> BackgroundFillSolid(
                color = if (nativeMask.has(0)) color else throwMissingField("color")
            )
            "gradient" -> BackgroundFillGradient(
                topColor = if (nativeMask.has(1)) topColor else throwMissingField("top_color"),
                bottomColor = if (nativeMask.has(2)) bottomColor else throwMissingField("bottom_color"),
                rotationAngle = if (nativeMask.has(3)) rotationAngle else throwMissingField("rotation_angle")
            )
            "freeform_gradient" -> BackgroundFillFreeformGradient(
                colors = colors ?: throwMissingField("colors")
            )
            else -> throw SerializationException("Serializer wasn't found for BackgroundFill with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing BackgroundFill")
    }
}
