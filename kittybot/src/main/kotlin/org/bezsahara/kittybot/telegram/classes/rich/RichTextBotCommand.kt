package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A bot command.
 *
 * [link](https://core.telegram.org/bots/api#richtextbotcommand): https://core.telegram.org/bots/api#richtextbotcommand
 *
 * @param type Type of the rich text, always "bot_command"
 * @param text The text
 * @param botCommand The bot command
 */
@Serializable
data class RichTextBotCommand(
    val text: RichText,
    @SerialName("bot_command") val botCommand: String
) : RichText {
    override val type: String = "bot_command"
}
