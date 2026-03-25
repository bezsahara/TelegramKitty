package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardButton
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup


/**
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 * 
 * [link](https://core.telegram.org/bots/api#inlinekeyboardmarkup): https://core.telegram.org/bots/api#inlinekeyboardmarkup
 * 
 * @param inlineKeyboard Array of button rows, each represented by an Array of InlineKeyboardButton objects
 */
@Serializable
data class InlineKeyboardMarkup(
    @SerialName("inline_keyboard") val inlineKeyboard: List<List<InlineKeyboardButton>>
) : ReplyMarkup

