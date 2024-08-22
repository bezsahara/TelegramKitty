package org.bezsahara.kittybot.telegram.classes.messages.origins


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The message was originally sent by an unknown user.
 * 
 * *[link](https://core.telegram.org/bots/api#messageoriginhiddenuser)*: https://core.telegram.org/bots/api#messageoriginhiddenuser
 * 
 * @param type Type of the message origin, always "hidden_user"
 * @param date Date the message was sent originally in Unix time
 * @param senderUserName Name of the user that sent the message originally
 */
@Serializable
data class MessageOriginHiddenUser(
    val date: Long,
    @SerialName("sender_user_name") val senderUserName: String,
) : MessageOrigin {
    override val type: String = "hidden_user"
}

