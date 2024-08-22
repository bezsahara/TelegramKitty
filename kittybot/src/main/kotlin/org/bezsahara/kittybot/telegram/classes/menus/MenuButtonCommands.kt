package org.bezsahara.kittybot.telegram.classes.menus


import kotlinx.serialization.Serializable


/**
 * Represents a menu button, which opens the bot's list of commands.
 * 
 * *[link](https://core.telegram.org/bots/api#menubuttoncommands)*: https://core.telegram.org/bots/api#menubuttoncommands
 * 
 * @param type Type of the button, must be commands
 */
@Serializable
object MenuButtonCommands : MenuButton {
    override val type: String = "commands"
}

