package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A superscript text.
 *
 * [link](https://core.telegram.org/bots/api#richtextsuperscript): https://core.telegram.org/bots/api#richtextsuperscript
 *
 * @param type Type of the rich text, always "superscript"
 * @param text The text
 */
@Serializable
data class RichTextSuperscript(
    val text: RichText
) : RichText {
    override val type: String = "superscript"
}
