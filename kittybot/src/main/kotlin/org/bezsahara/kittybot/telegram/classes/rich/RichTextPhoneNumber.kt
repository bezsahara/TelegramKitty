package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A text with a phone number.
 *
 * [link](https://core.telegram.org/bots/api#richtextphonenumber): https://core.telegram.org/bots/api#richtextphonenumber
 *
 * @param type Type of the rich text, always "phone_number"
 * @param text The text
 * @param phoneNumber The phone number
 */
@Serializable
data class RichTextPhoneNumber(
    val text: RichText,
    @SerialName("phone_number") val phoneNumber: String
) : RichText {
    override val type: String = "phone_number"
}
