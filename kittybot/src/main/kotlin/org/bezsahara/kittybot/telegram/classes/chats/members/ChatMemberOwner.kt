package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * Represents a chat member that owns the chat and has all administrator privileges.
 * 
 * *[link](https://core.telegram.org/bots/api#chatmemberowner)*: https://core.telegram.org/bots/api#chatmemberowner
 * 
 * @param status The member's status in the chat, always "creator"
 * @param user Information about the user
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param customTitle Optional. Custom title for this user
 */
@Serializable
data class ChatMemberOwner(
    val user: User,
    @SerialName("is_anonymous") val isAnonymous: Boolean,
    @SerialName("custom_title") val customTitle: String? = null,
) : ChatMember {
    override val status: String = "creator"
}

