package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a service message about a change in the price of direct messages sent to a channel chat.
 * 
 * [link](https://core.telegram.org/bots/api#directmessagepricechanged): https://core.telegram.org/bots/api#directmessagepricechanged
 * 
 * @param areDirectMessagesEnabled True, if direct messages are enabled for the channel chat; false otherwise
 * @param directMessageStarCount Optional. The new number of Telegram Stars that must be paid by users for each direct message sent to the channel. Does not apply to users who have been exempted by administrators. Defaults to 0.
 */
@Serializable
data class DirectMessagePriceChanged(
    @SerialName("are_direct_messages_enabled") val areDirectMessagesEnabled: Boolean,
    @SerialName("direct_message_star_count") val directMessageStarCount: Long? = null
)

