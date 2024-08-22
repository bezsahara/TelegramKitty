package org.bezsahara.kittybot.telegram.classes.reactions


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The reaction is based on a custom emoji.
 * 
 * *[link](https://core.telegram.org/bots/api#reactiontypecustomemoji)*: https://core.telegram.org/bots/api#reactiontypecustomemoji
 * 
 * @param type Type of the reaction, always "custom_emoji"
 * @param customEmojiId Custom emoji identifier
 */
@Serializable
data class ReactionTypeCustomEmoji(
    @SerialName("custom_emoji_id") val customEmojiId: String,
) : ReactionType {
    override val type: String = "custom_emoji"
}

