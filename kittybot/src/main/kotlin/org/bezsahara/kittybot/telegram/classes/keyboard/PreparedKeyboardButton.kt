package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.Serializable


/**
 * Describes a keyboard button to be used by a user of a Mini App.
 * 
 * [link](https://core.telegram.org/bots/api#preparedkeyboardbutton): https://core.telegram.org/bots/api#preparedkeyboardbutton
 * 
 * @param id Unique identifier of the keyboard button
 */
@Serializable
data class PreparedKeyboardButton(
    val id: String
)

