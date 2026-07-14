package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A cashtag.
 *
 * [link](https://core.telegram.org/bots/api#richtextcashtag): https://core.telegram.org/bots/api#richtextcashtag
 *
 * @param type Type of the rich text, always "cashtag"
 * @param text The text
 * @param cashtag The cashtag
 */
@Serializable
data class RichTextCashtag(
    val text: RichText,
    val cashtag: String
) : RichText {
    override val type: String = "cashtag"
}
