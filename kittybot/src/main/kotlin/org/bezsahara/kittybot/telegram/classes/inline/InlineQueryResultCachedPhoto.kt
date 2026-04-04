package org.bezsahara.kittybot.telegram.classes.inline

import org.bezsahara.kittybot.telegram.classes.inline.InputMessageContent
import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.utils.ParseMode
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResult


/**
 * Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 * 
 * [link](https://core.telegram.org/bots/api#inlinequeryresultcachedphoto): https://core.telegram.org/bots/api#inlinequeryresultcachedphoto
 * 
 * @param type Type of the result, must be photo
 * @param id Unique identifier for this result, 1-64 bytes
 * @param photoFileId A valid file identifier of the photo
 * @param title Optional. Title for the result
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the photo
 */
@Serializable
data class InlineQueryResultCachedPhoto(
    val id: String,
    @SerialName("photo_file_id") val photoFileId: String,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: String = "photo"
}

