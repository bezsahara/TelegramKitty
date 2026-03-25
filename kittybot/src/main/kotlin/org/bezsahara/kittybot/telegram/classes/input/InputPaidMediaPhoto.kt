package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import kotlinx.serialization.Serializable


/**
 * The paid media to send is a photo.
 * 
 * [link](https://core.telegram.org/bots/api#inputpaidmediaphoto): https://core.telegram.org/bots/api#inputpaidmediaphoto
 * 
 * @param type Type of the media, must be photo
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 */
@Serializable
data class InputPaidMediaPhoto(
    val media: String
) : InputPaidMedia {
    override val type: String get() = "photo"
}

