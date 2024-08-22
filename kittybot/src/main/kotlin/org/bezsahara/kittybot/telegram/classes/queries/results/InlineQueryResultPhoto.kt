package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultphoto)*: https://core.telegram.org/bots/api#inlinequeryresultphoto
 * 
 * @param type Type of the result, must be photo
 * @param id Unique identifier for this result, 1-64 bytes
 * @param photoUrl A valid URL of the photo. Photo must be in JPEG format. Photo size must not exceed 5MB
 * @param thumbnailUrl URL of the thumbnail for the photo
 * @param photoWidth Optional. Width of the photo
 * @param photoHeight Optional. Height of the photo
 * @param title Optional. Title for the result
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the photo
 */
@Serializable
data class InlineQueryResultPhoto(
    val id: String,
    @SerialName("photo_url") val photoUrl: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("photo_width") val photoWidth: Long? = null,
    @SerialName("photo_height") val photoHeight: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "photo"
}

