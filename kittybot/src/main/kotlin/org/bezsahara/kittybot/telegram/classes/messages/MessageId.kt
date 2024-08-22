package org.bezsahara.kittybot.telegram.classes.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a unique message identifier.
 * 
 * *[link](https://core.telegram.org/bots/api#messageid)*: https://core.telegram.org/bots/api#messageid
 * 
 * @param messageId Unique message identifier
 */
@Serializable
data class MessageId(
    @SerialName("message_id") val messageId: Long
)

