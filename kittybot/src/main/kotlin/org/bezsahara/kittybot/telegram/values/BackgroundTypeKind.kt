package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype BackgroundType.type
 * - type BackgroundTypeFill.type
 * - type BackgroundTypeWallpaper.type
 * - type BackgroundTypePattern.type
 * - type BackgroundTypeChatTheme.type
 */
@Serializable(with = BackgroundTypeKindSerializer::class)
class BackgroundTypeKind internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<BackgroundTypeKind.Known>() {
    companion object : ResolveEnumLike<BackgroundTypeKind>() {
        @JvmField
        val FILL = BackgroundTypeKind("fill", Known.FILL)

        @JvmField
        val WALLPAPER = BackgroundTypeKind("wallpaper", Known.WALLPAPER)

        @JvmField
        val PATTERN = BackgroundTypeKind("pattern", Known.PATTERN)

        @JvmField
        val CHAT_THEME = BackgroundTypeKind("chat_theme", Known.CHAT_THEME)

        override fun resolve(value: String): BackgroundTypeKind {
            return when (value) {
                "fill" -> FILL
                "wallpaper" -> WALLPAPER
                "pattern" -> PATTERN
                "chat_theme" -> CHAT_THEME
                else -> BackgroundTypeKind(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        FILL,
        WALLPAPER,
        PATTERN,
        CHAT_THEME;

        fun toBackgroundTypeKind(): BackgroundTypeKind {
            return when (this) {
                FILL -> BackgroundTypeKind.FILL
                WALLPAPER -> BackgroundTypeKind.WALLPAPER
                PATTERN -> BackgroundTypeKind.PATTERN
                CHAT_THEME -> BackgroundTypeKind.CHAT_THEME
            }
        }
    }

    override fun toString(): String {
        return "BackgroundTypeKind($value)"
    }
}

internal object BackgroundTypeKindSerializer : EnumLikeJsonSerializer<BackgroundTypeKind>("BackgroundTypeKind", BackgroundTypeKind)

