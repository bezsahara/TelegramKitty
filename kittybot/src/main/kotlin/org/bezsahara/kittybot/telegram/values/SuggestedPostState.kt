package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type SuggestedPostInfo.state
 */
@Serializable(with = SuggestedPostStateSerializer::class)
class SuggestedPostState internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<SuggestedPostState.Known>() {
    companion object : ResolveEnumLike<SuggestedPostState>() {
        @JvmField
        val PENDING = SuggestedPostState("pending", Known.PENDING)

        @JvmField
        val APPROVED = SuggestedPostState("approved", Known.APPROVED)

        @JvmField
        val DECLINED = SuggestedPostState("declined", Known.DECLINED)

        override fun resolve(value: String): SuggestedPostState {
            return when (value) {
                "pending" -> PENDING
                "approved" -> APPROVED
                "declined" -> DECLINED
                else -> SuggestedPostState(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PENDING,
        APPROVED,
        DECLINED;

        fun toSuggestedPostState(): SuggestedPostState {
            return when (this) {
                PENDING -> SuggestedPostState.PENDING
                APPROVED -> SuggestedPostState.APPROVED
                DECLINED -> SuggestedPostState.DECLINED
            }
        }
    }

    override fun toString(): String {
        return "SuggestedPostState($value)"
    }
}

internal object SuggestedPostStateSerializer : EnumLikeJsonSerializer<SuggestedPostState>("SuggestedPostState", SuggestedPostState)

