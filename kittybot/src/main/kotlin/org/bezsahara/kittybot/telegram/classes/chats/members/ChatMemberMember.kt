package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * Represents a chat member that has no additional privileges or restrictions.
 * 
 * *[link](https://core.telegram.org/bots/api#chatmembermember)*: https://core.telegram.org/bots/api#chatmembermember
 * 
 * @param status The member's status in the chat, always "member"
 * @param user Information about the user
 */
@Serializable
data class ChatMemberMember(
    val user: User,
) : ChatMember {
    override val status: String = "member"
}

