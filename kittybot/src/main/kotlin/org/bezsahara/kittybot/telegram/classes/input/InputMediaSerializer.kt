package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.ExperimentalSerializationApi
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
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.values.ParseMode


internal object InputMediaSerializer : KSerializer<InputMedia> {
    private val listSerializer0 = ListSerializer(MessageEntity.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMedia") {
        element<String>("type")
        element<TelegramFile>("media")
        element<TelegramFile?>("thumbnail", isOptional = true)
        element<String?>("caption", isOptional = true)
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media", isOptional = true)
        element<Long?>("width", isOptional = true)
        element<Long?>("height", isOptional = true)
        element<Long?>("duration", isOptional = true)
        element<Boolean?>("has_spoiler", isOptional = true)
        element<String?>("performer", isOptional = true)
        element<String?>("title", isOptional = true)
        element<Boolean?>("disable_content_type_detection", isOptional = true)
        element<TelegramFile>("photo", isOptional = true)
        element<TelegramFile?>("cover", isOptional = true)
        element<Long?>("start_timestamp", isOptional = true)
        element<Boolean?>("supports_streaming", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputMedia) {
        when (value) {
            is InputMediaAnimation -> InputMediaAnimation.serializer().serialize(encoder, value)
            is InputMediaAudio -> InputMediaAudio.serializer().serialize(encoder, value)
            is InputMediaDocument -> InputMediaDocument.serializer().serialize(encoder, value)
            is InputMediaLivePhoto -> InputMediaLivePhoto.serializer().serialize(encoder, value)
            is InputMediaPhoto -> InputMediaPhoto.serializer().serialize(encoder, value)
            is InputMediaVideo -> InputMediaVideo.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): InputMedia {
        var type: String? = null
        var media: TelegramFile? = null
        var thumbnail: TelegramFile? = null
        var caption: String? = null
        var parseMode: ParseMode? = null
        var captionEntities: List<MessageEntity>? = null
        var showCaptionAboveMedia: Boolean? = null
        var width: Long? = null
        var height: Long? = null
        var duration: Long? = null
        var hasSpoiler: Boolean? = null
        var performer: String? = null
        var title: String? = null
        var disableContentTypeDetection: Boolean? = null
        var photo: TelegramFile? = null
        var cover: TelegramFile? = null
        var startTimestamp: Long? = null
        var supportsStreaming: Boolean? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> media = dec.decodeSerializableElement(descriptor, 1, TelegramFile.serializer())
                2 -> thumbnail = dec.decodeNullableSerializableElement(descriptor, 2, TelegramFile.serializer(), null)
                3 -> caption = dec.decodeNullableSerializableElement(descriptor, 3, String.serializer(), null)
                4 -> parseMode = dec.decodeNullableSerializableElement(descriptor, 4, ParseMode.serializer(), null)
                5 -> captionEntities = dec.decodeNullableSerializableElement(descriptor, 5, listSerializer0, null)
                6 -> showCaptionAboveMedia = dec.decodeNullableSerializableElement(descriptor, 6, Boolean.serializer(), null)
                7 -> width = dec.decodeNullableSerializableElement(descriptor, 7, Long.serializer(), null)
                8 -> height = dec.decodeNullableSerializableElement(descriptor, 8, Long.serializer(), null)
                9 -> duration = dec.decodeNullableSerializableElement(descriptor, 9, Long.serializer(), null)
                10 -> hasSpoiler = dec.decodeNullableSerializableElement(descriptor, 10, Boolean.serializer(), null)
                11 -> performer = dec.decodeNullableSerializableElement(descriptor, 11, String.serializer(), null)
                12 -> title = dec.decodeNullableSerializableElement(descriptor, 12, String.serializer(), null)
                13 -> disableContentTypeDetection = dec.decodeNullableSerializableElement(descriptor, 13, Boolean.serializer(), null)
                14 -> photo = dec.decodeSerializableElement(descriptor, 14, TelegramFile.serializer())
                15 -> cover = dec.decodeNullableSerializableElement(descriptor, 15, TelegramFile.serializer(), null)
                16 -> startTimestamp = dec.decodeNullableSerializableElement(descriptor, 16, Long.serializer(), null)
                17 -> supportsStreaming = dec.decodeNullableSerializableElement(descriptor, 17, Boolean.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing InputMedia")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "animation" -> InputMediaAnimation(
                media = media ?: throwMissingField("media"),
                thumbnail = thumbnail,
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                width = width,
                height = height,
                duration = duration,
                hasSpoiler = hasSpoiler
            )
            "audio" -> InputMediaAudio(
                media = media ?: throwMissingField("media"),
                thumbnail = thumbnail,
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                duration = duration,
                performer = performer,
                title = title
            )
            "document" -> InputMediaDocument(
                media = media ?: throwMissingField("media"),
                thumbnail = thumbnail,
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                disableContentTypeDetection = disableContentTypeDetection
            )
            "live_photo" -> InputMediaLivePhoto(
                media = media ?: throwMissingField("media"),
                photo = photo ?: throwMissingField("photo"),
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                hasSpoiler = hasSpoiler
            )
            "photo" -> InputMediaPhoto(
                media = media ?: throwMissingField("media"),
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                hasSpoiler = hasSpoiler
            )
            "video" -> InputMediaVideo(
                media = media ?: throwMissingField("media"),
                thumbnail = thumbnail,
                cover = cover,
                startTimestamp = startTimestamp,
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                width = width,
                height = height,
                duration = duration,
                supportsStreaming = supportsStreaming,
                hasSpoiler = hasSpoiler
            )
            else -> throw SerializationException("Serializer wasn't found for InputMedia with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing InputMedia")
    }
}
