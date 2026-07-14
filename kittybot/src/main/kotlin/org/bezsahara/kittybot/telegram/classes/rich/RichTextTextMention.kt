package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * A mention of a Telegram user by their identifier.
 *
 * [link](https://core.telegram.org/bots/api#richtexttextmention): https://core.telegram.org/bots/api#richtexttextmention
 *
 * @param type Type of the rich text, always "text_mention"
 * @param text The text
 * @param user The mentioned user
 */
@Serializable
data class RichTextTextMention(
    val text: RichText,
    val user: User
) : RichText {
    override val type: String = "text_mention"
}
