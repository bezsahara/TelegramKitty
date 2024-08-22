package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * Represents a chat member that is under certain restrictions in the chat. Supergroups only.
 * 
 * *[link](https://core.telegram.org/bots/api#chatmemberrestricted)*: https://core.telegram.org/bots/api#chatmemberrestricted
 * 
 * @param status The member's status in the chat, always "restricted"
 * @param user Information about the user
 * @param isMember True, if the user is a member of the chat at the moment of the request
 * @param canSendMessages True, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
 * @param canSendAudios True, if the user is allowed to send audios
 * @param canSendDocuments True, if the user is allowed to send documents
 * @param canSendPhotos True, if the user is allowed to send photos
 * @param canSendVideos True, if the user is allowed to send videos
 * @param canSendVideoNotes True, if the user is allowed to send video notes
 * @param canSendVoiceNotes True, if the user is allowed to send voice notes
 * @param canSendPolls True, if the user is allowed to send polls
 * @param canSendOtherMessages True, if the user is allowed to send animations, games, stickers and use inline bots
 * @param canAddWebPagePreviews True, if the user is allowed to add web page previews to their messages
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPinMessages True, if the user is allowed to pin messages
 * @param canManageTopics True, if the user is allowed to create forum topics
 * @param untilDate Date when restrictions will be lifted for this user; Unix time. If 0, then the user is restricted forever
 */
@Serializable
data class ChatMemberRestricted(
    val user: User,
    @SerialName("is_member") val isMember: Boolean,
    @SerialName("can_send_messages") val canSendMessages: Boolean,
    @SerialName("can_send_audios") val canSendAudios: Boolean,
    @SerialName("can_send_documents") val canSendDocuments: Boolean,
    @SerialName("can_send_photos") val canSendPhotos: Boolean,
    @SerialName("can_send_videos") val canSendVideos: Boolean,
    @SerialName("can_send_video_notes") val canSendVideoNotes: Boolean,
    @SerialName("can_send_voice_notes") val canSendVoiceNotes: Boolean,
    @SerialName("can_send_polls") val canSendPolls: Boolean,
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean,
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean,
    @SerialName("can_change_info") val canChangeInfo: Boolean,
    @SerialName("can_invite_users") val canInviteUsers: Boolean,
    @SerialName("can_pin_messages") val canPinMessages: Boolean,
    @SerialName("can_manage_topics") val canManageTopics: Boolean,
    @SerialName("until_date") val untilDate: Long,
) : ChatMember {
    override val status: String = "restricted"
}

