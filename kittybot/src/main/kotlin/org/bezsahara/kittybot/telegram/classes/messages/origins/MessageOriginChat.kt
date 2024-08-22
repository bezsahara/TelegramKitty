package org.bezsahara.kittybot.telegram.classes.messages.origins


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat


/**
 * The message was originally sent on behalf of a chat to a group chat.
 * 
 * *[link](https://core.telegram.org/bots/api#messageoriginchat)*: https://core.telegram.org/bots/api#messageoriginchat
 * 
 * @param type Type of the message origin, always "chat"
 * @param date Date the message was sent originally in Unix time
 * @param senderChat Chat that sent the message originally
 * @param authorSignature Optional. For messages originally sent by an anonymous chat administrator, original message author signature
 */
@Serializable
data class MessageOriginChat(
    val date: Long,
    @SerialName("sender_chat") val senderChat: Chat,
    @SerialName("author_signature") val authorSignature: String? = null,
) : MessageOrigin {
    override val type: String = "chat"
}

