package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.ChatId


/**
 * Represents the scope of bot commands, covering all administrators of a specific group or supergroup chat.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopechatadministrators)*: https://core.telegram.org/bots/api#botcommandscopechatadministrators
 * 
 * @param type Scope type, must be chat_administrators
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class BotCommandScopeChatAdministrators(
    @SerialName("chat_id") val chatId: ChatId,
) : BotCommandScope {
    override val type: String = "chat_administrators"
}

