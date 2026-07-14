package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A marked text.
 *
 * [link](https://core.telegram.org/bots/api#richtextmarked): https://core.telegram.org/bots/api#richtextmarked
 *
 * @param type Type of the rich text, always "marked"
 * @param text The text
 */
@Serializable
data class RichTextMarked(
    val text: RichText
) : RichText {
    override val type: String = "marked"
}
