package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Represents the scope of bot commands, covering all private chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallprivatechats): https://core.telegram.org/bots/api#botcommandscopeallprivatechats
 * 
 * @param type Scope type, must be all_private_chats
 */
@Serializable(with = BotCommandScopeAllPrivateChatsJsonSerializer::class)
object BotCommandScopeAllPrivateChats : BotCommandScope {
    override val type: String = "all_private_chats"
}


internal class BotCommandScopeAllPrivateChatsJsonSerializer : PureJsonSerializer<BotCommandScopeAllPrivateChats>("BotCommandScopeAllPrivateChats", BotCommandScopeAllPrivateChats, buildJsonObject { put("type", JsonPrimitive("all_private_chats")) })



