package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype InputStoryContent.type
 * - type InputStoryContentPhoto.type
 * - type InputStoryContentVideo.type
 */
@Serializable(with = InputStoryContentTypeSerializer::class)
class InputStoryContentType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InputStoryContentType.Known>() {
    companion object : ResolveEnumLike<InputStoryContentType>() {
        @JvmField
        val PHOTO = InputStoryContentType("photo", Known.PHOTO)

        @JvmField
        val VIDEO = InputStoryContentType("video", Known.VIDEO)

        override fun resolve(value: String): InputStoryContentType {
            return when (value) {
                "photo" -> PHOTO
                "video" -> VIDEO
                else -> InputStoryContentType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PHOTO,
        VIDEO;

        fun toInputStoryContentType(): InputStoryContentType {
            return when (this) {
                PHOTO -> InputStoryContentType.PHOTO
                VIDEO -> InputStoryContentType.VIDEO
            }
        }
    }

    override fun toString(): String {
        return "InputStoryContentType($value)"
    }
}

internal object InputStoryContentTypeSerializer : EnumLikeJsonSerializer<InputStoryContentType>("InputStoryContentType", InputStoryContentType)

