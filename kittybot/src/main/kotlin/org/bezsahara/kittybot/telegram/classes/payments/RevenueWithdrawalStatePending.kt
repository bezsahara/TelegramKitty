package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * The withdrawal is in progress.
 * 
 * [link](https://core.telegram.org/bots/api#revenuewithdrawalstatepending): https://core.telegram.org/bots/api#revenuewithdrawalstatepending
 * 
 * @param type Type of the state, always "pending"
 */
@Serializable(with = RevenueWithdrawalStatePendingJsonSerializer::class)
object RevenueWithdrawalStatePending : RevenueWithdrawalState {
    override val type: String = "pending"
}


internal class RevenueWithdrawalStatePendingJsonSerializer : PureJsonSerializer<RevenueWithdrawalStatePending>("RevenueWithdrawalStatePending", RevenueWithdrawalStatePending, buildJsonObject { put("type", JsonPrimitive("pending")) })



