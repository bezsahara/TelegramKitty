package org.bezsahara.kittybot.telegram.classes.bots


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents the bot's short description.
 * 
 * *[link](https://core.telegram.org/bots/api#botshortdescription)*: https://core.telegram.org/bots/api#botshortdescription
 * 
 * @param shortDescription The bot's short description
 */
@Serializable
data class BotShortDescription(
    @SerialName("short_description") val shortDescription: String
)

