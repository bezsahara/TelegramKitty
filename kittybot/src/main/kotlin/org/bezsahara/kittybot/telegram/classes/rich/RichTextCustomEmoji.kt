package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A custom emoji.
 *
 * [link](https://core.telegram.org/bots/api#richtextcustomemoji): https://core.telegram.org/bots/api#richtextcustomemoji
 *
 * @param type Type of the rich text, always "custom_emoji"
 * @param customEmojiId Unique identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker.
 * @param alternativeText Alternative emoji for the custom emoji
 */
@Serializable
data class RichTextCustomEmoji(
    @SerialName("custom_emoji_id") val customEmojiId: String,
    @SerialName("alternative_text") val alternativeText: String
) : RichText {
    override val type: String = "custom_emoji"
}
