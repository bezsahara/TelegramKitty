package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A block quotation, corresponding to the HTML tag <blockquote>.
 *
 * [link](https://core.telegram.org/bots/api#richblockblockquotation): https://core.telegram.org/bots/api#richblockblockquotation
 *
 * @param type Type of the block, always "blockquote"
 * @param blocks Content of the block
 * @param credit Optional. Credit of the block
 */
@Serializable
data class RichBlockBlockQuotation(
    val blocks: List<RichBlock>,
    val credit: RichText? = null
) : RichBlock {
    override val type: String = "blockquote"
}
