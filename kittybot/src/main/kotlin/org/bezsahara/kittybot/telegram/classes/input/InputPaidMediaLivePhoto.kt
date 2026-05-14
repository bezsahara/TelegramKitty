package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import kotlin.Unit
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.serialization.Serializable


/**
 * The paid media to send is a live photo.
 * 
 * [link](https://core.telegram.org/bots/api#inputpaidmedialivephoto): https://core.telegram.org/bots/api#inputpaidmedialivephoto
 * 
 * @param type Type of the media, must be live_photo
 * @param media Video of the live photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Sending live photos by a URL is currently unsupported.
 * @param photo The static photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Sending live photos by a URL is currently unsupported.
 */
@Serializable
data class InputPaidMediaLivePhoto(
    val media: TelegramFile,
    val photo: TelegramFile
) : InputPaidMedia {
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

