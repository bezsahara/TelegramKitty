package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.Message
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about the completion of a giveaway without public winners.
 * 
 * [link](https://core.telegram.org/bots/api#giveawaycompleted): https://core.telegram.org/bots/api#giveawaycompleted
 * 
 * @param winnerCount Number of winners in the giveaway
 * @param unclaimedPrizeCount Optional. Number of undistributed prizes
 * @param giveawayMessage Optional. Message with the giveaway that was completed, if it wasn't deleted
 * @param isStarGiveaway Optional. True, if the giveaway is a Telegram Star giveaway. Otherwise, currently, the giveaway is a Telegram Premium giveaway.
 */
@Serializable
data class GiveawayCompleted(
    @SerialName("winner_count") val winnerCount: Long,
    @SerialName("unclaimed_prize_count") val unclaimedPrizeCount: Long? = null,
    @SerialName("giveaway_message") val giveawayMessage: Message? = null,
    @SerialName("is_star_giveaway") val isStarGiveaway: Boolean? = null
)

