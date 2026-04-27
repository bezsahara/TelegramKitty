package org.bezsahara.kittybot.telegram.classes.inline

import org.bezsahara.kittybot.telegram.classes.inline.InputMessageContent
import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.values.InlineQueryResultDocumentMimeType
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResult


/**
 * Represents a link to a file. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file. Currently, only .PDF and .ZIP files can be sent using this method.
 * 
 * [link](https://core.telegram.org/bots/api#inlinequeryresultdocument): https://core.telegram.org/bots/api#inlinequeryresultdocument
 * 
 * @param type Type of the result, must be document
 * @param id Unique identifier for this result, 1-64 bytes
 * @param title Title for the result
 * @param caption Optional. Caption of the document to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the document caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param documentUrl A valid URL for the file
 * @param mimeType MIME type of the content of the file, either "application/pdf" or "application/zip"
 * @param description Optional. Short description of the result
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the file
 * @param thumbnailUrl Optional. URL of the thumbnail (JPEG only) for the file
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
data class InlineQueryResultDocument(
    val id: String,
    val title: String,
    @SerialName("document_url") val documentUrl: String,
    @SerialName("mime_type") val mimeType: InlineQueryResultDocumentMimeType,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    val description: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("thumbnail_width") val thumbnailWidth: Long? = null,
    @SerialName("thumbnail_height") val thumbnailHeight: Long? = null
) : InlineQueryResult {
    override val type: String = "document"
}

