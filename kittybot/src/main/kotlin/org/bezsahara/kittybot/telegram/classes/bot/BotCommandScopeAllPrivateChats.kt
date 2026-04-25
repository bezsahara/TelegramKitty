package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllPrivateChats
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.BotCommandScopeType
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all private chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallprivatechats): https://core.telegram.org/bots/api#botcommandscopeallprivatechats
 * 
 * @param type Scope type, must be all_private_chats
 */
@Serializable(with = BotCommandScopeAllPrivateChatsJsonSerializer::class)
object BotCommandScopeAllPrivateChats : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_PRIVATE_CHATS
}


internal class BotCommandScopeAllPrivateChatsJsonSerializer : PureJsonSerializer<BotCommandScopeAllPrivateChats>("BotCommandScopeAllPrivateChats", BotCommandScopeAllPrivateChats, buildJsonObject { put("type", JsonPrimitive("all_private_chats")) })



