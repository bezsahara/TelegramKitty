package org.bezsahara.kittybot.telegram.classes.chat.member

import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * Represents a chat member that isn't currently a member of the chat, but may join it themselves.
 * 
 * [link](https://core.telegram.org/bots/api#chatmemberleft): https://core.telegram.org/bots/api#chatmemberleft
 * 
 * @param status The member's status in the chat, always "left"
 * @param user Information about the user
 */
@Serializable
data class ChatMemberLeft(
    val user: User
) : ChatMember {
    override val status: String = "left"
}

