package org.bezsahara.kittybot.telegram.classes.message.reactions

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.ReactionEmoji
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import kotlinx.serialization.Serializable


/**
 * The reaction is based on an emoji.
 * 
 * [link](https://core.telegram.org/bots/api#reactiontypeemoji): https://core.telegram.org/bots/api#reactiontypeemoji
 * 
 * @param type Type of the reaction, always "emoji"
 * @param emoji Reaction emoji. Currently, it can be one of "❤", "👍", "👎", "🔥", "🥰", "👏", "😁", "🤔", "🤯", "😱", "🤬", "😢", "🎉", "🤩", "🤮", "💩", "🙏", "👌", "🕊", "🤡", "🥱", "🥴", "😍", "🐳", "❤‍🔥", "🌚", "🌭", "💯", "🤣", "⚡", "🍌", "🏆", "💔", "🤨", "😐", "🍓", "🍾", "💋", "🖕", "😈", "😴", "😭", "🤓", "👻", "👨‍💻", "👀", "🎃", "🙈", "😇", "😨", "🤝", "✍", "🤗", "🫡", "🎅", "🎄", "☃", "💅", "🤪", "🗿", "🆒", "💘", "🙉", "🦄", "😘", "💊", "🙊", "😎", "👾", "🤷‍♂", "🤷", "🤷‍♀", "😡"
 */
@Serializable
data class ReactionTypeEmoji(
    val emoji: ReactionEmoji
) : ReactionType {
    override val type: String = "emoji"
}

