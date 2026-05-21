package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * Represents a menu button, which opens the bot's list of commands.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttoncommands): https://core.telegram.org/bots/api#menubuttoncommands
 * 
 * @param type Type of the button, must be commands
 */
@Serializable(with = MenuButtonCommandsJsonSerializer::class)
object MenuButtonCommands : MenuButton {
    override val type: String = "commands"
}


internal class MenuButtonCommandsJsonSerializer : PureJsonSerializer<MenuButtonCommands>("MenuButtonCommands", MenuButtonCommands, buildJsonObject { put("type", JsonPrimitive("commands")) })



