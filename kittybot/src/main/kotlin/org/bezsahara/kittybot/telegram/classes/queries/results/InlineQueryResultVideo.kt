package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultvideo)*: https://core.telegram.org/bots/api#inlinequeryresultvideo
 * 
 * @param type Type of the result, must be video
 * @param id Unique identifier for this result, 1-64 bytes
 * @param videoUrl A valid URL for the embedded video player or video file
 * @param mimeType MIME type of the content of the video URL, "text/html" or "video/mp4"
 * @param thumbnailUrl URL of the thumbnail (JPEG only) for the video
 * @param title Title for the result
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param videoWidth Optional. Video width
 * @param videoHeight Optional. Video height
 * @param videoDuration Optional. Video duration in seconds
 * @param description Optional. Short description of the result
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video. This field is required if InlineQueryResultVideo is used to send an HTML-page as a result (e.g., a YouTube video).
 */
@Serializable
data class InlineQueryResultVideo(
    val id: String,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    val title: String,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("video_width") val videoWidth: Long? = null,
    @SerialName("video_height") val videoHeight: Long? = null,
    @SerialName("video_duration") val videoDuration: Long? = null,
    val description: String? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "video"
}

