package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes an inline message sent by a guest bot.
 * 
 * [link](https://core.telegram.org/bots/api#sentguestmessage): https://core.telegram.org/bots/api#sentguestmessage
 * 
 * @param inlineMessageId Identifier of the sent inline message
 */
@Serializable
data class SentGuestMessage(
    @SerialName("inline_message_id") val inlineMessageId: String
)

