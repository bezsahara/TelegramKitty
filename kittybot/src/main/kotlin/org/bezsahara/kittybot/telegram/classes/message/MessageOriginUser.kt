package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.classes.message.MessageOrigin
import kotlinx.serialization.Serializable


/**
 * The message was originally sent by a known user.
 * 
 * [link](https://core.telegram.org/bots/api#messageoriginuser): https://core.telegram.org/bots/api#messageoriginuser
 * 
 * @param type Type of the message origin, always "user"
 * @param date Date the message was sent originally in Unix time
 * @param senderUser User that sent the message originally
 */
@Serializable
data class MessageOriginUser(
    val date: Long,
    @SerialName("sender_user") val senderUser: User
) : MessageOrigin {
    override val type: String get() = "user"
}

