package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A text covered by a spoiler.
 *
 * [link](https://core.telegram.org/bots/api#richtextspoiler): https://core.telegram.org/bots/api#richtextspoiler
 *
 * @param type Type of the rich text, always "spoiler"
 * @param text The text
 */
@Serializable
data class RichTextSpoiler(
    val text: RichText
) : RichText {
    override val type: String = "spoiler"
}
