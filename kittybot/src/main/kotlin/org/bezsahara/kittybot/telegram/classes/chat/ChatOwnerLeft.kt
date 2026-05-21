package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Describes a service message about the chat owner leaving the chat.
 * 
 * [link](https://core.telegram.org/bots/api#chatownerleft): https://core.telegram.org/bots/api#chatownerleft
 * 
 * @param newOwner Optional. The user who will become the new owner of the chat if the previous owner does not return to the chat
 */
@Serializable
data class ChatOwnerLeft(
    @SerialName("new_owner") val newOwner: User? = null
)

