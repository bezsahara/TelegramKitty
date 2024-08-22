package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.ChatId


/**
 * Represents the scope of bot commands, covering a specific chat.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopechat)*: https://core.telegram.org/bots/api#botcommandscopechat
 * 
 * @param type Scope type, must be chat
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class BotCommandScopeChat(
    @SerialName("chat_id") val chatId: ChatId,
) : BotCommandScope {
    override val type: String = "chat"
}

