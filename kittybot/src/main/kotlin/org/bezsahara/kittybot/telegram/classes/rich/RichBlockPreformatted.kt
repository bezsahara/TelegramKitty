package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A preformatted text block, corresponding to the nested HTML tags <pre> and <code>.
 *
 * [link](https://core.telegram.org/bots/api#richblockpreformatted): https://core.telegram.org/bots/api#richblockpreformatted
 *
 * @param type Type of the block, always "pre"
 * @param text Text of the block
 * @param language Optional. The programming language of the text
 */
@Serializable
data class RichBlockPreformatted(
    val text: RichText,
    val language: String? = null
) : RichBlock {
    override val type: String = "pre"
}
