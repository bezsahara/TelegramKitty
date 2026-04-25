package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type InlineQueryResultGif.thumbnail_mime_type
 * - type InlineQueryResultMpeg4Gif.thumbnail_mime_type
 */
@Serializable(with = InlineQueryThumbnailMimeTypeSerializer::class)
class InlineQueryThumbnailMimeType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InlineQueryThumbnailMimeType.Known>() {
    companion object : ResolveEnumLike<InlineQueryThumbnailMimeType>() {
        @JvmField
        val IMAGE_JPEG = InlineQueryThumbnailMimeType("image/jpeg", Known.IMAGE_JPEG)

        @JvmField
        val IMAGE_GIF = InlineQueryThumbnailMimeType("image/gif", Known.IMAGE_GIF)

        @JvmField
        val VIDEO_MP4 = InlineQueryThumbnailMimeType("video/mp4", Known.VIDEO_MP4)

        override fun resolve(value: String): InlineQueryThumbnailMimeType {
            return when (value) {
                "image/jpeg" -> IMAGE_JPEG
                "image/gif" -> IMAGE_GIF
                "video/mp4" -> VIDEO_MP4
                else -> InlineQueryThumbnailMimeType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        IMAGE_JPEG,
        IMAGE_GIF,
        VIDEO_MP4;

        fun toInlineQueryThumbnailMimeType(): InlineQueryThumbnailMimeType {
            return when (this) {
                IMAGE_JPEG -> InlineQueryThumbnailMimeType.IMAGE_JPEG
                IMAGE_GIF -> InlineQueryThumbnailMimeType.IMAGE_GIF
                VIDEO_MP4 -> InlineQueryThumbnailMimeType.VIDEO_MP4
            }
        }
    }

    override fun toString(): String {
        return "InlineQueryThumbnailMimeType($value)"
    }
}

internal object InlineQueryThumbnailMimeTypeSerializer : EnumLikeJsonSerializer<InlineQueryThumbnailMimeType>("InlineQueryThumbnailMimeType", InlineQueryThumbnailMimeType)

