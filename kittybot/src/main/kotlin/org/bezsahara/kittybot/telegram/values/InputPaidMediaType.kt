package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype InputPaidMedia.type
 * - type InputPaidMediaPhoto.type
 * - type InputPaidMediaVideo.type
 */
@Serializable(with = InputPaidMediaTypeSerializer::class)
class InputPaidMediaType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InputPaidMediaType.Known>() {
    companion object : ResolveEnumLike<InputPaidMediaType>() {
        @JvmField
        val PHOTO = InputPaidMediaType("photo", Known.PHOTO)

        @JvmField
        val VIDEO = InputPaidMediaType("video", Known.VIDEO)

        override fun resolve(value: String): InputPaidMediaType {
            return when (value) {
                "photo" -> PHOTO
                "video" -> VIDEO
                else -> InputPaidMediaType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PHOTO,
        VIDEO;

        fun toInputPaidMediaType(): InputPaidMediaType {
            return when (this) {
                PHOTO -> InputPaidMediaType.PHOTO
                VIDEO -> InputPaidMediaType.VIDEO
            }
        }
    }

    override fun toString(): String {
        return "InputPaidMediaType($value)"
    }
}

internal object InputPaidMediaTypeSerializer : EnumLikeJsonSerializer<InputPaidMediaType>("InputPaidMediaType", InputPaidMediaType)

