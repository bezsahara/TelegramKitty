package org.bezsahara.kittybot.telegram.classes.chat.boosts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium giveaways and prize_star_count / 500 times for one year for Telegram Star giveaways.
 * 
 * [link](https://core.telegram.org/bots/api#chatboostsourcegiveaway): https://core.telegram.org/bots/api#chatboostsourcegiveaway
 * 
 * @param source Source of the boost, always "giveaway"
 * @param giveawayMessageId Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet.
 * @param user Optional. User that won the prize in the giveaway if any; for Telegram Premium giveaways only
 * @param prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 * @param isUnclaimed Optional. True, if the giveaway was completed, but there was no user to win the prize
 */
@Serializable
data class ChatBoostSourceGiveaway(
    @SerialName("giveaway_message_id") val giveawayMessageId: Long,
    val user: User? = null,
    @SerialName("prize_star_count") val prizeStarCount: Long? = null,
    @SerialName("is_unclaimed") val isUnclaimed: Boolean? = null
) : ChatBoostSource {
    override val source: String = "giveaway"
}

