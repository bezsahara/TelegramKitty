package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.gifts.Gift
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.values.TransactionType


/**
 * Describes a transaction with a user.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartneruser): https://core.telegram.org/bots/api#transactionpartneruser
 * 
 * @param type Type of the transaction partner, always "user"
 * @param transactionType Type of the transaction, currently one of "invoice_payment" for payments via invoices, "paid_media_payment" for payments for paid media, "gift_purchase" for gifts sent by the bot, "premium_purchase" for Telegram Premium subscriptions gifted by the bot, "business_account_transfer" for direct transfers from managed business accounts
 * @param user Information about the user
 * @param affiliate Optional. Information about the affiliate that received a commission via this transaction. Can be available only for "invoice_payment" and "paid_media_payment" transactions.
 * @param invoicePayload Optional. Bot-specified invoice payload. Can be available only for "invoice_payment" transactions.
 * @param subscriptionPeriod Optional. The duration of the paid subscription. Can be available only for "invoice_payment" transactions.
 * @param paidMedia Optional. Information about the paid media bought by the user; for "paid_media_payment" transactions only
 * @param paidMediaPayload Optional. Bot-specified paid media payload. Can be available only for "paid_media_payment" transactions.
 * @param gift Optional. The gift sent to the user by the bot; for "gift_purchase" transactions only
 * @param premiumSubscriptionDuration Optional. Number of months the gifted Telegram Premium subscription will be active for; for "premium_purchase" transactions only
 */
@Serializable
data class TransactionPartnerUser(
    @SerialName("transaction_type") val transactionType: TransactionType,
    val user: User,
    val affiliate: AffiliateInfo? = null,
    @SerialName("invoice_payload") val invoicePayload: String? = null,
    @SerialName("subscription_period") val subscriptionPeriod: Long? = null,
    @SerialName("paid_media") val paidMedia: List<PaidMedia>? = null,
    @SerialName("paid_media_payload") val paidMediaPayload: String? = null,
    val gift: Gift? = null,
    @SerialName("premium_subscription_duration") val premiumSubscriptionDuration: Long? = null
) : TransactionPartner {
    override val type: String = "user"
}

