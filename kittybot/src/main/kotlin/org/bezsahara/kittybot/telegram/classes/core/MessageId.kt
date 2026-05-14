package org.bezsahara.kittybot.telegram.classes.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a unique message identifier.
 * 
 * [link](https://core.telegram.org/bots/api#messageid): https://core.telegram.org/bots/api#messageid
 * 
 * @param messageId Unique message identifier. In specific instances (e.g., message containing a video sent to a big chat), the server might automatically schedule a message instead of sending it immediately. In such cases, this field will be 0 and the relevant message will be unusable until it is actually sent.
 */
@Serializable
data class MessageId(
    @SerialName("message_id") val messageId: Long
)

