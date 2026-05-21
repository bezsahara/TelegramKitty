package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.Serializable


/**
 * The paid media is a video.
 * 
 * [link](https://core.telegram.org/bots/api#paidmediavideo): https://core.telegram.org/bots/api#paidmediavideo
 * 
 * @param type Type of the paid media, always "video"
 * @param video The video
 */
@Serializable
data class PaidMediaVideo(
    val video: Video
) : PaidMedia {
    override val type: String = "video"
}

