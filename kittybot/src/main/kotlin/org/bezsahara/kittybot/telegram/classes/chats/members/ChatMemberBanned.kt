package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 * 
 * *[link](https://core.telegram.org/bots/api#chatmemberbanned)*: https://core.telegram.org/bots/api#chatmemberbanned
 * 
 * @param status The member's status in the chat, always "kicked"
 * @param user Information about the user
 * @param untilDate Date when restrictions will be lifted for this user; Unix time. If 0, then the user is banned forever
 */
@Serializable
data class ChatMemberBanned(
    val user: User,
    @SerialName("until_date") val untilDate: Long,
) : ChatMember {
    override val status: String = "kicked"
}

