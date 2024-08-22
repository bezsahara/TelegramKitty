package org.bezsahara.kittybot.telegram.classes.messages.inaccessible


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat


/**
 * This object describes a message that was deleted or is otherwise inaccessible to the bot.
 * 
 * *[link](https://core.telegram.org/bots/api#inaccessiblemessage)*: https://core.telegram.org/bots/api#inaccessiblemessage
 * 
 * @param chat Chat the message belonged to
 * @param messageId Unique message identifier inside the chat
 * @param date Always 0. The field can be used to differentiate regular and inaccessible messages.
 */
@Serializable
data class InaccessibleMessage(
    val chat: Chat,
    @SerialName("message_id") val messageId: Long,
    val date: Long
) : MaybeInaccessibleMessage

