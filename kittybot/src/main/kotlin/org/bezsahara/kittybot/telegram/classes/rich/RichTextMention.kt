package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A mention by a username.
 *
 * [link](https://core.telegram.org/bots/api#richtextmention): https://core.telegram.org/bots/api#richtextmention
 *
 * @param type Type of the rich text, always "mention"
 * @param text The text
 * @param username The username
 */
@Serializable
data class RichTextMention(
    val text: RichText,
    val username: String
) : RichText {
    override val type: String = "mention"
}
