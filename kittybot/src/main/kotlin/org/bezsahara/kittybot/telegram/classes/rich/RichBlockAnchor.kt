package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A block with an anchor, corresponding to the HTML tag <a> with the attribute name.
 *
 * [link](https://core.telegram.org/bots/api#richblockanchor): https://core.telegram.org/bots/api#richblockanchor
 *
 * @param type Type of the block, always "anchor"
 * @param name The name of the anchor
 */
@Serializable
data class RichBlockAnchor(
    val name: String
) : RichBlock {
    override val type: String = "anchor"
}
