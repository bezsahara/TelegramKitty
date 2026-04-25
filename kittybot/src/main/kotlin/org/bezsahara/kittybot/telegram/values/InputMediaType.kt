package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype InputMedia.type
 * - type InputMediaAnimation.type
 * - type InputMediaDocument.type
 * - type InputMediaAudio.type
 * - type InputMediaPhoto.type
 * - type InputMediaVideo.type
 */
@Serializable(with = InputMediaTypeSerializer::class)
class InputMediaType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InputMediaType.Known>() {
    companion object : ResolveEnumLike<InputMediaType>() {
        @JvmField
        val ANIMATION = InputMediaType("animation", Known.ANIMATION)

        @JvmField
        val DOCUMENT = InputMediaType("document", Known.DOCUMENT)

        @JvmField
        val AUDIO = InputMediaType("audio", Known.AUDIO)

        @JvmField
        val PHOTO = InputMediaType("photo", Known.PHOTO)

        @JvmField
        val VIDEO = InputMediaType("video", Known.VIDEO)

        override fun resolve(value: String): InputMediaType {
            return when (value) {
                "animation" -> ANIMATION
                "document" -> DOCUMENT
                "audio" -> AUDIO
                "photo" -> PHOTO
                "video" -> VIDEO
                else -> InputMediaType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        ANIMATION,
        DOCUMENT,
        AUDIO,
        PHOTO,
        VIDEO;

        fun toInputMediaType(): InputMediaType {
            return when (this) {
                ANIMATION -> InputMediaType.ANIMATION
                DOCUMENT -> InputMediaType.DOCUMENT
                AUDIO -> InputMediaType.AUDIO
                PHOTO -> InputMediaType.PHOTO
                VIDEO -> InputMediaType.VIDEO
            }
        }
    }

    override fun toString(): String {
        return "InputMediaType($value)"
    }
}

internal object InputMediaTypeSerializer : EnumLikeJsonSerializer<InputMediaType>("InputMediaType", InputMediaType)

