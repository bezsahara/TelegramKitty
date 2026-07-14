package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A section heading, corresponding to the HTML tags <h1>, <h2>, <h3>, <h4>, <h5>, or <h6>.
 *
 * [link](https://core.telegram.org/bots/api#richblocksectionheading): https://core.telegram.org/bots/api#richblocksectionheading
 *
 * @param type Type of the block, always "heading"
 * @param text Text of the block
 * @param size Relative size of the text font; 1-6, 1 is the largest, 6 is the smallest
 */
@Serializable
data class RichBlockSectionHeading(
    val text: RichText,
    val size: Long
) : RichBlock {
    override val type: String = "heading"
}
