package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the contact.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultcontact)*: https://core.telegram.org/bots/api#inlinequeryresultcontact
 * 
 * @param type Type of the result, must be contact
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Optional. Contact's last name
 * @param vcard Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the contact
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
data class InlineQueryResultContact(
    val id: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    val vcard: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("thumbnail_width") val thumbnailWidth: Long? = null,
    @SerialName("thumbnail_height") val thumbnailHeight: Long? = null,
) : InlineQueryResult {
    override val type: String = "contact"
}

