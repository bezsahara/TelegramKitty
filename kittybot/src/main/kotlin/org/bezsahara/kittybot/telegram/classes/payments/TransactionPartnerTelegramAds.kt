package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.TransactionPartnerType
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerTelegramAds
import kotlinx.serialization.Serializable


/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnertelegramads): https://core.telegram.org/bots/api#transactionpartnertelegramads
 * 
 * @param type Type of the transaction partner, always "telegram_ads"
 */
@Serializable(with = TransactionPartnerTelegramAdsJsonSerializer::class)
object TransactionPartnerTelegramAds : TransactionPartner {
    override val type: TransactionPartnerType = TransactionPartnerType.TELEGRAM_ADS
}


internal class TransactionPartnerTelegramAdsJsonSerializer : PureJsonSerializer<TransactionPartnerTelegramAds>("TransactionPartnerTelegramAds", TransactionPartnerTelegramAds, buildJsonObject { put("type", JsonPrimitive("telegram_ads")) })



