package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable


/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopedefault): https://core.telegram.org/bots/api#botcommandscopedefault
 * 
 * @param type Scope type, must be default
 */
@Serializable
open class BotCommandScopeDefault : BotCommandScope {
    override val type: String get() = "default"
    companion object Default : BotCommandScopeDefault()
}

