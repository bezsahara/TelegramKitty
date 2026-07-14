package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Animation


/**
 * A block with an animation, corresponding to the HTML tag <video>.
 *
 * [link](https://core.telegram.org/bots/api#richblockanimation): https://core.telegram.org/bots/api#richblockanimation
 *
 * @param type Type of the block, always "animation"
 * @param animation The animation
 * @param hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockAnimation(
    val animation: Animation,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "animation"
}
