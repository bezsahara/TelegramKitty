package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A slideshow, corresponding to the custom HTML tag <tg-slideshow>.
 *
 * [link](https://core.telegram.org/bots/api#richblockslideshow): https://core.telegram.org/bots/api#richblockslideshow
 *
 * @param type Type of the block, always "slideshow"
 * @param blocks Elements of the slideshow
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockSlideshow(
    val blocks: List<RichBlock>,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "slideshow"
}
