package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators): https://core.telegram.org/bots/api#botcommandscopeallchatadministrators
 * 
 * @param type Scope type, must be all_chat_administrators
 */
@Serializable(with = BotCommandScopeAllChatAdministratorsJsonSerializer::class)
object BotCommandScopeAllChatAdministrators : BotCommandScope {
    override val type: String = "all_chat_administrators"
}


internal class BotCommandScopeAllChatAdministratorsJsonSerializer : PureJsonSerializer<BotCommandScopeAllChatAdministrators>("BotCommandScopeAllChatAdministrators", BotCommandScopeAllChatAdministrators, buildJsonObject { put("type", JsonPrimitive("all_chat_administrators")) })



