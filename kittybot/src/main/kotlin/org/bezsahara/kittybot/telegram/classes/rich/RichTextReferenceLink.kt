package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A link to a reference.
 *
 * [link](https://core.telegram.org/bots/api#richtextreferencelink): https://core.telegram.org/bots/api#richtextreferencelink
 *
 * @param type Type of the rich text, always "reference_link"
 * @param text The link text
 * @param referenceName The name of the reference
 */
@Serializable
data class RichTextReferenceLink(
    val text: RichText,
    @SerialName("reference_name") val referenceName: String
) : RichText {
    override val type: String = "reference_link"
}
