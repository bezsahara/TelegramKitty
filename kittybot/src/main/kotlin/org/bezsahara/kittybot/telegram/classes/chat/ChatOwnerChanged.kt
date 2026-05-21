package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Describes a service message about an ownership change in the chat.
 * 
 * [link](https://core.telegram.org/bots/api#chatownerchanged): https://core.telegram.org/bots/api#chatownerchanged
 * 
 * @param newOwner The new owner of the chat
 */
@Serializable
data class ChatOwnerChanged(
    @SerialName("new_owner") val newOwner: User
)

