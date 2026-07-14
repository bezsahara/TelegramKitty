package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * An underlined text.
 *
 * [link](https://core.telegram.org/bots/api#richtextunderline): https://core.telegram.org/bots/api#richtextunderline
 *
 * @param type Type of the rich text, always "underline"
 * @param text The text
 */
@Serializable
data class RichTextUnderline(
    val text: RichText
) : RichText {
    override val type: String = "underline"
}
