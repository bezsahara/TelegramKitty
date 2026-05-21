package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.values.ParseMode


/**
 * Represents a live photo to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmedialivephoto): https://core.telegram.org/bots/api#inputmedialivephoto
 * 
 * @param type Type of the result, must be live_photo
 * @param media Video of the live photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Sending live photos by a URL is currently unsupported.
 * @param photo The static photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Sending live photos by a URL is currently unsupported.
 * @param caption Optional. Caption of the live photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the live photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param hasSpoiler Optional. Pass True if the live photo needs to be covered with a spoiler animation
 */
@Serializable
data class InputMediaLivePhoto(
    val media: TelegramFile,
    val photo: TelegramFile,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null
) : InputPollMedia, InputPollOptionMedia, InputMedia, MediaGroupAccepted {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        media.asVertx().executeCustom(builder, null)
        photo.asVertx().executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        media.asVertx().execute(builder, null)
        photo.asVertx().execute(builder, null)
    }
    override val type: String = "live_photo"
}

