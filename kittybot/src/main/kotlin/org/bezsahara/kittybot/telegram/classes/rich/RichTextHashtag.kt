package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A hashtag.
 *
 * [link](https://core.telegram.org/bots/api#richtexthashtag): https://core.telegram.org/bots/api#richtexthashtag
 *
 * @param type Type of the rich text, always "hashtag"
 * @param text The text
 * @param hashtag The hashtag
 */
@Serializable
data class RichTextHashtag(
    val text: RichText,
    val hashtag: String
) : RichText {
    override val type: String = "hashtag"
}
