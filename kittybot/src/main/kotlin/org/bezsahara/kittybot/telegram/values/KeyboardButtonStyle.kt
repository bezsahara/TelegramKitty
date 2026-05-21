package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type InlineKeyboardButton.style
 * - type KeyboardButton.style
 */
@Serializable(with = KeyboardButtonStyleSerializer::class)
class KeyboardButtonStyle internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<KeyboardButtonStyle.Known>() {
    companion object : ResolveEnumLike<KeyboardButtonStyle>() {
        @JvmField
        val DANGER = KeyboardButtonStyle("danger", Known.DANGER)

        @JvmField
        val SUCCESS = KeyboardButtonStyle("success", Known.SUCCESS)

        @JvmField
        val PRIMARY = KeyboardButtonStyle("primary", Known.PRIMARY)

        override fun resolve(value: String): KeyboardButtonStyle {
            return when (value) {
                "danger" -> DANGER
                "success" -> SUCCESS
                "primary" -> PRIMARY
                else -> KeyboardButtonStyle(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        DANGER,
        SUCCESS,
        PRIMARY;

        fun toKeyboardButtonStyle(): KeyboardButtonStyle {
            return when (this) {
                DANGER -> KeyboardButtonStyle.DANGER
                SUCCESS -> KeyboardButtonStyle.SUCCESS
                PRIMARY -> KeyboardButtonStyle.PRIMARY
            }
        }
    }

    override fun toString(): String {
        return "KeyboardButtonStyle($value)"
    }
}

internal object KeyboardButtonStyleSerializer : EnumLikeJsonSerializer<KeyboardButtonStyle>("KeyboardButtonStyle", KeyboardButtonStyle)

