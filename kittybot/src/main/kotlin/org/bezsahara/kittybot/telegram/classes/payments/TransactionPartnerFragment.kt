package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
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
    override val type: String = "fragment"
}

