package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.client.file.TelegramFile


internal object InputProfilePhotoSerializer : KSerializer<InputProfilePhoto> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputProfilePhoto") {
        element<String>("type")
        element<TelegramFile>("photo", isOptional = true)
        element<TelegramFile>("animation", isOptional = true)
        element<Double?>("main_frame_timestamp", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputProfilePhoto) {
        when (value) {
            is InputProfilePhotoStatic -> InputProfilePhotoStatic.serializer().serialize(encoder, value)
            is InputProfilePhotoAnimated -> InputProfilePhotoAnimated.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): InputProfilePhoto {
        var type: String? = null
        var photo: TelegramFile? = null
        var animation: TelegramFile? = null
        var mainFrameTimestamp: Double? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> photo = dec.decodeSerializableElement(descriptor, 1, TelegramFile.serializer())
                2 -> animation = dec.decodeSerializableElement(descriptor, 2, TelegramFile.serializer())
                3 -> mainFrameTimestamp = dec.decodeNullableSerializableElement(descriptor, 3, Double.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing InputProfilePhoto")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "static" -> InputProfilePhotoStatic(
                photo = photo ?: throwMissingField("photo")
            )
            "animated" -> InputProfilePhotoAnimated(
                animation = animation ?: throwMissingField("animation"),
                mainFrameTimestamp = mainFrameTimestamp
            )
            else -> throw SerializationException("Serializer wasn't found for InputProfilePhoto with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing InputProfilePhoto")
    }
}
