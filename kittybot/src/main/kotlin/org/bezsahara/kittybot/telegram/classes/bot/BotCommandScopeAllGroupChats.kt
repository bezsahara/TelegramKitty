package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 * 
 * [link](https://core.telegram.org/bots/api#botcommandscopeallgroupchats): https://core.telegram.org/bots/api#botcommandscopeallgroupchats
 * 
 * @param type Scope type, must be all_group_chats
 */
@Serializable
open class BotCommandScopeAllGroupChats : BotCommandScope {
    override val type: String get() = "all_group_chats"
    companion object Default : BotCommandScopeAllGroupChats()
}

