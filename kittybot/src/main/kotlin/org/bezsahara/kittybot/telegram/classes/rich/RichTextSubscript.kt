package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A subscript text.
 *
 * [link](https://core.telegram.org/bots/api#richtextsubscript): https://core.telegram.org/bots/api#richtextsubscript
 *
 * @param type Type of the rich text, always "subscript"
 * @param text The text
 */
@Serializable
data class RichTextSubscript(
    val text: RichText
) : RichText {
    override val type: String = "subscript"
}
