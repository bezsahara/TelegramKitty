package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.Serializable


/**
 * This object represents a bot command.
 * 
 * *[link](https://core.telegram.org/bots/api#botcommand)*: https://core.telegram.org/bots/api#botcommand
 * 
 * @param command Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
 * @param description Description of the command; 1-256 characters.
 */
@Serializable
data class BotCommand(
    val command: String,
    val description: String
)

