package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * An italicized text.
 *
 * [link](https://core.telegram.org/bots/api#richtextitalic): https://core.telegram.org/bots/api#richtextitalic
 *
 * @param type Type of the rich text, always "italic"
 * @param text The text
 */
@Serializable
data class RichTextItalic(
    val text: RichText
) : RichText {
    override val type: String = "italic"
}
