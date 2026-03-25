package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The withdrawal failed and the transaction was refunded.
 * 
 * [link](https://core.telegram.org/bots/api#revenuewithdrawalstatefailed): https://core.telegram.org/bots/api#revenuewithdrawalstatefailed
 * 
 * @param type Type of the state, always "failed"
 */
@Serializable
open class RevenueWithdrawalStateFailed : RevenueWithdrawalState {
    override val type: String get() = "failed"
    companion object Default : RevenueWithdrawalStateFailed()
}

