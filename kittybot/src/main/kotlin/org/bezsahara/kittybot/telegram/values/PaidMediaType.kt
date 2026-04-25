package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype PaidMedia.type
 * - type PaidMediaPreview.type
 * - type PaidMediaPhoto.type
 * - type PaidMediaVideo.type
 */
@Serializable(with = PaidMediaTypeSerializer::class)
class PaidMediaType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<PaidMediaType.Known>() {
    companion object : ResolveEnumLike<PaidMediaType>() {
        @JvmField
        val PREVIEW = PaidMediaType("preview", Known.PREVIEW)

        @JvmField
        val PHOTO = PaidMediaType("photo", Known.PHOTO)

        @JvmField
        val VIDEO = PaidMediaType("video", Known.VIDEO)

        override fun resolve(value: String): PaidMediaType {
            return when (value) {
                "preview" -> PREVIEW
                "photo" -> PHOTO
                "video" -> VIDEO
                else -> PaidMediaType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PREVIEW,
        PHOTO,
        VIDEO;

        fun toPaidMediaType(): PaidMediaType {
            return when (this) {
                PREVIEW -> PaidMediaType.PREVIEW
                PHOTO -> PaidMediaType.PHOTO
                VIDEO -> PaidMediaType.VIDEO
            }
        }
    }

    override fun toString(): String {
        return "PaidMediaType($value)"
    }
}

internal object PaidMediaTypeSerializer : EnumLikeJsonSerializer<PaidMediaType>("PaidMediaType", PaidMediaType)

