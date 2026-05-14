package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.ChatId


/**
 * Represents the scope of bot commands, covering a specific chat.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopechat): https://core.telegram.org/bots/api#botcommandscopechat
 * 
 * @param type Scope type, must be chat
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username. Channel direct messages chats and channel chats aren't supported.
 */
@Serializable
data class BotCommandScopeChat(
    @SerialName("chat_id") val chatId: ChatId
) : BotCommandScope {
    override val type: String = "chat"
}

