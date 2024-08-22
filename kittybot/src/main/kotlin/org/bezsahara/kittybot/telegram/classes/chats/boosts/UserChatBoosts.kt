package org.bezsahara.kittybot.telegram.classes.chats.boosts


import kotlinx.serialization.Serializable


/**
 * This object represents a list of boosts added to a chat by a user.
 * 
 * *[link](https://core.telegram.org/bots/api#userchatboosts)*: https://core.telegram.org/bots/api#userchatboosts
 * 
 * @param boosts The list of boosts added to the chat by the user
 */
@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>
)

