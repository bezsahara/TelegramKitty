package org.bezsahara.kittybot.telegram.classes.messages.reactions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.reactions.ReactionType
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents a change of a reaction on a message performed by a user.
 * 
 * *[link](https://core.telegram.org/bots/api#messagereactionupdated)*: https://core.telegram.org/bots/api#messagereactionupdated
 * 
 * @param chat The chat containing the message the user reacted to
 * @param messageId Unique identifier of the message inside the chat
 * @param user Optional. The user that changed the reaction, if the user isn't anonymous
 * @param actorChat Optional. The chat on behalf of which the reaction was changed, if the user is anonymous
 * @param date Date of the change in Unix time
 * @param oldReaction Previous list of reaction types that were set by the user
 * @param newReaction New list of reaction types that have been set by the user
 */
@Serializable
data class MessageReactionUpdated(
    val chat: Chat,
    @SerialName("message_id") val messageId: Long,
    val user: User? = null,
    @SerialName("actor_chat") val actorChat: Chat? = null,
    val date: Long,
    @SerialName("old_reaction") val oldReaction: List<ReactionType>,
    @SerialName("new_reaction") val newReaction: List<ReactionType>
)

