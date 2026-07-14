package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A strikethrough text.
 *
 * [link](https://core.telegram.org/bots/api#richtextstrikethrough): https://core.telegram.org/bots/api#richtextstrikethrough
 *
 * @param type Type of the rich text, always "strikethrough"
 * @param text The text
 */
@Serializable
data class RichTextStrikethrough(
    val text: RichText
) : RichText {
    override val type: String = "strikethrough"
}
