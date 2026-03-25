package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo


/**
 * This object represents one button of the reply keyboard. At most one of the optional fields must be used to specify type of the button. For simple text buttons, String can be used instead of this object to specify the button text.
 * Note: request_users and request_chat options will only work in Telegram versions released after 3 February, 2023. Older clients will display unsupported message.
 * 
 * [link](https://core.telegram.org/bots/api#keyboardbutton): https://core.telegram.org/bots/api#keyboardbutton
 * 
 * @param text Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed
 * @param requestUsers Optional. If specified, pressing the button will open a list of suitable users. Identifiers of selected users will be sent to the bot in a "users_shared" service message. Available in private chats only.
 * @param requestChat Optional. If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier to the bot in a "chat_shared" service message. Available in private chats only.
 * @param requestContact Optional. If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only.
 * @param requestLocation Optional. If True, the user's current location will be sent when the button is pressed. Available in private chats only.
 * @param requestPoll Optional. If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. Available in private chats only.
 * @param webApp Optional. If specified, the described Web App will be launched when the button is pressed. The Web App will be able to send a "web_app_data" service message. Available in private chats only.
 */
@Serializable(with = KBSerializer::class)
sealed class KeyboardButton {
    abstract val text: String

    @SerialName("request_users") open val requestUsers: KeyboardButtonRequestUsers? get() = null
    @SerialName("request_chat") open val requestChat: KeyboardButtonRequestChat? get() = null
    @SerialName("request_contact") open val requestContact: Boolean? get() = null
    @SerialName("request_location") open val requestLocation: Boolean? get() = null
    @SerialName("request_poll") open val requestPoll: KeyboardButtonPollType? get() = null
    @SerialName("web_app") open val webApp: WebAppInfo? get() = null

    @Serializable
    data class Text(
        override val text: String
    ) : KeyboardButton()

    @Serializable
    data class RequestUsers(
        override val text: String,
        @SerialName("request_users") override val requestUsers: KeyboardButtonRequestUsers
    ) : KeyboardButton()

    @Serializable
    data class RequestChat(
        override val text: String,
        @SerialName("request_chat") override val requestChat: KeyboardButtonRequestChat
    ) : KeyboardButton()

    @Serializable
    data class RequestContact(
        override val text: String
    ) : KeyboardButton() {
        @SerialName("request_contact") override val requestContact: Boolean get() = true
    }

    @Serializable
    data class RequestLocation(
        override val text: String
    ) : KeyboardButton() {
        @SerialName("request_location") override val requestLocation: Boolean get() = true
    }

    @Serializable
    data class RequestPoll(
        override val text: String,
        @SerialName("request_poll") override val requestPoll: KeyboardButtonPollType
    ) : KeyboardButton()

    @Serializable
    data class WebApp(
        override val text: String,
        @SerialName("web_app") override val webApp: WebAppInfo
    ) : KeyboardButton()
}