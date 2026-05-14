package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object defines the parameters for the creation of a managed bot. Information about the created bot will be shared with the bot using the update managed_bot and a Message with the field managed_bot_created.
 * 
 * [link](https://core.telegram.org/bots/api#keyboardbuttonrequestmanagedbot): https://core.telegram.org/bots/api#keyboardbuttonrequestmanagedbot
 * 
 * @param requestId Signed 32-bit identifier of the request. Must be unique within the message.
 * @param suggestedName Optional. Suggested name for the bot
 * @param suggestedUsername Optional. Suggested username for the bot
 */
@Serializable
data class KeyboardButtonRequestManagedBot(
    @SerialName("request_id") val requestId: Long,
    @SerialName("suggested_name") val suggestedName: String? = null,
    @SerialName("suggested_username") val suggestedUsername: String? = null
)

