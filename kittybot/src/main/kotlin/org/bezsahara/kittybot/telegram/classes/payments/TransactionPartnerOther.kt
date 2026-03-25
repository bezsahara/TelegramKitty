package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a transaction with an unknown source or recipient.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnerother): https://core.telegram.org/bots/api#transactionpartnerother
 * 
 * @param type Type of the transaction partner, always "other"
 */
@Serializable
open class TransactionPartnerOther : TransactionPartner {
    override val type: String get() = "other"
    companion object Default : TransactionPartnerOther()
}

