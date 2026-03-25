package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators): https://core.telegram.org/bots/api#botcommandscopeallchatadministrators
 * 
 * @param type Scope type, must be all_chat_administrators
 */
@Serializable
open class BotCommandScopeAllChatAdministrators : BotCommandScope {
    override val type: String get() = "all_chat_administrators"
    companion object Default : BotCommandScopeAllChatAdministrators()
}

