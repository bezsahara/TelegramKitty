package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The background is taken directly from a built-in chat theme.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundtypechattheme)*: https://core.telegram.org/bots/api#backgroundtypechattheme
 * 
 * @param type Type of the background, always "chat_theme"
 * @param themeName Name of the chat theme, which is usually an emoji
 */
@Serializable
data class BackgroundTypeChatTheme(
    @SerialName("theme_name") val themeName: String,
) : BackgroundType {
    override val type: String = "chat_theme"
}

