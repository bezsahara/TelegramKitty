package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a service message about an option deleted from a poll.
 * 
 * [link](https://core.telegram.org/bots/api#polloptiondeleted): https://core.telegram.org/bots/api#polloptiondeleted
 * 
 * @param pollMessage Optional. Message containing the poll from which the option was deleted, if known. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param optionPersistentId Unique identifier of the deleted option
 * @param optionText Option text
 * @param optionTextEntities Optional. Special entities that appear in the option_text
 */
@Serializable
data class PollOptionDeleted(
    @SerialName("option_persistent_id") val optionPersistentId: String,
    @SerialName("option_text") val optionText: String,
    @SerialName("poll_message") val pollMessage: MaybeInaccessibleMessage? = null,
    @SerialName("option_text_entities") val optionTextEntities: List<MessageEntity>? = null
)

