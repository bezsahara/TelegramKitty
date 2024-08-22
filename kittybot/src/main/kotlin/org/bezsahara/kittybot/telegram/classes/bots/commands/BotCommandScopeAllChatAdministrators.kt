package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators)*: https://core.telegram.org/bots/api#botcommandscopeallchatadministrators
 * 
 * @param type Scope type, must be all_chat_administrators
 */
@Serializable
object BotCommandScopeAllChatAdministrators : BotCommandScope {
    override val type: String = "all_chat_administrators"
}

