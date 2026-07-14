package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A monowidth text.
 *
 * [link](https://core.telegram.org/bots/api#richtextcode): https://core.telegram.org/bots/api#richtextcode
 *
 * @param type Type of the rich text, always "code"
 * @param text The text
 */
@Serializable
data class RichTextCode(
    val text: RichText
) : RichText {
    override val type: String = "code"
}
