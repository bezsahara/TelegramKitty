package org.bezsahara.kittybot.telegram.classes.messages.input


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents the content of a contact message to be sent as the result of an inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#inputcontactmessagecontent)*: https://core.telegram.org/bots/api#inputcontactmessagecontent
 * 
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Optional. Contact's last name
 * @param vcard Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes
 */
@Serializable
data class InputContactMessageContent(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    val vcard: String? = null
) : InputMessageContent

