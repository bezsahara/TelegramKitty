package org.bezsahara.kittybot.telegram.classes.media

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


internal object PaidMediaSerializer : KSerializer<PaidMedia> {
    private val listSerializer0 = ListSerializer(PhotoSize.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PaidMedia") {
        element<String>("type")
        element<LivePhoto>("live_photo", isOptional = true)
        element<List<PhotoSize>>("photo", isOptional = true)
        element<Long?>("width", isOptional = true)
        element<Long?>("height", isOptional = true)
        element<Long?>("duration", isOptional = true)
        element<Video>("video", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: PaidMedia) {
        when (value) {
            is PaidMediaLivePhoto -> PaidMediaLivePhoto.serializer().serialize(encoder, value)
            is PaidMediaPhoto -> PaidMediaPhoto.serializer().serialize(encoder, value)
            is PaidMediaPreview -> PaidMediaPreview.serializer().serialize(encoder, value)
            is PaidMediaVideo -> PaidMediaVideo.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): PaidMedia {
        var type: String? = null
        var livePhoto: LivePhoto? = null
        var photo: List<PhotoSize>? = null
        var width: Long? = null
        var height: Long? = null
        var duration: Long? = null
        var video: Video? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> livePhoto = dec.decodeSerializableElement(descriptor, 1, LivePhoto.serializer())
                2 -> photo = dec.decodeSerializableElement(descriptor, 2, listSerializer0)
                3 -> width = dec.decodeNullableSerializableElement(descriptor, 3, Long.serializer(), null)
                4 -> height = dec.decodeNullableSerializableElement(descriptor, 4, Long.serializer(), null)
                5 -> duration = dec.decodeNullableSerializableElement(descriptor, 5, Long.serializer(), null)
                6 -> video = dec.decodeSerializableElement(descriptor, 6, Video.serializer())
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing PaidMedia")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "live_photo" -> PaidMediaLivePhoto(
                livePhoto = livePhoto ?: throwMissingField("live_photo")
            )
            "photo" -> PaidMediaPhoto(
                photo = photo ?: throwMissingField("photo")
            )
            "preview" -> PaidMediaPreview(
                width = width,
                height = height,
                duration = duration
            )
            "video" -> PaidMediaVideo(
                video = video ?: throwMissingField("video")
            )
            else -> throw SerializationException("Serializer wasn't found for PaidMedia with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing PaidMedia")
    }
}
