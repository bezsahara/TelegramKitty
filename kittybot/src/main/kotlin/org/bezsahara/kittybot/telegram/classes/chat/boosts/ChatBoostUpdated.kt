package org.bezsahara.kittybot.telegram.classes.chat.boosts

import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoost
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * This object represents a boost added to a chat or changed.
 * 
 * [link](https://core.telegram.org/bots/api#chatboostupdated): https://core.telegram.org/bots/api#chatboostupdated
 * 
 * @param chat Chat which was boosted
 * @param boost Information about the chat boost
 */
@Serializable
data class ChatBoostUpdated(
    val chat: Chat,
    val boost: ChatBoost
)

