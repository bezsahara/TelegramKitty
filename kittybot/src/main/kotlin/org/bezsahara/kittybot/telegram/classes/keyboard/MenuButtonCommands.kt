package org.bezsahara.kittybot.telegram.classes.keyboard

import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButtonCommands
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.values.MenuButtonType


/**
 * Represents a menu button, which opens the bot's list of commands.
 * 
 * [link](https://core.telegram.org/bots/api#menubuttoncommands): https://core.telegram.org/bots/api#menubuttoncommands
 * 
 * @param type Type of the button, must be commands
 */
@Serializable(with = MenuButtonCommandsJsonSerializer::class)
object MenuButtonCommands : MenuButton {
    override val type: MenuButtonType = MenuButtonType.COMMANDS
}


internal class MenuButtonCommandsJsonSerializer : PureJsonSerializer<MenuButtonCommands>("MenuButtonCommands", MenuButtonCommands, buildJsonObject { put("type", JsonPrimitive("commands")) })



