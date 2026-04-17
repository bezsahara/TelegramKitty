package org.bezsahara.kittybot.telegram.classes.keyboard

import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButtonRequestUsers
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButtonPollType
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo
import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButtonRequestChat
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButtonRequestManagedBot


/**
 * This object represents one button of the reply keyboard. At most one of the fields other than text, icon_custom_emoji_id, and style must be used to specify the type of the button. For simple text buttons, String can be used instead of this object to specify the button text.
 * 
 * [link](https://core.telegram.org/bots/api#keyboardbutton): https://core.telegram.org/bots/api#keyboardbutton
 * 
 * @param text Text of the button. If none of the fields other than text, icon_custom_emoji_id, and style are used, it will be sent as a message when the button is pressed
 * @param iconCustomEmojiId Optional. Unique identifier of the custom emoji shown before the text of the button. Can only be used by bots that purchased additional usernames on Fragment or in the messages directly sent by the bot to private, group and supergroup chats if the owner of the bot has a Telegram Premium subscription.
 * @param style Optional. Style of the button. Must be one of "danger" (red), "success" (green) or "primary" (blue). If omitted, then an app-specific style is used.
 * @param requestUsers Optional. If specified, pressing the button will open a list of suitable users. Identifiers of selected users will be sent to the bot in a "users_shared" service message. Available in private chats only.
 * @param requestChat Optional. If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier to the bot in a "chat_shared" service message. Available in private chats only.
 * @param requestManagedBot Optional. If specified, pressing the button will ask the user to create and share a bot that will be managed by the current bot. Available for bots that enabled management of other bots in the @BotFather Mini App. Available in private chats only.
 * @param requestContact Optional. If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only.
 * @param requestLocation Optional. If True, the user's current location will be sent when the button is pressed. Available in private chats only.
 * @param requestPoll Optional. If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. Available in private chats only.
 * @param webApp Optional. If specified, the described Web App will be launched when the button is pressed. The Web App will be able to send a "web_app_data" service message. Available in private chats only.
 */
@Serializable
data class KeyboardButton(
    val text: String,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null,
    val style: String? = null,
    @SerialName("request_users") val requestUsers: KeyboardButtonRequestUsers? = null,
    @SerialName("request_chat") val requestChat: KeyboardButtonRequestChat? = null,
    @SerialName("request_managed_bot") val requestManagedBot: KeyboardButtonRequestManagedBot? = null,
    @SerialName("request_contact") val requestContact: Boolean? = null,
    @SerialName("request_location") val requestLocation: Boolean? = null,
    @SerialName("request_poll") val requestPoll: KeyboardButtonPollType? = null,
    @SerialName("web_app") val webApp: WebAppInfo? = null
)

