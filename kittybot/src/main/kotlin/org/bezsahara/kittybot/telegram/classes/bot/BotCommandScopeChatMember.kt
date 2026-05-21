package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.ChatId


/**
 * Represents the scope of bot commands, covering a specific member of a group or supergroup chat.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopechatmember): https://core.telegram.org/bots/api#botcommandscopechatmember
 * 
 * @param type Scope type, must be chat_member
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username. Channel direct messages chats and channel chats aren't supported.
 * @param userId Unique identifier of the target user
 */
@Serializable
data class BotCommandScopeChatMember(
    @SerialName("chat_id") val chatId: ChatId,
    @SerialName("user_id") val userId: Long
) : BotCommandScope {
    override val type: String = "chat_member"
}

