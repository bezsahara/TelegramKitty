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


internal object InputStoryContentSerializer : KSerializer<InputStoryContent> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputStoryContent") {
        element<String>("type")
        element<TelegramFile>("photo", isOptional = true)
        element<TelegramFile>("video", isOptional = true)
        element<Double?>("duration", isOptional = true)
        element<Double?>("cover_frame_timestamp", isOptional = true)
        element<Boolean?>("is_animation", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputStoryContent) {
        when (value) {
            is InputStoryContentPhoto -> InputStoryContentPhoto.serializer().serialize(encoder, value)
            is InputStoryContentVideo -> InputStoryContentVideo.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): InputStoryContent {
        var type: String? = null
        var photo: TelegramFile? = null
        var video: TelegramFile? = null
        var duration: Double? = null
        var coverFrameTimestamp: Double? = null
        var isAnimation: Boolean? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> photo = dec.decodeSerializableElement(descriptor, 1, TelegramFile.serializer())
                2 -> video = dec.decodeSerializableElement(descriptor, 2, TelegramFile.serializer())
                3 -> duration = dec.decodeNullableSerializableElement(descriptor, 3, Double.serializer(), null)
                4 -> coverFrameTimestamp = dec.decodeNullableSerializableElement(descriptor, 4, Double.serializer(), null)
                5 -> isAnimation = dec.decodeNullableSerializableElement(descriptor, 5, Boolean.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing InputStoryContent")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "photo" -> InputStoryContentPhoto(
                photo = photo ?: throwMissingField("photo")
            )
            "video" -> InputStoryContentVideo(
                video = video ?: throwMissingField("video"),
                duration = duration,
                coverFrameTimestamp = coverFrameTimestamp,
                isAnimation = isAnimation
            )
            else -> throw SerializationException("Serializer wasn't found for InputStoryContent with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing InputStoryContent")
    }
}
