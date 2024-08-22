package org.bezsahara.kittybot.telegram.classes.reactions


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * Represents a reaction added to a message along with the number of times it was added.
 * 
 * *[link](https://core.telegram.org/bots/api#reactioncount)*: https://core.telegram.org/bots/api#reactioncount
 * 
 * @param type Type of the reaction
 * @param totalCount Number of times the reaction was added
 */
@Serializable
data class ReactionCount(
    val type: ReactionType,
    @SerialName("total_count") val totalCount: Long
)

