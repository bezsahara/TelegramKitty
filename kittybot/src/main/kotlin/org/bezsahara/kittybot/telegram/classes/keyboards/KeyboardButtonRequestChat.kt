package org.bezsahara.kittybot.telegram.classes.keyboards


import org.bezsahara.kittybot.telegram.classes.chats.admin.ChatAdministratorRights
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * This object defines the criteria used to request a suitable chat. Information about the selected chat will be shared with the bot when the corresponding button is pressed. The bot will be granted requested rights in the chat if appropriate. More about requesting chats: https://core.telegram.org/bots/features#chat-and-user-selection.
 * 
 * *[link](https://core.telegram.org/bots/api#keyboardbuttonrequestchat)*: https://core.telegram.org/bots/api#keyboardbuttonrequestchat
 * 
 * @param requestId Signed 32-bit identifier of the request, which will be received back in the ChatShared object. Must be unique within the message
 * @param chatIsChannel Pass True to request a channel chat, pass False to request a group or a supergroup chat.
 * @param chatIsForum Optional. Pass True to request a forum supergroup, pass False to request a non-forum chat. If not specified, no additional restrictions are applied.
 * @param chatHasUsername Optional. Pass True to request a supergroup or a channel with a username, pass False to request a chat without a username. If not specified, no additional restrictions are applied.
 * @param chatIsCreated Optional. Pass True to request a chat owned by the user. Otherwise, no additional restrictions are applied.
 * @param userAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of bot_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of user_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botIsMember Optional. Pass True to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
 * @param requestTitle Optional. Pass True to request the chat's title
 * @param requestUsername Optional. Pass True to request the chat's username
 * @param requestPhoto Optional. Pass True to request the chat's photo
 */
@Serializable
data class KeyboardButtonRequestChat(
    @SerialName("request_id") val requestId: Long,
    @SerialName("chat_is_channel") val chatIsChannel: Boolean,
    @SerialName("chat_is_forum") val chatIsForum: Boolean? = null,
    @SerialName("chat_has_username") val chatHasUsername: Boolean? = null,
    @SerialName("chat_is_created") val chatIsCreated: Boolean? = null,
    @SerialName("user_administrator_rights") val userAdministratorRights: ChatAdministratorRights? = null,
    @SerialName("bot_administrator_rights") val botAdministratorRights: ChatAdministratorRights? = null,
    @SerialName("bot_is_member") val botIsMember: Boolean? = null,
    @SerialName("request_title") val requestTitle: Boolean? = null,
    @SerialName("request_username") val requestUsername: Boolean? = null,
    @SerialName("request_photo") val requestPhoto: Boolean? = null
)

