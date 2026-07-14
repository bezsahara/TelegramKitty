package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A list of blocks, corresponding to the HTML tag <ul> or <ol> with multiple nested tags <li>.
 *
 * [link](https://core.telegram.org/bots/api#richblocklist): https://core.telegram.org/bots/api#richblocklist
 *
 * @param type Type of the block, always "list"
 * @param items Items of the list
 */
@Serializable
data class RichBlockList(
    val items: List<RichBlockListItem>
) : RichBlock {
    override val type: String = "list"
}
