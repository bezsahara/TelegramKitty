package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all private chats.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopeallprivatechats)*: https://core.telegram.org/bots/api#botcommandscopeallprivatechats
 * 
 * @param type Scope type, must be all_private_chats
 */
@Serializable
object BotCommandScopeAllPrivateChats : BotCommandScope {
    override val type: String = "all_private_chats"
}

