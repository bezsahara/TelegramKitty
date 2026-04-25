package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.TransactionPartnerType
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerOther
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.Serializable


/**
 * Describes a transaction with an unknown source or recipient.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnerother): https://core.telegram.org/bots/api#transactionpartnerother
 * 
 * @param type Type of the transaction partner, always "other"
 */
@Serializable(with = TransactionPartnerOtherJsonSerializer::class)
object TransactionPartnerOther : TransactionPartner {
    override val type: TransactionPartnerType = TransactionPartnerType.OTHER
}


internal class TransactionPartnerOtherJsonSerializer : PureJsonSerializer<TransactionPartnerOther>("TransactionPartnerOther", TransactionPartnerOther, buildJsonObject { put("type", JsonPrimitive("other")) })



