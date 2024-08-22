package org.bezsahara.kittybot.telegram.classes.keyboards


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.games.CallbackGame
import org.bezsahara.kittybot.telegram.classes.queries.SwitchInlineQueryChosenChat
import org.bezsahara.kittybot.telegram.classes.urls.LoginUrl
import org.bezsahara.kittybot.telegram.classes.webapps.WebAppInfo


/**
 * This object represents one button of an inline keyboard. You must use exactly one of the optional fields.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinekeyboardbutton)*: https://core.telegram.org/bots/api#inlinekeyboardbutton
 * 
 * @param text Label text on the button
 * @param url Optional. HTTP or tg:// URL to be opened when the button is pressed. Links tg://user?id=<user_id> can be used to mention a user by their identifier without using a username, if this is allowed by their privacy settings.
 * @param callbackData Optional. Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes. Not supported for messages sent on behalf of a Telegram Business account.
 * @param webApp Optional. Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery. Available only in private chats between a user and the bot. Not supported for messages sent on behalf of a Telegram Business account.
 * @param loginUrl Optional. An HTTPS URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login Widget.
 * @param switchInlineQuery Optional. If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the bot's username and the specified inline query in the input field. May be empty, in which case just the bot's username will be inserted. Not supported for messages sent on behalf of a Telegram Business account.
 * @param switchInlineQueryCurrentChat Optional. If set, pressing the button will insert the bot's username and the specified inline query in the current chat's input field. May be empty, in which case only the bot's username will be inserted. This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting something from multiple options. Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * @param switchInlineQueryChosenChat Optional. If set, pressing the button will prompt the user to select one of their chats of the specified type, open that chat and insert the bot's username and the specified inline query in the input field. Not supported for messages sent on behalf of a Telegram Business account.
 * @param callbackGame Optional. Description of the game that will be launched when the user presses the button. NOTE: This type of button must always be the first button in the first row.
 * @param pay Optional. Specify True, to send a Pay button. NOTE: This type of button must always be the first button in the first row and can only be used in invoice messages.
 */
@Serializable
data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    @SerialName("callback_data") val callbackData: String? = null,
    @SerialName("web_app") val webApp: WebAppInfo? = null,
    @SerialName("login_url") val loginUrl: LoginUrl? = null,
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,
    @SerialName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,
    @SerialName("switch_inline_query_chosen_chat") val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,
    @SerialName("callback_game") val callbackGame: CallbackGame? = null,
    val pay: Boolean? = null
)

