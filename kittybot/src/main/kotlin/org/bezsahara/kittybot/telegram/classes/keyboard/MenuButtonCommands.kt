package org.bezsahara.kittybot.telegram.classes.keyboard

import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents a menu button, which opens the bot's list of commands.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttoncommands): https://core.telegram.org/bots/api#menubuttoncommands
 * 
 * @param type Type of the button, must be commands
 */
@Serializable
open class MenuButtonCommands : MenuButton {
    override val type: String get() = "commands"
    companion object Default : MenuButtonCommands()
}

