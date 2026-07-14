package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A link to an anchor.
 *
 * [link](https://core.telegram.org/bots/api#richtextanchorlink): https://core.telegram.org/bots/api#richtextanchorlink
 *
 * @param type Type of the rich text, always "anchor_link"
 * @param text The link text
 * @param anchorName The name of the anchor. If the name is empty, then the link brings back to the top of the message.
 */
@Serializable
data class RichTextAnchorLink(
    val text: RichText,
    @SerialName("anchor_name") val anchorName: String
) : RichText {
    override val type: String = "anchor_link"
}
