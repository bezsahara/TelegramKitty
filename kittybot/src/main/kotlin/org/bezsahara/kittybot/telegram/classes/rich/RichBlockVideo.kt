package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Video


/**
 * A block with a video, corresponding to the HTML tag <video>.
 *
 * [link](https://core.telegram.org/bots/api#richblockvideo): https://core.telegram.org/bots/api#richblockvideo
 *
 * @param type Type of the block, always "video"
 * @param video The video
 * @param hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockVideo(
    val video: Video,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "video"
}
