package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.TelegramFile


/**
 * Describes a photo to post as a story.
 * 
 * [link](https://core.telegram.org/bots/api#inputstorycontentphoto): https://core.telegram.org/bots/api#inputstorycontentphoto
 * 
 * @param type Type of the content, must be photo
 * @param photo The photo to post as a story. The photo must be of the size 1080x1920 and must not exceed 10 MB. The photo can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 */
@Serializable
data class InputStoryContentPhoto(
    val photo: TelegramFile
) : InputStoryContent {
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
    override val type: String = "photo"
}

