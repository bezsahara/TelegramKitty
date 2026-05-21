package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallgroupchats): https://core.telegram.org/bots/api#botcommandscopeallgroupchats
 * 
 * @param type Scope type, must be all_group_chats
 */
@Serializable(with = BotCommandScopeAllGroupChatsJsonSerializer::class)
object BotCommandScopeAllGroupChats : BotCommandScope {
    override val type: String = "all_group_chats"
}


internal class BotCommandScopeAllGroupChatsJsonSerializer : PureJsonSerializer<BotCommandScopeAllGroupChats>("BotCommandScopeAllGroupChats", BotCommandScopeAllGroupChats, buildJsonObject { put("type", JsonPrimitive("all_group_chats")) })



