package org.bezsahara.kittybot.telegram.classes.webapps


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes data sent from a Web App to the bot.
 * 
 * *[link](https://core.telegram.org/bots/api#webappdata)*: https://core.telegram.org/bots/api#webappdata
 * 
 * @param data The data. Be aware that a bad client can send arbitrary data in this field.
 * @param buttonText Text of the web_app keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field.
 */
@Serializable
data class WebAppData(
    val data: String,
    @SerialName("button_text") val buttonText: String
)

