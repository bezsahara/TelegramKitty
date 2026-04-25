package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalState
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.RevenueWithdrawalStateType
import kotlinx.serialization.Serializable


/**
 * The withdrawal succeeded.
 * 
 * [link](https://core.telegram.org/bots/api#revenuewithdrawalstatesucceeded): https://core.telegram.org/bots/api#revenuewithdrawalstatesucceeded
 * 
 * @param type Type of the state, always "succeeded"
 * @param date Date the withdrawal was completed in Unix time
 * @param url An HTTPS URL that can be used to see transaction details
 */
@Serializable
data class RevenueWithdrawalStateSucceeded(
    val date: Long,
    val url: String
) : RevenueWithdrawalState {
    override val type: RevenueWithdrawalStateType = RevenueWithdrawalStateType.SUCCEEDED
}

