package org.bezsahara.kittybot.telegram.classes.messages.reactions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.reactions.ReactionCount


/**
 * This object represents reaction changes on a message with anonymous reactions.
 * 
 * *[link](https://core.telegram.org/bots/api#messagereactioncountupdated)*: https://core.telegram.org/bots/api#messagereactioncountupdated
 * 
 * @param chat The chat containing the message
 * @param messageId Unique message identifier inside the chat
 * @param date Date of the change in Unix time
 * @param reactions List of reactions that are present on the message
 */
@Serializable
data class MessageReactionCountUpdated(
    val chat: Chat,
    @SerialName("message_id") val messageId: Long,
    val date: Long,
    val reactions: List<ReactionCount>
)

