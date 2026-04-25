package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type TransactionPartnerUser.transaction_type
 */
@Serializable(with = TransactionTypeSerializer::class)
class TransactionType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<TransactionType.Known>() {
    companion object : ResolveEnumLike<TransactionType>() {
        @JvmField
        val INVOICE_PAYMENT = TransactionType("invoice_payment", Known.INVOICE_PAYMENT)

        @JvmField
        val PAID_MEDIA_PAYMENT = TransactionType("paid_media_payment", Known.PAID_MEDIA_PAYMENT)

        @JvmField
        val GIFT_PURCHASE = TransactionType("gift_purchase", Known.GIFT_PURCHASE)

        @JvmField
        val PREMIUM_PURCHASE = TransactionType("premium_purchase", Known.PREMIUM_PURCHASE)

        @JvmField
        val BUSINESS_ACCOUNT_TRANSFER = TransactionType("business_account_transfer", Known.BUSINESS_ACCOUNT_TRANSFER)

        override fun resolve(value: String): TransactionType {
            return when (value) {
                "invoice_payment" -> INVOICE_PAYMENT
                "paid_media_payment" -> PAID_MEDIA_PAYMENT
                "gift_purchase" -> GIFT_PURCHASE
                "premium_purchase" -> PREMIUM_PURCHASE
                "business_account_transfer" -> BUSINESS_ACCOUNT_TRANSFER
                else -> TransactionType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        INVOICE_PAYMENT,
        PAID_MEDIA_PAYMENT,
        GIFT_PURCHASE,
        PREMIUM_PURCHASE,
        BUSINESS_ACCOUNT_TRANSFER;

        fun toTransactionType(): TransactionType {
            return when (this) {
                INVOICE_PAYMENT -> TransactionType.INVOICE_PAYMENT
                PAID_MEDIA_PAYMENT -> TransactionType.PAID_MEDIA_PAYMENT
                GIFT_PURCHASE -> TransactionType.GIFT_PURCHASE
                PREMIUM_PURCHASE -> TransactionType.PREMIUM_PURCHASE
                BUSINESS_ACCOUNT_TRANSFER -> TransactionType.BUSINESS_ACCOUNT_TRANSFER
            }
        }
    }

    override fun toString(): String {
        return "TransactionType($value)"
    }
}

internal object TransactionTypeSerializer : EnumLikeJsonSerializer<TransactionType>("TransactionType", TransactionType)

