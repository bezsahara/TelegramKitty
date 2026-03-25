package org.bezsahara.kittybot.telegram.classes.keyboard

import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes that no specific value for the menu button was set.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttondefault): https://core.telegram.org/bots/api#menubuttondefault
 * 
 * @param type Type of the button, must be default
 */
@Serializable
open class MenuButtonDefault : MenuButton {
    override val type: String get() = "default"
    companion object Default : MenuButtonDefault()
}

