package org.bezsahara.kittybot.telegram.classes.polls


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity


/**
 * This object contains information about one answer option in a poll.
 * 
 * *[link](https://core.telegram.org/bots/api#polloption)*: https://core.telegram.org/bots/api#polloption
 * 
 * @param text Option text, 1-100 characters
 * @param textEntities Optional. Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts
 * @param voterCount Number of users that voted for this option
 */
@Serializable
data class PollOption(
    val text: String,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
    @SerialName("voter_count") val voterCount: Long
)

