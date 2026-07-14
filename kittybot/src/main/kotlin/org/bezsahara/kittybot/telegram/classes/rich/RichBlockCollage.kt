package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A collage, corresponding to the custom HTML tag <tg-collage>.
 *
 * [link](https://core.telegram.org/bots/api#richblockcollage): https://core.telegram.org/bots/api#richblockcollage
 *
 * @param type Type of the block, always "collage"
 * @param blocks Elements of the collage
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockCollage(
    val blocks: List<RichBlock>,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "collage"
}
