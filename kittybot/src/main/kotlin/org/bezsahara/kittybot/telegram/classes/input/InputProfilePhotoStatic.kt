package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import kotlinx.serialization.Serializable


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
    val photo: String
) : InputProfilePhoto {
    override val type: String get() = "static"
}

