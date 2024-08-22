package org.bezsahara.kittybot.telegram.classes.media.input


import io.ktor.http.content.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.client.TelegramFile


/**
 * Represents a photo to be sent.
 * 
 * *[link](https://core.telegram.org/bots/api#inputmediaphoto)*: https://core.telegram.org/bots/api#inputmediaphoto
 * 
 * @param type Type of the result, must be photo
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Optional. Pass True if the photo needs to be covered with a spoiler animation
 */
@Serializable
data class InputMediaPhoto(
    val media: TelegramFile,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
) : InputMedia, MediaGroupAccepted {
    override val type: String = "photo"

    override fun MutableList<PartData>.appendFiles() {
        media.run { appendOnlyData("media") }
    }
}

