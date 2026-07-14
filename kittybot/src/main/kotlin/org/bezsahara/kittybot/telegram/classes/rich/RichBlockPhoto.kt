package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize


/**
 * A block with a photo, corresponding to the HTML tag <img>.
 *
 * [link](https://core.telegram.org/bots/api#richblockphoto): https://core.telegram.org/bots/api#richblockphoto
 *
 * @param type Type of the block, always "photo"
 * @param photo Available sizes of the photo
 * @param hasSpoiler Optional. True, if the media preview is covered by a spoiler animation
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockPhoto(
    val photo: List<PhotoSize>,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "photo"
}
