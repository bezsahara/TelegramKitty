package org.bezsahara.kittybot.telegram.classes.chats.boosts.sources


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 * 
 * *[link](https://core.telegram.org/bots/api#chatboostsourcegiftcode)*: https://core.telegram.org/bots/api#chatboostsourcegiftcode
 * 
 * @param source Source of the boost, always "gift_code"
 * @param user User for which the gift code was created
 */
@Serializable
data class ChatBoostSourceGiftCode(
    val user: User,
) : ChatBoostSource {
    override val source: String = "gift_code"
}

