package org.bezsahara.kittybot.telegram.classes.chat.member

import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * Represents a chat member that has no additional privileges or restrictions.
 * 
 * [link](https://core.telegram.org/bots/api#chatmembermember): https://core.telegram.org/bots/api#chatmembermember
 * 
 * @param status The member's status in the chat, always "member"
 * @param user Information about the user
 * @param untilDate Optional. Date when the user's subscription will expire; Unix time
 */
@Serializable
data class ChatMemberMember(
    val user: User,
    @SerialName("until_date") val untilDate: Long? = null
) : ChatMember {
    override val status: String = "member"
}

