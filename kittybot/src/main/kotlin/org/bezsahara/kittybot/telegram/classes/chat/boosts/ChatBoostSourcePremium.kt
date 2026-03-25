package org.bezsahara.kittybot.telegram.classes.chat.boosts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSource
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
 * 
 * [link](https://core.telegram.org/bots/api#chatboostsourcepremium): https://core.telegram.org/bots/api#chatboostsourcepremium
 * 
 * @param source Source of the boost, always "premium"
 * @param user User that boosted the chat
 */
@Serializable
data class ChatBoostSourcePremium(
    val user: User
) : ChatBoostSource {
    override val source: String get() = "premium"
}

