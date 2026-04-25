package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype MenuButton.type
 * - type MenuButtonCommands.type
 * - type MenuButtonWebApp.type
 * - type MenuButtonDefault.type
 */
@Serializable(with = MenuButtonTypeSerializer::class)
class MenuButtonType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<MenuButtonType.Known>() {
    companion object : ResolveEnumLike<MenuButtonType>() {
        @JvmField
        val COMMANDS = MenuButtonType("commands", Known.COMMANDS)

        @JvmField
        val WEB_APP = MenuButtonType("web_app", Known.WEB_APP)

        @JvmField
        val DEFAULT = MenuButtonType("default", Known.DEFAULT)

        override fun resolve(value: String): MenuButtonType {
            return when (value) {
                "commands" -> COMMANDS
                "web_app" -> WEB_APP
                "default" -> DEFAULT
                else -> MenuButtonType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        COMMANDS,
        WEB_APP,
        DEFAULT;

        fun toMenuButtonType(): MenuButtonType {
            return when (this) {
                COMMANDS -> MenuButtonType.COMMANDS
                WEB_APP -> MenuButtonType.WEB_APP
                DEFAULT -> MenuButtonType.DEFAULT
            }
        }
    }

    override fun toString(): String {
        return "MenuButtonType($value)"
    }
}

internal object MenuButtonTypeSerializer : EnumLikeJsonSerializer<MenuButtonType>("MenuButtonType", MenuButtonType)

