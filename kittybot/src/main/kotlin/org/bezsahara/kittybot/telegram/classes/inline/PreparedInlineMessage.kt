package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes an inline message to be sent by a user of a Mini App.
 * 
 * [link](https://core.telegram.org/bots/api#preparedinlinemessage): https://core.telegram.org/bots/api#preparedinlinemessage
 * 
 * @param id Unique identifier of the prepared message
 * @param expirationDate Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used.
 */
@Serializable
data class PreparedInlineMessage(
    val id: String,
    @SerialName("expiration_date") val expirationDate: Long
)

