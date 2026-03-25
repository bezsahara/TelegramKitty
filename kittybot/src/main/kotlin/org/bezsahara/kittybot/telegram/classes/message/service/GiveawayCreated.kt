package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about the creation of a scheduled giveaway.
 * 
 * [link](https://core.telegram.org/bots/api#giveawaycreated): https://core.telegram.org/bots/api#giveawaycreated
 * 
 * @param prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
data class GiveawayCreated(
    @SerialName("prize_star_count") val prizeStarCount: Long? = null
)

