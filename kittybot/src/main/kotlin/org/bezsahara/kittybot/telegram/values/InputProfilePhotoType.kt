package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype InputProfilePhoto.type
 * - type InputProfilePhotoStatic.type
 * - type InputProfilePhotoAnimated.type
 */
@Serializable(with = InputProfilePhotoTypeSerializer::class)
class InputProfilePhotoType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InputProfilePhotoType.Known>() {
    companion object : ResolveEnumLike<InputProfilePhotoType>() {
        @JvmField
        val STATIC = InputProfilePhotoType("static", Known.STATIC)

        @JvmField
        val ANIMATED = InputProfilePhotoType("animated", Known.ANIMATED)

        override fun resolve(value: String): InputProfilePhotoType {
            return when (value) {
                "static" -> STATIC
                "animated" -> ANIMATED
                else -> InputProfilePhotoType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        STATIC,
        ANIMATED;

        fun toInputProfilePhotoType(): InputProfilePhotoType {
            return when (this) {
                STATIC -> InputProfilePhotoType.STATIC
                ANIMATED -> InputProfilePhotoType.ANIMATED
            }
        }
    }

    override fun toString(): String {
        return "InputProfilePhotoType($value)"
    }
}

internal object InputProfilePhotoTypeSerializer : EnumLikeJsonSerializer<InputProfilePhotoType>("InputProfilePhotoType", InputProfilePhotoType)

