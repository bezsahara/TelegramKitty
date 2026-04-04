package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.utils.ParseMode
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import kotlin.collections.List
import kotlin.Unit
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.serialization.Serializable


/**
 * Represents a video to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmediavideo): https://core.telegram.org/bots/api#inputmediavideo
 * 
 * @param type Type of the result, must be video
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param cover Optional. Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param startTimestamp Optional. Start timestamp for the video in the message
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param width Optional. Video width
 * @param height Optional. Video height
 * @param duration Optional. Video duration in seconds
 * @param supportsStreaming Optional. Pass True if the uploaded video is suitable for streaming
 * @param hasSpoiler Optional. Pass True if the video needs to be covered with a spoiler animation
 */
@Serializable
data class InputMediaVideo(
    val media: TelegramFile,
    val thumbnail: TelegramFile? = null,
    val cover: TelegramFile? = null,
    @SerialName("start_timestamp") val startTimestamp: Long? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null,
    @SerialName("supports_streaming") val supportsStreaming: Boolean? = null,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null
) : InputMedia, MediaGroupAccepted {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        media.asVertx().executeCustom(builder, null)
        thumbnail?.asVertx()?.executeCustom(builder, null)
        cover?.asVertx()?.executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        media.asVertx().execute(builder, null)
        thumbnail?.asVertx()?.execute(builder, null)
        cover?.asVertx()?.execute(builder, null)
    }
    override val type: String = "video"
}

