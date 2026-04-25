package org.bezsahara.kittybot.telegram.classes.bot

import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.BotCommandScopeType
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllGroupChats
import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallgroupchats): https://core.telegram.org/bots/api#botcommandscopeallgroupchats
 * 
 * @param type Scope type, must be all_group_chats
 */
@Serializable(with = BotCommandScopeAllGroupChatsJsonSerializer::class)
object BotCommandScopeAllGroupChats : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_GROUP_CHATS
}


internal class BotCommandScopeAllGroupChatsJsonSerializer : PureJsonSerializer<BotCommandScopeAllGroupChats>("BotCommandScopeAllGroupChats", BotCommandScopeAllGroupChats, buildJsonObject { put("type", JsonPrimitive("all_group_chats")) })



