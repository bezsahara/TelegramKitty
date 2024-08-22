package org.bezsahara.kittybot.telegram.classes.reactions


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The reaction is based on an emoji.
 * 
 * *[link](https://core.telegram.org/bots/api#reactiontypeemoji)*: https://core.telegram.org/bots/api#reactiontypeemoji
 * 
 * @param type Type of the reaction, always "emoji"
 * @param emoji Reaction emoji. Currently, it can be one of "👍", "👎", "❤", "🔥", "🥰", "👏", "😁", "🤔", "🤯", "😱", "🤬", "😢", "🎉", "🤩", "🤮", "💩", "🙏", "👌", "🕊", "🤡", "🥱", "🥴", "😍", "🐳", "❤‍🔥", "🌚", "🌭", "💯", "🤣", "⚡", "🍌", "🏆", "💔", "🤨", "😐", "🍓", "🍾", "💋", "🖕", "😈", "😴", "😭", "🤓", "👻", "👨‍💻", "👀", "🎃", "🙈", "😇", "😨", "🤝", "✍", "🤗", "🫡", "🎅", "🎄", "☃", "💅", "🤪", "🗿", "🆒", "💘", "🙉", "🦄", "😘", "💊", "🙊", "😎", "👾", "🤷‍♂", "🤷", "🤷‍♀", "😡"
 */
@Serializable
data class ReactionTypeEmoji(
    val emoji: String,
) : ReactionType {
    override val type: String = "emoji"
}

