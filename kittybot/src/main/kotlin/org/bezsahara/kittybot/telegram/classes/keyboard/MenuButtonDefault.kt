package org.bezsahara.kittybot.telegram.classes.keyboard

import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButtonDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.Serializable


/**
 * Describes that no specific value for the menu button was set.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttondefault): https://core.telegram.org/bots/api#menubuttondefault
 * 
 * @param type Type of the button, must be default
 */
@Serializable(with = MenuButtonDefaultJsonSerializer::class)
object MenuButtonDefault : MenuButton {
    override val type: String get() = "default"
}


internal class MenuButtonDefaultJsonSerializer : PureJsonSerializer<MenuButtonDefault>("MenuButtonDefault", MenuButtonDefault, buildJsonObject { put("type", JsonPrimitive("default")) })



