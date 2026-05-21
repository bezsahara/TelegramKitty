package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type Chat.type
 * - type ChatFullInfo.type
 */
@Serializable(with = ChatTypeSerializer::class)
class ChatType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ChatType.Known>() {
    companion object : ResolveEnumLike<ChatType>() {
        @JvmField
        val PRIVATE = ChatType("private", Known.PRIVATE)

        @JvmField
        val GROUP = ChatType("group", Known.GROUP)

        @JvmField
        val SUPERGROUP = ChatType("supergroup", Known.SUPERGROUP)

        @JvmField
        val CHANNEL = ChatType("channel", Known.CHANNEL)

        override fun resolve(value: String): ChatType {
            return when (value) {
                "private" -> PRIVATE
                "group" -> GROUP
                "supergroup" -> SUPERGROUP
                "channel" -> CHANNEL
                else -> ChatType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PRIVATE,
        GROUP,
        SUPERGROUP,
        CHANNEL;

        fun toChatType(): ChatType {
            return when (this) {
                PRIVATE -> ChatType.PRIVATE
                GROUP -> ChatType.GROUP
                SUPERGROUP -> ChatType.SUPERGROUP
                CHANNEL -> ChatType.CHANNEL
            }
        }
    }

    override fun toString(): String {
        return "ChatType($value)"
    }
}

internal object ChatTypeSerializer : EnumLikeJsonSerializer<ChatType>("ChatType", ChatType)

