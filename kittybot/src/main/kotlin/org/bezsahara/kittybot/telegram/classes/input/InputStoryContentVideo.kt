package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import kotlinx.serialization.Serializable


/**
 * Describes a video to post as a story.
 * 
 * [link](https://core.telegram.org/bots/api#inputstorycontentvideo): https://core.telegram.org/bots/api#inputstorycontentvideo
 * 
 * @param type Type of the content, must be video
 * @param video The video to post as a story. The video must be of the size 720x1280, streamable, encoded with H.265 codec, with key frames added each second in the MPEG4 format, and must not exceed 30 MB. The video can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the video was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param duration Optional. Precise duration of the video in seconds; 0-60
 * @param coverFrameTimestamp Optional. Timestamp in seconds of the frame that will be used as the static cover for the story. Defaults to 0.0.
 * @param isAnimation Optional. Pass True if the video has no sound
 */
@Serializable
data class InputStoryContentVideo(
    val video: String,
    val duration: Double? = null,
    @SerialName("cover_frame_timestamp") val coverFrameTimestamp: Double? = null,
    @SerialName("is_animation") val isAnimation: Boolean? = null
) : InputStoryContent {
    override val type: String = "video"
}

