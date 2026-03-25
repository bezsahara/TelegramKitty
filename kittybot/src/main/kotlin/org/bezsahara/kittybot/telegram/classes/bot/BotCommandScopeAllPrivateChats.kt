package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all private chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallprivatechats): https://core.telegram.org/bots/api#botcommandscopeallprivatechats
 * 
 * @param type Scope type, must be all_private_chats
 */
@Serializable
open class BotCommandScopeAllPrivateChats : BotCommandScope {
    override val type: String get() = "all_private_chats"
    companion object Default : BotCommandScopeAllPrivateChats()
}

