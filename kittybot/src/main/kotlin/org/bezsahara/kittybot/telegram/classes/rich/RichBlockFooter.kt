package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A footer, corresponding to the HTML tag <footer>.
 *
 * [link](https://core.telegram.org/bots/api#richblockfooter): https://core.telegram.org/bots/api#richblockfooter
 *
 * @param type Type of the block, always "footer"
 * @param text Text of the block
 */
@Serializable
data class RichBlockFooter(
    val text: RichText
) : RichBlock {
    override val type: String = "footer"
}
