package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A text paragraph, corresponding to the HTML tag <p>.
 *
 * [link](https://core.telegram.org/bots/api#richblockparagraph): https://core.telegram.org/bots/api#richblockparagraph
 *
 * @param type Type of the block, always "paragraph"
 * @param text Text of the block
 */
@Serializable
data class RichBlockParagraph(
    val text: RichText
) : RichBlock {
    override val type: String = "paragraph"
}
