package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type InlineQuery.chat_type
 */
@Serializable(with = InlineQueryChatTypeSerializer::class)
class InlineQueryChatType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InlineQueryChatType.Known>() {
    companion object : ResolveEnumLike<InlineQueryChatType>() {
        @JvmField
        val SENDER = InlineQueryChatType("sender", Known.SENDER)

        @JvmField
        val PRIVATE = InlineQueryChatType("private", Known.PRIVATE)

        @JvmField
        val GROUP = InlineQueryChatType("group", Known.GROUP)

        @JvmField
        val SUPERGROUP = InlineQueryChatType("supergroup", Known.SUPERGROUP)

        @JvmField
        val CHANNEL = InlineQueryChatType("channel", Known.CHANNEL)

        override fun resolve(value: String): InlineQueryChatType {
            return when (value) {
                "sender" -> SENDER
                "private" -> PRIVATE
                "group" -> GROUP
                "supergroup" -> SUPERGROUP
                "channel" -> CHANNEL
                else -> InlineQueryChatType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        SENDER,
        PRIVATE,
        GROUP,
        SUPERGROUP,
        CHANNEL;

        fun toInlineQueryChatType(): InlineQueryChatType {
            return when (this) {
                SENDER -> InlineQueryChatType.SENDER
                PRIVATE -> InlineQueryChatType.PRIVATE
                GROUP -> InlineQueryChatType.GROUP
                SUPERGROUP -> InlineQueryChatType.SUPERGROUP
                CHANNEL -> InlineQueryChatType.CHANNEL
            }
        }
    }

    override fun toString(): String {
        return "InlineQueryChatType($value)"
    }
}

internal object InlineQueryChatTypeSerializer : EnumLikeJsonSerializer<InlineQueryChatType>("InlineQueryChatType", InlineQueryChatType)

