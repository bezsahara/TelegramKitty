package org.bezsahara.kittybot.telegram.classes.chats.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.chats.links.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * Represents a join request sent to a chat.
 * 
 * *[link](https://core.telegram.org/bots/api#chatjoinrequest)*: https://core.telegram.org/bots/api#chatjoinrequest
 * 
 * @param chat Chat to which the request was sent
 * @param from User that sent the join request
 * @param userChatId Identifier of a private chat with the user who sent the join request. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot can use this identifier for 5 minutes to send messages until the join request is processed, assuming no other administrator contacted the user.
 * @param date Date the request was sent in Unix time
 * @param bio Optional. Bio of the user.
 * @param inviteLink Optional. Chat invite link that was used by the user to send the join request
 */
@Serializable
data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    @SerialName("user_chat_id") val userChatId: Long,
    val date: Long,
    val bio: String? = null,
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null
)

