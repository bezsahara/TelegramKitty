package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a transaction with payment for paid broadcasting.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnertelegramapi): https://core.telegram.org/bots/api#transactionpartnertelegramapi
 * 
 * @param type Type of the transaction partner, always "telegram_api"
 * @param requestCount The number of successful requests that exceeded regular limits and were therefore billed
 */
@Serializable
data class TransactionPartnerTelegramApi(
    @SerialName("request_count") val requestCount: Long
) : TransactionPartner {
    override val type: String = "telegram_api"
}

