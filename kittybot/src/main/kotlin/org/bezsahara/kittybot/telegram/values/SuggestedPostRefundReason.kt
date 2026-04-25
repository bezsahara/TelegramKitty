package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type SuggestedPostRefunded.reason
 */
@Serializable(with = SuggestedPostRefundReasonSerializer::class)
class SuggestedPostRefundReason internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<SuggestedPostRefundReason.Known>() {
    companion object : ResolveEnumLike<SuggestedPostRefundReason>() {
        @JvmField
        val POST_DELETED = SuggestedPostRefundReason("post_deleted", Known.POST_DELETED)

        @JvmField
        val PAYMENT_REFUNDED = SuggestedPostRefundReason("payment_refunded", Known.PAYMENT_REFUNDED)

        override fun resolve(value: String): SuggestedPostRefundReason {
            return when (value) {
                "post_deleted" -> POST_DELETED
                "payment_refunded" -> PAYMENT_REFUNDED
                else -> SuggestedPostRefundReason(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        POST_DELETED,
        PAYMENT_REFUNDED;

        fun toSuggestedPostRefundReason(): SuggestedPostRefundReason {
            return when (this) {
                POST_DELETED -> SuggestedPostRefundReason.POST_DELETED
                PAYMENT_REFUNDED -> SuggestedPostRefundReason.PAYMENT_REFUNDED
            }
        }
    }

    override fun toString(): String {
        return "SuggestedPostRefundReason($value)"
    }
}

internal object SuggestedPostRefundReasonSerializer : EnumLikeJsonSerializer<SuggestedPostRefundReason>("SuggestedPostRefundReason", SuggestedPostRefundReason)

