package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import kotlin.Unit
import org.bezsahara.kittybot.telegram.values.InputProfilePhotoType
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.serialization.Serializable


/**
 * An animated profile photo in the MPEG4 format.
 * 
 * [link](https://core.telegram.org/bots/api#inputprofilephotoanimated): https://core.telegram.org/bots/api#inputprofilephotoanimated
 * 
 * @param type Type of the profile photo, must be animated
 * @param animation The animated profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param mainFrameTimestamp Optional. Timestamp in seconds of the frame that will be used as the static profile photo. Defaults to 0.0.
 */
@Serializable
data class InputProfilePhotoAnimated(
    val animation: TelegramFile,
    @SerialName("main_frame_timestamp") val mainFrameTimestamp: Double? = null
) : InputProfilePhoto {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
        animation.asVertx().executeCustom(builder, null)
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
        animation.asVertx().execute(builder, null)
    }
    override val type: InputProfilePhotoType = InputProfilePhotoType.ANIMATED
}

