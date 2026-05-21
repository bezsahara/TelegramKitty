package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - method *.parse_mode
 * - method *.question_parse_mode
 * - method *.explanation_parse_mode
 * - method *.description_parse_mode
 * - method *.text_parse_mode
 */
@Serializable(with = ParseModeSerializer::class)
class ParseMode internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ParseMode.Known>() {
    companion object : ResolveEnumLike<ParseMode>() {
        @JvmField
        val HTML = ParseMode("HTML", Known.HTML)

        @JvmField
        val MARKDOWN = ParseMode("Markdown", Known.MARKDOWN)

        @JvmField
        val MARKDOWN_V2 = ParseMode("MarkdownV2", Known.MARKDOWN_V2)

        override fun resolve(value: String): ParseMode {
            return when (value) {
                "HTML" -> HTML
                "Markdown" -> MARKDOWN
                "MarkdownV2" -> MARKDOWN_V2
                else -> ParseMode(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        HTML,
        MARKDOWN,
        MARKDOWN_V2;

        fun toParseMode(): ParseMode {
            return when (this) {
                HTML -> ParseMode.HTML
                MARKDOWN -> ParseMode.MARKDOWN
                MARKDOWN_V2 -> ParseMode.MARKDOWN_V2
            }
        }
    }

    override fun toString(): String {
        return "ParseMode($value)"
    }
}

internal object ParseModeSerializer : EnumLikeJsonSerializer<ParseMode>("ParseMode", ParseMode)

