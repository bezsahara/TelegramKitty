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
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.values.ParseMode


internal object InputPollOptionMediaSerializer : KSerializer<InputPollOptionMedia> {
    private val listSerializer0 = ListSerializer(MessageEntity.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputPollOptionMedia") {
        element<String>("type")
        element<TelegramFile>("media", isOptional = true)
        element<TelegramFile?>("thumbnail", isOptional = true)
        element<String?>("caption", isOptional = true)
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media", isOptional = true)
        element<Long?>("width", isOptional = true)
        element<Long?>("height", isOptional = true)
        element<Long?>("duration", isOptional = true)
        element<Boolean?>("has_spoiler", isOptional = true)
        element<String>("url", isOptional = true)
        element<TelegramFile>("photo", isOptional = true)
        element<Double>("latitude", isOptional = true)
        element<Double>("longitude", isOptional = true)
        element<Double?>("horizontal_accuracy", isOptional = true)
        element<String?>("emoji", isOptional = true)
        element<String>("title", isOptional = true)
        element<String>("address", isOptional = true)
        element<String?>("foursquare_id", isOptional = true)
        element<String?>("foursquare_type", isOptional = true)
        element<String?>("google_place_id", isOptional = true)
        element<String?>("google_place_type", isOptional = true)
        element<TelegramFile?>("cover", isOptional = true)
        element<Long?>("start_timestamp", isOptional = true)
        element<Boolean?>("supports_streaming", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputPollOptionMedia) {
        when (value) {
            is InputMediaAnimation -> InputMediaAnimation.serializer().serialize(encoder, value)
            is InputMediaLink -> InputMediaLink.serializer().serialize(encoder, value)
            is InputMediaLivePhoto -> InputMediaLivePhoto.serializer().serialize(encoder, value)
            is InputMediaLocation -> InputMediaLocation.serializer().serialize(encoder, value)
            is InputMediaPhoto -> InputMediaPhoto.serializer().serialize(encoder, value)
            is InputMediaSticker -> InputMediaSticker.serializer().serialize(encoder, value)
            is InputMediaVenue -> InputMediaVenue.serializer().serialize(encoder, value)
            is InputMediaVideo -> InputMediaVideo.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): InputPollOptionMedia {
        var nativeMask = IntMask.EMPTY
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
        var url: String? = null
        var photo: TelegramFile? = null
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var horizontalAccuracy: Double? = null
        var emoji: String? = null
        var title: String? = null
        var address: String? = null
        var foursquareId: String? = null
        var foursquareType: String? = null
        var googlePlaceId: String? = null
        var googlePlaceType: String? = null
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
                11 -> url = dec.decodeStringElement(descriptor, 11)
                12 -> photo = dec.decodeSerializableElement(descriptor, 12, TelegramFile.serializer())
                13 -> {
                    latitude = dec.decodeDoubleElement(descriptor, 13)
                    nativeMask = nativeMask.setBit(0)
                }
                14 -> {
                    longitude = dec.decodeDoubleElement(descriptor, 14)
                    nativeMask = nativeMask.setBit(1)
                }
                15 -> horizontalAccuracy = dec.decodeNullableSerializableElement(descriptor, 15, Double.serializer(), null)
                16 -> emoji = dec.decodeNullableSerializableElement(descriptor, 16, String.serializer(), null)
                17 -> title = dec.decodeStringElement(descriptor, 17)
                18 -> address = dec.decodeStringElement(descriptor, 18)
                19 -> foursquareId = dec.decodeNullableSerializableElement(descriptor, 19, String.serializer(), null)
                20 -> foursquareType = dec.decodeNullableSerializableElement(descriptor, 20, String.serializer(), null)
                21 -> googlePlaceId = dec.decodeNullableSerializableElement(descriptor, 21, String.serializer(), null)
                22 -> googlePlaceType = dec.decodeNullableSerializableElement(descriptor, 22, String.serializer(), null)
                23 -> cover = dec.decodeNullableSerializableElement(descriptor, 23, TelegramFile.serializer(), null)
                24 -> startTimestamp = dec.decodeNullableSerializableElement(descriptor, 24, Long.serializer(), null)
                25 -> supportsStreaming = dec.decodeNullableSerializableElement(descriptor, 25, Boolean.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing InputPollOptionMedia")
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
            "link" -> InputMediaLink(
                url = url ?: throwMissingField("url")
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
            "location" -> InputMediaLocation(
                latitude = if (nativeMask.has(0)) latitude else throwMissingField("latitude"),
                longitude = if (nativeMask.has(1)) longitude else throwMissingField("longitude"),
                horizontalAccuracy = horizontalAccuracy
            )
            "photo" -> InputMediaPhoto(
                media = media ?: throwMissingField("media"),
                caption = caption,
                parseMode = parseMode,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                hasSpoiler = hasSpoiler
            )
            "sticker" -> InputMediaSticker(
                media = media ?: throwMissingField("media"),
                emoji = emoji
            )
            "venue" -> InputMediaVenue(
                latitude = if (nativeMask.has(0)) latitude else throwMissingField("latitude"),
                longitude = if (nativeMask.has(1)) longitude else throwMissingField("longitude"),
                title = title ?: throwMissingField("title"),
                address = address ?: throwMissingField("address"),
                foursquareId = foursquareId,
                foursquareType = foursquareType,
                googlePlaceId = googlePlaceId,
                googlePlaceType = googlePlaceType
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
            else -> throw SerializationException("Serializer wasn't found for InputPollOptionMedia with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing InputPollOptionMedia")
    }
}
