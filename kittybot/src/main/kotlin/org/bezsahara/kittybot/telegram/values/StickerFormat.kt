package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type InputSticker.format
 * - method uploadStickerFile.sticker_format
 * - method setStickerSetThumbnail.format
 */
@Serializable(with = StickerFormatSerializer::class)
class StickerFormat internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<StickerFormat.Known>() {
    companion object : ResolveEnumLike<StickerFormat>() {
        @JvmField
        val STATIC = StickerFormat("static", Known.STATIC)

        @JvmField
        val ANIMATED = StickerFormat("animated", Known.ANIMATED)

        @JvmField
        val VIDEO = StickerFormat("video", Known.VIDEO)

        override fun resolve(value: String): StickerFormat {
            return when (value) {
                "static" -> STATIC
                "animated" -> ANIMATED
                "video" -> VIDEO
                else -> StickerFormat(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        STATIC,
        ANIMATED,
        VIDEO;

        fun toStickerFormat(): StickerFormat {
            return when (this) {
                STATIC -> StickerFormat.STATIC
                ANIMATED -> StickerFormat.ANIMATED
                VIDEO -> StickerFormat.VIDEO
            }
        }
    }

    override fun toString(): String {
        return "StickerFormat($value)"
    }
}

internal object StickerFormatSerializer : EnumLikeJsonSerializer<StickerFormat>("StickerFormat", StickerFormat)

