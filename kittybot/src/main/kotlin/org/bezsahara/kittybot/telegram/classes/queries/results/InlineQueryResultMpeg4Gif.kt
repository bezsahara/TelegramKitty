package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultmpeg4gif)*: https://core.telegram.org/bots/api#inlinequeryresultmpeg4gif
 * 
 * @param type Type of the result, must be mpeg4_gif
 * @param id Unique identifier for this result, 1-64 bytes
 * @param mpeg4Url A valid URL for the MPEG4 file. File size must not exceed 1MB
 * @param mpeg4Width Optional. Video width
 * @param mpeg4Height Optional. Video height
 * @param mpeg4Duration Optional. Video duration in seconds
 * @param thumbnailUrl URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
 * @param thumbnailMimeType Optional. MIME type of the thumbnail, must be one of "image/jpeg", "image/gif", or "video/mp4". Defaults to "image/jpeg"
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video animation
 */
@Serializable
data class InlineQueryResultMpeg4Gif(
    val id: String,
    @SerialName("mpeg4_url") val mpeg4Url: String,
    @SerialName("mpeg4_width") val mpeg4Width: Long? = null,
    @SerialName("mpeg4_height") val mpeg4Height: Long? = null,
    @SerialName("mpeg4_duration") val mpeg4Duration: Long? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("thumbnail_mime_type") val thumbnailMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "mpeg4_gif"
}

