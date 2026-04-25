package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype RevenueWithdrawalState.type
 * - type RevenueWithdrawalStatePending.type
 * - type RevenueWithdrawalStateSucceeded.type
 * - type RevenueWithdrawalStateFailed.type
 */
@Serializable(with = RevenueWithdrawalStateTypeSerializer::class)
class RevenueWithdrawalStateType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<RevenueWithdrawalStateType.Known>() {
    companion object : ResolveEnumLike<RevenueWithdrawalStateType>() {
        @JvmField
        val PENDING = RevenueWithdrawalStateType("pending", Known.PENDING)

        @JvmField
        val SUCCEEDED = RevenueWithdrawalStateType("succeeded", Known.SUCCEEDED)

        @JvmField
        val FAILED = RevenueWithdrawalStateType("failed", Known.FAILED)

        override fun resolve(value: String): RevenueWithdrawalStateType {
            return when (value) {
                "pending" -> PENDING
                "succeeded" -> SUCCEEDED
                "failed" -> FAILED
                else -> RevenueWithdrawalStateType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PENDING,
        SUCCEEDED,
        FAILED;

        fun toRevenueWithdrawalStateType(): RevenueWithdrawalStateType {
            return when (this) {
                PENDING -> RevenueWithdrawalStateType.PENDING
                SUCCEEDED -> RevenueWithdrawalStateType.SUCCEEDED
                FAILED -> RevenueWithdrawalStateType.FAILED
            }
        }
    }

    override fun toString(): String {
        return "RevenueWithdrawalStateType($value)"
    }
}

internal object RevenueWithdrawalStateTypeSerializer : EnumLikeJsonSerializer<RevenueWithdrawalStateType>("RevenueWithdrawalStateType", RevenueWithdrawalStateType)

