package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.Serializable


/**
 * This object represents an inline keyboard button that copies specified text to the clipboard.
 * 
 * [link](https://core.telegram.org/bots/api#copytextbutton): https://core.telegram.org/bots/api#copytextbutton
 * 
 * @param text The text to be copied to the clipboard; 1-256 characters
 */
@Serializable
data class CopyTextButton(
    val text: String
)

