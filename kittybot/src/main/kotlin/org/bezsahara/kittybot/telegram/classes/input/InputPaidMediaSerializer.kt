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


internal object InputPaidMediaSerializer : KSerializer<InputPaidMedia> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputPaidMedia") {
        element<String>("type")
        element<TelegramFile>("media")
        element<TelegramFile>("photo", isOptional = true)
        element<TelegramFile?>("thumbnail", isOptional = true)
        element<TelegramFile?>("cover", isOptional = true)
        element<Long?>("start_timestamp", isOptional = true)
        element<Long?>("width", isOptional = true)
        element<Long?>("height", isOptional = true)
        element<Long?>("duration", isOptional = true)
        element<Boolean?>("supports_streaming", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputPaidMedia) {
        when (value) {
            is InputPaidMediaLivePhoto -> InputPaidMediaLivePhoto.serializer().serialize(encoder, value)
            is InputPaidMediaPhoto -> InputPaidMediaPhoto.serializer().serialize(encoder, value)
            is InputPaidMediaVideo -> InputPaidMediaVideo.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): InputPaidMedia {
        var type: String? = null
        var media: TelegramFile? = null
        var photo: TelegramFile? = null
        var thumbnail: TelegramFile? = null
        var cover: TelegramFile? = null
        var startTimestamp: Long? = null
        var width: Long? = null
        var height: Long? = null
        var duration: Long? = null
        var supportsStreaming: Boolean? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> media = dec.decodeSerializableElement(descriptor, 1, TelegramFile.serializer())
                2 -> photo = dec.decodeSerializableElement(descriptor, 2, TelegramFile.serializer())
                3 -> thumbnail = dec.decodeNullableSerializableElement(descriptor, 3, TelegramFile.serializer(), null)
                4 -> cover = dec.decodeNullableSerializableElement(descriptor, 4, TelegramFile.serializer(), null)
                5 -> startTimestamp = dec.decodeNullableSerializableElement(descriptor, 5, Long.serializer(), null)
                6 -> width = dec.decodeNullableSerializableElement(descriptor, 6, Long.serializer(), null)
                7 -> height = dec.decodeNullableSerializableElement(descriptor, 7, Long.serializer(), null)
                8 -> duration = dec.decodeNullableSerializableElement(descriptor, 8, Long.serializer(), null)
                9 -> supportsStreaming = dec.decodeNullableSerializableElement(descriptor, 9, Boolean.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing InputPaidMedia")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "live_photo" -> InputPaidMediaLivePhoto(
                media = media ?: throwMissingField("media"),
                photo = photo ?: throwMissingField("photo")
            )
            "photo" -> InputPaidMediaPhoto(
                media = media ?: throwMissingField("media")
            )
            "video" -> InputPaidMediaVideo(
                media = media ?: throwMissingField("media"),
                thumbnail = thumbnail,
                cover = cover,
                startTimestamp = startTimestamp,
                width = width,
                height = height,
                duration = duration,
                supportsStreaming = supportsStreaming
            )
            else -> throw SerializationException("Serializer wasn't found for InputPaidMedia with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing InputPaidMedia")
    }
}
