package org.bezsahara.kittybot.telegram.classes.chats.boosts.sources


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
 * 
 * *[link](https://core.telegram.org/bots/api#chatboostsourcepremium)*: https://core.telegram.org/bots/api#chatboostsourcepremium
 * 
 * @param source Source of the boost, always "premium"
 * @param user User that boosted the chat
 */
@Serializable
data class ChatBoostSourcePremium(
    val user: User,
) : ChatBoostSource {
    override val source: String = "premium"
}

