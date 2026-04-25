package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype ReactionType.type
 * - type ReactionTypeEmoji.type
 * - type ReactionTypeCustomEmoji.type
 * - type ReactionTypePaid.type
 */
@Serializable(with = ReactionTypeKindSerializer::class)
class ReactionTypeKind internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ReactionTypeKind.Known>() {
    companion object : ResolveEnumLike<ReactionTypeKind>() {
        @JvmField
        val EMOJI = ReactionTypeKind("emoji", Known.EMOJI)

        @JvmField
        val CUSTOM_EMOJI = ReactionTypeKind("custom_emoji", Known.CUSTOM_EMOJI)

        @JvmField
        val PAID = ReactionTypeKind("paid", Known.PAID)

        override fun resolve(value: String): ReactionTypeKind {
            return when (value) {
                "emoji" -> EMOJI
                "custom_emoji" -> CUSTOM_EMOJI
                "paid" -> PAID
                else -> ReactionTypeKind(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        EMOJI,
        CUSTOM_EMOJI,
        PAID;

        fun toReactionTypeKind(): ReactionTypeKind {
            return when (this) {
                EMOJI -> ReactionTypeKind.EMOJI
                CUSTOM_EMOJI -> ReactionTypeKind.CUSTOM_EMOJI
                PAID -> ReactionTypeKind.PAID
            }
        }
    }

    override fun toString(): String {
        return "ReactionTypeKind($value)"
    }
}

internal object ReactionTypeKindSerializer : EnumLikeJsonSerializer<ReactionTypeKind>("ReactionTypeKind", ReactionTypeKind)

