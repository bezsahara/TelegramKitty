package org.bezsahara.kittybot.telegram.classes.keyboards


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinekeyboardmarkup)*: https://core.telegram.org/bots/api#inlinekeyboardmarkup
 * 
 * @param inlineKeyboard Array of button rows, each represented by an Array of InlineKeyboardButton objects
 */
@Serializable
data class InlineKeyboardMarkup(
    @SerialName("inline_keyboard") val inlineKeyboard: List<List<InlineKeyboardButton>>
) : ReplyMarkup

