package org.bezsahara.kittybot.telegram.classes.business

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.business.BusinessBotRights
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * Describes the connection of the bot with a business account.
 * 
 * [link](https://core.telegram.org/bots/api#businessconnection): https://core.telegram.org/bots/api#businessconnection
 * 
 * @param id Unique identifier of the business connection
 * @param user Business account user that created the business connection
 * @param userChatId Identifier of a private chat with the user who created the business connection. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param date Date the connection was established in Unix time
 * @param rights Optional. Rights of the business bot
 * @param isEnabled True, if the connection is active
 */
@Serializable
data class BusinessConnection(
    val id: String,
    val user: User,
    @SerialName("user_chat_id") val userChatId: Long,
    val date: Long,
    @SerialName("is_enabled") val isEnabled: Boolean,
    val rights: BusinessBotRights? = null
)

