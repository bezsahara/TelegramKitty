package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo


/**
 * Represents a menu button, which launches a Web App.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttonwebapp): https://core.telegram.org/bots/api#menubuttonwebapp
 * 
 * @param type Type of the button, must be web_app
 * @param text Text on the button
 * @param webApp Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery. Alternatively, a t.me link to a Web App of the bot can be specified in the object instead of the Web App's URL, in which case the Web App will be opened as if the user pressed the link.
 */
@Serializable
data class MenuButtonWebApp(
    val text: String,
    @SerialName("web_app") val webApp: WebAppInfo
) : MenuButton {
    override val type: String = "web_app"
}

