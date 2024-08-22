package org.bezsahara.kittybot.telegram.classes.bots


import kotlinx.serialization.Serializable


/**
 * This object represents the bot's name.
 * 
 * *[link](https://core.telegram.org/bots/api#botname)*: https://core.telegram.org/bots/api#botname
 * 
 * @param name The bot's name
 */
@Serializable
data class BotName(
    val name: String
)

