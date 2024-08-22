package org.bezsahara.kittybot.telegram.classes.webapps.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes an inline message sent by a Web App on behalf of a user.
 * 
 * *[link](https://core.telegram.org/bots/api#sentwebappmessage)*: https://core.telegram.org/bots/api#sentwebappmessage
 * 
 * @param inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
 */
@Serializable
data class SentWebAppMessage(
    @SerialName("inline_message_id") val inlineMessageId: String? = null
)

