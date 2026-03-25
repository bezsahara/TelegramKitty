package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents the bot's name.
 * 
 * [link](https://core.telegram.org/bots/api#botname): https://core.telegram.org/bots/api#botname
 * 
 * @param name The bot's name
 */
@Serializable
data class BotName(
    val name: String
)

