package org.bezsahara.kittybot.telegram.classes.chats


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a chat.
 * 
 * *[link](https://core.telegram.org/bots/api#chat)*: https://core.telegram.org/bots/api#chat
 * 
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of the chat, can be either "private", "group", "supergroup" or "channel"
 * @param title Optional. Title, for supergroups, channels and group chats
 * @param username Optional. Username, for private chats, supergroups and channels if available
 * @param firstName Optional. First name of the other party in a private chat
 * @param lastName Optional. Last name of the other party in a private chat
 * @param isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 */
@Serializable
data class Chat(
    val id: Long,
    val type: String,
    val title: String? = null,
    val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("is_forum") val isForum: Boolean? = null
)

