package org.bezsahara.kittybot.telegram.classes.giveaways


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message


/**
 * This object represents a service message about the completion of a giveaway without public winners.
 * 
 * *[link](https://core.telegram.org/bots/api#giveawaycompleted)*: https://core.telegram.org/bots/api#giveawaycompleted
 * 
 * @param winnerCount Number of winners in the giveaway
 * @param unclaimedPrizeCount Optional. Number of undistributed prizes
 * @param giveawayMessage Optional. Message with the giveaway that was completed, if it wasn't deleted
 */
@Serializable
data class GiveawayCompleted(
    @SerialName("winner_count") val winnerCount: Long,
    @SerialName("unclaimed_prize_count") val unclaimedPrizeCount: Long? = null,
    @SerialName("giveaway_message") val giveawayMessage: Message? = null
)

