package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a service message about an option added to a poll.
 * 
 * [link](https://core.telegram.org/bots/api#polloptionadded): https://core.telegram.org/bots/api#polloptionadded
 * 
 * @param pollMessage Optional. Message containing the poll to which the option was added, if known. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param optionPersistentId Unique identifier of the added option
 * @param optionText Option text
 * @param optionTextEntities Optional. Special entities that appear in the option_text
 */
@Serializable
data class PollOptionAdded(
    @SerialName("option_persistent_id") val optionPersistentId: String,
    @SerialName("option_text") val optionText: String,
    @SerialName("poll_message") val pollMessage: MaybeInaccessibleMessage? = null,
    @SerialName("option_text_entities") val optionTextEntities: List<MessageEntity>? = null
)

