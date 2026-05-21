package org.bezsahara.kittybot.telegram.classes.core

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * This object contains information about the bot that was created to be managed by the current bot.
 * 
 * [link](https://core.telegram.org/bots/api#managedbotcreated): https://core.telegram.org/bots/api#managedbotcreated
 * 
 * @param bot Information about the bot. The bot's token can be fetched using the method getManagedBotToken.
 */
@Serializable
data class ManagedBotCreated(
    val bot: User
)

