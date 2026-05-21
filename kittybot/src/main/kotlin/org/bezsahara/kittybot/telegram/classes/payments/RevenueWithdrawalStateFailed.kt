package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * The withdrawal failed and the transaction was refunded.
 * 
 * [link](https://core.telegram.org/bots/api#revenuewithdrawalstatefailed): https://core.telegram.org/bots/api#revenuewithdrawalstatefailed
 * 
 * @param type Type of the state, always "failed"
 */
@Serializable(with = RevenueWithdrawalStateFailedJsonSerializer::class)
object RevenueWithdrawalStateFailed : RevenueWithdrawalState {
    override val type: String = "failed"
}


internal class RevenueWithdrawalStateFailedJsonSerializer : PureJsonSerializer<RevenueWithdrawalStateFailed>("RevenueWithdrawalStateFailed", RevenueWithdrawalStateFailed, buildJsonObject { put("type", JsonPrimitive("failed")) })



