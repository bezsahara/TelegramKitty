package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * Caption of a rich formatted block.
 *
 * [link](https://core.telegram.org/bots/api#richblockcaption): https://core.telegram.org/bots/api#richblockcaption
 *
 * @param text Block caption
 * @param credit Optional. Block credit which corresponds to the HTML tag <cite>
 */
@Serializable
data class RichBlockCaption(
    val text: RichText,
    val credit: RichText? = null
)
