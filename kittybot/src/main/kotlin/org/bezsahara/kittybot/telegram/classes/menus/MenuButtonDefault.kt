package org.bezsahara.kittybot.telegram.classes.menus


import kotlinx.serialization.Serializable


/**
 * Describes that no specific value for the menu button was set.
 * 
 * *[link](https://core.telegram.org/bots/api#menubuttondefault)*: https://core.telegram.org/bots/api#menubuttondefault
 * 
 * @param type Type of the button, must be default
 */
@Serializable
object MenuButtonDefault : MenuButton {
    override val type: String = "default"
}

