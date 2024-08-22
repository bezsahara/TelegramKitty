package org.bezsahara.kittybot.telegram.classes.media.input


import io.ktor.http.content.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.client.TelegramFile


/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 * 
 * *[link](https://core.telegram.org/bots/api#inputmediaanimation)*: https://core.telegram.org/bots/api#inputmediaanimation
 * 
 * @param type Type of the result, must be animation
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Optional. Caption of the animation to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the animation caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param width Optional. Animation width
 * @param height Optional. Animation height
 * @param duration Optional. Animation duration in seconds
 * @param hasSpoiler Optional. Pass True if the animation needs to be covered with a spoiler animation
 */
@Serializable
data class InputMediaAnimation(
    val media: TelegramFile,
    val thumbnail: TelegramFile? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
) : InputMedia {
    override val type: String = "animation"

    override fun MutableList<PartData>.appendFiles() {
        media.run { appendOnlyData("media") }
        thumbnail?.run { appendOnlyData("thumbnail") }
    }
}
