package org.bezsahara.kittybot.telegram.classes.business


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat


/**
 * This object is received when messages are deleted from a connected business account.
 * 
 * *[link](https://core.telegram.org/bots/api#businessmessagesdeleted)*: https://core.telegram.org/bots/api#businessmessagesdeleted
 * 
 * @param businessConnectionId Unique identifier of the business connection
 * @param chat Information about a chat in the business account. The bot may not have access to the chat or the corresponding user.
 * @param messageIds The list of identifiers of deleted messages in the chat of the business account
 */
@Serializable
data class BusinessMessagesDeleted(
    @SerialName("business_connection_id") val businessConnectionId: String,
    val chat: Chat,
    @SerialName("message_ids") val messageIds: List<Long>
)

