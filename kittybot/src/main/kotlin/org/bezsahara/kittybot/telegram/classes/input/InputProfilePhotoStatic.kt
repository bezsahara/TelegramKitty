package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.TelegramFile


/**
 * A static profile photo in the .JPG format.
 * 
 * [link](https://core.telegram.org/bots/api#inputprofilephotostatic): https://core.telegram.org/bots/api#inputprofilephotostatic
 * 
 * @param type Type of the profile photo, must be static
 * @param photo The static profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 */
@Serializable
data class InputProfilePhotoStatic(
    val photo: TelegramFile
) : InputProfilePhoto {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        photo.asVertx().executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        photo.asVertx().execute(builder, null)
    }
    override val type: String = "static"
}

