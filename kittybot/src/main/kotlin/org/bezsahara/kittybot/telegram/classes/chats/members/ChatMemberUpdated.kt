package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.chats.links.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents changes in the status of a chat member.
 * 
 * *[link](https://core.telegram.org/bots/api#chatmemberupdated)*: https://core.telegram.org/bots/api#chatmemberupdated
 * 
 * @param chat Chat the user belongs to
 * @param from Performer of the action, which resulted in the change
 * @param date Date the change was done in Unix time
 * @param oldChatMember Previous information about the chat member
 * @param newChatMember New information about the chat member
 * @param inviteLink Optional. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
 * @param viaJoinRequest Optional. True, if the user joined the chat after sending a direct join request without using an invite link and being approved by an administrator
 * @param viaChatFolderInviteLink Optional. True, if the user joined the chat via a chat folder invite link
 */
@Serializable
data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Long,
    @SerialName("old_chat_member") val oldChatMember: ChatMember,
    @SerialName("new_chat_member") val newChatMember: ChatMember,
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null,
    @SerialName("via_join_request") val viaJoinRequest: Boolean? = null,
    @SerialName("via_chat_folder_invite_link") val viaChatFolderInviteLink: Boolean? = null
)

