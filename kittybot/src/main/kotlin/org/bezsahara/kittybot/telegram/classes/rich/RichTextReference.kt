package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A reference.
 *
 * [link](https://core.telegram.org/bots/api#richtextreference): https://core.telegram.org/bots/api#richtextreference
 *
 * @param type Type of the rich text, always "reference"
 * @param text Text of the reference
 * @param name The name of the reference
 */
@Serializable
data class RichTextReference(
    val text: RichText,
    val name: String
) : RichText {
    override val type: String = "reference"
}
