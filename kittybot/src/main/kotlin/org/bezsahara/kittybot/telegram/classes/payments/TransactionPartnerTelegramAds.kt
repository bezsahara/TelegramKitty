package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnertelegramads): https://core.telegram.org/bots/api#transactionpartnertelegramads
 * 
 * @param type Type of the transaction partner, always "telegram_ads"
 */
@Serializable
open class TransactionPartnerTelegramAds : TransactionPartner {
    override val type: String get() = "telegram_ads"
    companion object Default : TransactionPartnerTelegramAds()
}

