package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a service message about a change in the price of paid messages within a chat.
 * 
 * [link](https://core.telegram.org/bots/api#paidmessagepricechanged): https://core.telegram.org/bots/api#paidmessagepricechanged
 * 
 * @param paidMessageStarCount The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message
 */
@Serializable
data class PaidMessagePriceChanged(
    @SerialName("paid_message_star_count") val paidMessageStarCount: Long
)

