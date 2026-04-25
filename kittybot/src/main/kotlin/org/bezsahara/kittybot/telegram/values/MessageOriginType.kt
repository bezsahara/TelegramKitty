package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype MessageOrigin.type
 * - type MessageOriginUser.type
 * - type MessageOriginHiddenUser.type
 * - type MessageOriginChat.type
 * - type MessageOriginChannel.type
 */
@Serializable(with = MessageOriginTypeSerializer::class)
class MessageOriginType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<MessageOriginType.Known>() {
    companion object : ResolveEnumLike<MessageOriginType>() {
        @JvmField
        val USER = MessageOriginType("user", Known.USER)

        @JvmField
        val HIDDEN_USER = MessageOriginType("hidden_user", Known.HIDDEN_USER)

        @JvmField
        val CHAT = MessageOriginType("chat", Known.CHAT)

        @JvmField
        val CHANNEL = MessageOriginType("channel", Known.CHANNEL)

        override fun resolve(value: String): MessageOriginType {
            return when (value) {
                "user" -> USER
                "hidden_user" -> HIDDEN_USER
                "chat" -> CHAT
                "channel" -> CHANNEL
                else -> MessageOriginType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        USER,
        HIDDEN_USER,
        CHAT,
        CHANNEL;

        fun toMessageOriginType(): MessageOriginType {
            return when (this) {
                USER -> MessageOriginType.USER
                HIDDEN_USER -> MessageOriginType.HIDDEN_USER
                CHAT -> MessageOriginType.CHAT
                CHANNEL -> MessageOriginType.CHANNEL
            }
        }
    }

    override fun toString(): String {
        return "MessageOriginType($value)"
    }
}

internal object MessageOriginTypeSerializer : EnumLikeJsonSerializer<MessageOriginType>("MessageOriginType", MessageOriginType)

