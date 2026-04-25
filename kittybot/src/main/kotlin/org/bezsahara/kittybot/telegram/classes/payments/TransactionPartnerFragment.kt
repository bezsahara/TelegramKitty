package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalState
import org.bezsahara.kittybot.telegram.values.TransactionPartnerType
import kotlinx.serialization.Serializable


/**
 * Describes a withdrawal transaction with Fragment.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnerfragment): https://core.telegram.org/bots/api#transactionpartnerfragment
 * 
 * @param type Type of the transaction partner, always "fragment"
 * @param withdrawalState Optional. State of the transaction if the transaction is outgoing
 */
@Serializable
data class TransactionPartnerFragment(
    @SerialName("withdrawal_state") val withdrawalState: RevenueWithdrawalState? = null
) : TransactionPartner {
    override val type: TransactionPartnerType = TransactionPartnerType.FRAGMENT
}

