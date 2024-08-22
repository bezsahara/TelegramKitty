package org.bezsahara.kittybot.telegram.classes.messages.origins


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * The message was originally sent by a known user.
 * 
 * *[link](https://core.telegram.org/bots/api#messageoriginuser)*: https://core.telegram.org/bots/api#messageoriginuser
 * 
 * @param type Type of the message origin, always "user"
 * @param date Date the message was sent originally in Unix time
 * @param senderUser User that sent the message originally
 */
@Serializable
data class MessageOriginUser(
    val date: Long,
    @SerialName("sender_user") val senderUser: User,
) : MessageOrigin {
    override val type: String = "user"
}

