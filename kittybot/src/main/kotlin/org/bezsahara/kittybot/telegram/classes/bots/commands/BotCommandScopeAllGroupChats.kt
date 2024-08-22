package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.Serializable


/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommandscopeallgroupchats)*: https://core.telegram.org/bots/api#botcommandscopeallgroupchats
 * 
 * @param type Scope type, must be all_group_chats
 */
@Serializable
object BotCommandScopeAllGroupChats : BotCommandScope {
    override val type: String = "all_group_chats"
}

