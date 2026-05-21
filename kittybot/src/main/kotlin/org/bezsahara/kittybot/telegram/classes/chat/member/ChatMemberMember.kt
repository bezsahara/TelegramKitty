package org.bezsahara.kittybot.telegram.classes.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Represents a chat member that has no additional privileges or restrictions.
 * 
 * [link](https://core.telegram.org/bots/api#chatmembermember): https://core.telegram.org/bots/api#chatmembermember
 * 
 * @param status The member's status in the chat, always "member"
 * @param tag Optional. Tag of the member
 * @param user Information about the user
 * @param untilDate Optional. Date when the user's subscription will expire; Unix time
 */
@Serializable
data class ChatMemberMember(
    val user: User,
    val tag: String? = null,
    @SerialName("until_date") val untilDate: Long? = null
) : ChatMember {
    override val status: String = "member"
}

