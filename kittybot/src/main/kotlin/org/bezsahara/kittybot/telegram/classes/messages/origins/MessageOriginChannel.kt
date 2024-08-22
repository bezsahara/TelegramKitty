package org.bezsahara.kittybot.telegram.classes.messages.origins


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat


/**
 * The message was originally sent to a channel chat.
 * 
 * *[link](https://core.telegram.org/bots/api#messageoriginchannel)*: https://core.telegram.org/bots/api#messageoriginchannel
 * 
 * @param type Type of the message origin, always "channel"
 * @param date Date the message was sent originally in Unix time
 * @param chat Channel chat to which the message was originally sent
 * @param messageId Unique message identifier inside the chat
 * @param authorSignature Optional. Signature of the original post author
 */
@Serializable
data class MessageOriginChannel(
    val date: Long,
    val chat: Chat,
    @SerialName("message_id") val messageId: Long,
    @SerialName("author_signature") val authorSignature: String? = null,
) : MessageOrigin {
    override val type: String = "channel"
}

