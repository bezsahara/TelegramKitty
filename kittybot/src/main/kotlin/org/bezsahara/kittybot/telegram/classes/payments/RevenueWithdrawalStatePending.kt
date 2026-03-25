package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The withdrawal is in progress.
 * 
 * [link](https://core.telegram.org/bots/api#revenuewithdrawalstatepending): https://core.telegram.org/bots/api#revenuewithdrawalstatepending
 * 
 * @param type Type of the state, always "pending"
 */
@Serializable
open class RevenueWithdrawalStatePending : RevenueWithdrawalState {
    override val type: String get() = "pending"
    companion object Default : RevenueWithdrawalStatePending()
}

