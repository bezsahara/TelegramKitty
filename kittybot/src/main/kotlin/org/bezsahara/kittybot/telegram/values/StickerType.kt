package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type Sticker.type
 * - type StickerSet.sticker_type
 * - method createNewStickerSet.sticker_type
 */
@Serializable(with = StickerTypeSerializer::class)
class StickerType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<StickerType.Known>() {
    companion object : ResolveEnumLike<StickerType>() {
        @JvmField
        val REGULAR = StickerType("regular", Known.REGULAR)

        @JvmField
        val MASK = StickerType("mask", Known.MASK)

        @JvmField
        val CUSTOM_EMOJI = StickerType("custom_emoji", Known.CUSTOM_EMOJI)

        override fun resolve(value: String): StickerType {
            return when (value) {
                "regular" -> REGULAR
                "mask" -> MASK
                "custom_emoji" -> CUSTOM_EMOJI
                else -> StickerType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        REGULAR,
        MASK,
        CUSTOM_EMOJI;

        fun toStickerType(): StickerType {
            return when (this) {
                REGULAR -> StickerType.REGULAR
                MASK -> StickerType.MASK
                CUSTOM_EMOJI -> StickerType.CUSTOM_EMOJI
            }
        }
    }

    override fun toString(): String {
        return "StickerType($value)"
    }
}

internal object StickerTypeSerializer : EnumLikeJsonSerializer<StickerType>("StickerType", StickerType)

