package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type MessageEntity.type
 */
@Serializable(with = MsgEntityTypeSerializer::class)
class MsgEntityType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<MsgEntityType.Known>() {
    companion object : ResolveEnumLikeBig<MsgEntityType, Known>(Known::class.java) {
        @JvmField
        val MENTION = MsgEntityType("mention", Known.MENTION).register()

        @JvmField
        val HASHTAG = MsgEntityType("hashtag", Known.HASHTAG).register()

        @JvmField
        val CASHTAG = MsgEntityType("cashtag", Known.CASHTAG).register()

        @JvmField
        val BOT_COMMAND = MsgEntityType("bot_command", Known.BOT_COMMAND).register()

        @JvmField
        val URL = MsgEntityType("url", Known.URL).register()

        @JvmField
        val EMAIL = MsgEntityType("email", Known.EMAIL).register()

        @JvmField
        val PHONE_NUMBER = MsgEntityType("phone_number", Known.PHONE_NUMBER).register()

        @JvmField
        val BOLD = MsgEntityType("bold", Known.BOLD).register()

        @JvmField
        val ITALIC = MsgEntityType("italic", Known.ITALIC).register()

        @JvmField
        val UNDERLINE = MsgEntityType("underline", Known.UNDERLINE).register()

        @JvmField
        val STRIKETHROUGH = MsgEntityType("strikethrough", Known.STRIKETHROUGH).register()

        @JvmField
        val SPOILER = MsgEntityType("spoiler", Known.SPOILER).register()

        @JvmField
        val BLOCKQUOTE = MsgEntityType("blockquote", Known.BLOCKQUOTE).register()

        @JvmField
        val EXPANDABLE_BLOCKQUOTE = MsgEntityType("expandable_blockquote", Known.EXPANDABLE_BLOCKQUOTE).register()

        @JvmField
        val CODE = MsgEntityType("code", Known.CODE).register()

        @JvmField
        val PRE = MsgEntityType("pre", Known.PRE).register()

        @JvmField
        val TEXT_LINK = MsgEntityType("text_link", Known.TEXT_LINK).register()

        @JvmField
        val TEXT_MENTION = MsgEntityType("text_mention", Known.TEXT_MENTION).register()

        @JvmField
        val CUSTOM_EMOJI = MsgEntityType("custom_emoji", Known.CUSTOM_EMOJI).register()

        @JvmField
        val DATE_TIME = MsgEntityType("date_time", Known.DATE_TIME).register()

        override fun create(value: String): MsgEntityType {
            return MsgEntityType(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        MENTION,
        HASHTAG,
        CASHTAG,
        BOT_COMMAND,
        URL,
        EMAIL,
        PHONE_NUMBER,
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        SPOILER,
        BLOCKQUOTE,
        EXPANDABLE_BLOCKQUOTE,
        CODE,
        PRE,
        TEXT_LINK,
        TEXT_MENTION,
        CUSTOM_EMOJI,
        DATE_TIME;

        fun toMsgEntityType(): MsgEntityType {
            return MsgEntityType.mapGet(this)
        }
    }

    override fun toString(): String {
        return "MsgEntityType($value)"
    }
}

internal object MsgEntityTypeSerializer : EnumLikeJsonSerializer<MsgEntityType>("MsgEntityType", MsgEntityType)

