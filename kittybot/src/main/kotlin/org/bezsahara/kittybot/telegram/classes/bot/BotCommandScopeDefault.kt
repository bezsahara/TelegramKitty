package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopedefault): https://core.telegram.org/bots/api#botcommandscopedefault
 * 
 * @param type Scope type, must be default
 */
@Serializable(with = BotCommandScopeDefaultJsonSerializer::class)
object BotCommandScopeDefault : BotCommandScope {
    override val type: String = "default"
}


internal class BotCommandScopeDefaultJsonSerializer : PureJsonSerializer<BotCommandScopeDefault>("BotCommandScopeDefault", BotCommandScopeDefault, buildJsonObject { put("type", JsonPrimitive("default")) })



