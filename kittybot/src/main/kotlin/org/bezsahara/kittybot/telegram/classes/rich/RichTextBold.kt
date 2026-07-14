package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A bold text.
 *
 * [link](https://core.telegram.org/bots/api#richtextbold): https://core.telegram.org/bots/api#richtextbold
 *
 * @param type Type of the rich text, always "bold"
 * @param text The text
 */
@Serializable
data class RichTextBold(
    val text: RichText
) : RichText {
    override val type: String = "bold"
}
