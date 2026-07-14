package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A text with an email address.
 *
 * [link](https://core.telegram.org/bots/api#richtextemailaddress): https://core.telegram.org/bots/api#richtextemailaddress
 *
 * @param type Type of the rich text, always "email_address"
 * @param text The text
 * @param emailAddress The email address
 */
@Serializable
data class RichTextEmailAddress(
    val text: RichText,
    @SerialName("email_address") val emailAddress: String
) : RichText {
    override val type: String = "email_address"
}
