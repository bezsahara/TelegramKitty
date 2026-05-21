package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnertelegramads): https://core.telegram.org/bots/api#transactionpartnertelegramads
 * 
 * @param type Type of the transaction partner, always "telegram_ads"
 */
@Serializable(with = TransactionPartnerTelegramAdsJsonSerializer::class)
object TransactionPartnerTelegramAds : TransactionPartner {
    override val type: String = "telegram_ads"
}


internal class TransactionPartnerTelegramAdsJsonSerializer : PureJsonSerializer<TransactionPartnerTelegramAds>("TransactionPartnerTelegramAds", TransactionPartnerTelegramAds, buildJsonObject { put("type", JsonPrimitive("telegram_ads")) })



