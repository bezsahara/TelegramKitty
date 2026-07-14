package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A quotation with centered text, loosely corresponding to the HTML tag <aside>.
 *
 * [link](https://core.telegram.org/bots/api#richblockpullquotation): https://core.telegram.org/bots/api#richblockpullquotation
 *
 * @param type Type of the block, always "pullquote"
 * @param text Text of the block
 * @param credit Optional. Credit of the block
 */
@Serializable
data class RichBlockPullQuotation(
    val text: RichText,
    val credit: RichText? = null
) : RichBlock {
    override val type: String = "pullquote"
}
