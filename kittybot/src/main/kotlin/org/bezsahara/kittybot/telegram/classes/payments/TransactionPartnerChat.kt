package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.gifts.Gift


/**
 * Describes a transaction with a chat.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartnerchat): https://core.telegram.org/bots/api#transactionpartnerchat
 * 
 * @param type Type of the transaction partner, always "chat"
 * @param chat Information about the chat
 * @param gift Optional. The gift sent to the chat by the bot
 */
@Serializable
data class TransactionPartnerChat(
    val chat: Chat,
    val gift: Gift? = null
) : TransactionPartner {
    override val type: String = "chat"
}

