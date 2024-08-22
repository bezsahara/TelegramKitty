package org.bezsahara.kittybot.telegram.classes.bots


import kotlinx.serialization.Serializable


/**
 * This object represents the bot's description.
 * 
 * *[link](https://core.telegram.org/bots/api#botdescription)*: https://core.telegram.org/bots/api#botdescription
 * 
 * @param description The bot's description
 */
@Serializable
data class BotDescription(
    val description: String
)

