package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A text with a link.
 *
 * [link](https://core.telegram.org/bots/api#richtexturl): https://core.telegram.org/bots/api#richtexturl
 *
 * @param type Type of the rich text, always "url"
 * @param text The text
 * @param url URL of the link
 */
@Serializable
data class RichTextUrl(
    val text: RichText,
    val url: String
) : RichText {
    override val type: String = "url"
}
