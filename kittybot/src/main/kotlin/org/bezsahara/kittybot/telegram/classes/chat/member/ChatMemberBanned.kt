package org.bezsahara.kittybot.telegram.classes.chat.member

import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 * 
 * [link](https://core.telegram.org/bots/api#chatmemberbanned): https://core.telegram.org/bots/api#chatmemberbanned
 * 
 * @param status The member's status in the chat, always "kicked"
 * @param user Information about the user
 * @param untilDate Date when restrictions will be lifted for this user; Unix time. If 0, then the user is banned forever.
 */
@Serializable
data class ChatMemberBanned(
    val user: User,
    @SerialName("until_date") val untilDate: Long
) : ChatMember {
    override val status: String = "kicked"
}

