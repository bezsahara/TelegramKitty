package org.bezsahara.kittybot.telegram.classes.core

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * This object contains information about the creation, token update, or owner update of a bot that is managed by the current bot.
 * 
 * [link](https://core.telegram.org/bots/api#managedbotupdated): https://core.telegram.org/bots/api#managedbotupdated
 * 
 * @param user User that created the bot
 * @param bot Information about the bot. Token of the bot can be fetched using the method getManagedBotToken.
 */
@Serializable
data class ManagedBotUpdated(
    val user: User,
    val bot: User
)

