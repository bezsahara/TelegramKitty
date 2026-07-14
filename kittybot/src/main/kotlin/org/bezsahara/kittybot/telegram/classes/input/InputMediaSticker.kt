package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.TelegramFile


/**
 * Represents a sticker file to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmediasticker): https://core.telegram.org/bots/api#inputmediasticker
 * 
 * @param type Type of the media, must be sticker
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a .WEBP sticker from the Internet, or pass "attach://<file_attach_name>" to upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param emoji Optional. Emoji associated with the sticker; only for just uploaded stickers
 */
@Serializable
data class InputMediaSticker(
    val media: TelegramFile,
    val emoji: String? = null
) : InputPollOptionMedia {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        media.asVertx().executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        media.asVertx().execute(builder, null)
    }
    override val type: String = "sticker"
}

