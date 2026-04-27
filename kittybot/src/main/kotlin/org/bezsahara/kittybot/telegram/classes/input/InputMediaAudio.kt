package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import kotlin.collections.List
import kotlin.Unit
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.serialization.Serializable


/**
 * Represents an audio file to be treated as music to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmediaaudio): https://core.telegram.org/bots/api#inputmediaaudio
 * 
 * @param type Type of the result, must be audio
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Optional. Caption of the audio to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Optional. Duration of the audio in seconds
 * @param performer Optional. Performer of the audio
 * @param title Optional. Title of the audio
 */
@Serializable
data class InputMediaAudio(
    val media: TelegramFile,
    val thumbnail: TelegramFile? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    val duration: Long? = null,
    val performer: String? = null,
    val title: String? = null
) : InputMedia, MediaGroupAccepted {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        media.asVertx().executeCustom(builder, null)
        thumbnail?.asVertx()?.executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        media.asVertx().execute(builder, null)
        thumbnail?.asVertx()?.execute(builder, null)
    }
    override val type: String = "audio"
}

