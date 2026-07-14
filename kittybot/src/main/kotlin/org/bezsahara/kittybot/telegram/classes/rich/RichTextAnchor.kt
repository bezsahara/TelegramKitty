package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * An anchor.
 *
 * [link](https://core.telegram.org/bots/api#richtextanchor): https://core.telegram.org/bots/api#richtextanchor
 *
 * @param type Type of the rich text, always "anchor"
 * @param name The name of the anchor
 */
@Serializable
data class RichTextAnchor(
    val name: String
) : RichText {
    override val type: String = "anchor"
}
