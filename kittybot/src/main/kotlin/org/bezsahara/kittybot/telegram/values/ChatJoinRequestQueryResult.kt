package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - method answerChatJoinRequestQuery.result
 */
@Serializable(with = ChatJoinRequestQueryResultSerializer::class)
class ChatJoinRequestQueryResult internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ChatJoinRequestQueryResult.Known>() {
    companion object : ResolveEnumLike<ChatJoinRequestQueryResult>() {
        @JvmField
        val APPROVE = ChatJoinRequestQueryResult("approve", Known.APPROVE)

        @JvmField
        val DECLINE = ChatJoinRequestQueryResult("decline", Known.DECLINE)

        @JvmField
        val QUEUE = ChatJoinRequestQueryResult("queue", Known.QUEUE)

        override fun resolve(value: String): ChatJoinRequestQueryResult {
            return when (value) {
                "approve" -> APPROVE
                "decline" -> DECLINE
                "queue" -> QUEUE
                else -> ChatJoinRequestQueryResult(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        APPROVE,
        DECLINE,
        QUEUE;

        fun toChatJoinRequestQueryResult(): ChatJoinRequestQueryResult {
            return when (this) {
                APPROVE -> ChatJoinRequestQueryResult.APPROVE
                DECLINE -> ChatJoinRequestQueryResult.DECLINE
                QUEUE -> ChatJoinRequestQueryResult.QUEUE
            }
        }
    }

    override fun toString(): String {
        return "ChatJoinRequestQueryResult($value)"
    }
}

internal object ChatJoinRequestQueryResultSerializer : EnumLikeJsonSerializer<ChatJoinRequestQueryResult>("ChatJoinRequestQueryResult", ChatJoinRequestQueryResult)
