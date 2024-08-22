package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.Serializable


/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopedefault)*: https://core.telegram.org/bots/api#botcommandscopedefault
 * 
 * @param type Scope type, must be default
 */
@Serializable
object BotCommandScopeDefault : BotCommandScope {
    override val type: String = "default"
}

