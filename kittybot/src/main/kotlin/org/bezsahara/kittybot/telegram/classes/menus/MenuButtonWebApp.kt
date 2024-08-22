package org.bezsahara.kittybot.telegram.classes.menus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.webapps.WebAppInfo


/**
 * Represents a menu button, which launches a Web App.
 * 
 * *[link](https://core.telegram.org/bots/api#menubuttonwebapp)*: https://core.telegram.org/bots/api#menubuttonwebapp
 * 
 * @param type Type of the button, must be web_app
 * @param text Text on the button
 * @param webApp Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery.
 */
@Serializable
data class MenuButtonWebApp(
    val text: String,
    @SerialName("web_app") val webApp: WebAppInfo,
) : MenuButton {
    override val type: String = "web_app"
}

