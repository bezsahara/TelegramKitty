package org.bezsahara.kittybot.telegram.classes.permissions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 * 
 * *[link](https://core.telegram.org/bots/api#chatpermissions)*: https://core.telegram.org/bots/api#chatpermissions
 * 
 * @param canSendMessages Optional. True, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
 * @param canSendAudios Optional. True, if the user is allowed to send audios
 * @param canSendDocuments Optional. True, if the user is allowed to send documents
 * @param canSendPhotos Optional. True, if the user is allowed to send photos
 * @param canSendVideos Optional. True, if the user is allowed to send videos
 * @param canSendVideoNotes Optional. True, if the user is allowed to send video notes
 * @param canSendVoiceNotes Optional. True, if the user is allowed to send voice notes
 * @param canSendPolls Optional. True, if the user is allowed to send polls
 * @param canSendOtherMessages Optional. True, if the user is allowed to send animations, games, stickers and use inline bots
 * @param canAddWebPagePreviews Optional. True, if the user is allowed to add web page previews to their messages
 * @param canChangeInfo Optional. True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
 * @param canInviteUsers Optional. True, if the user is allowed to invite new users to the chat
 * @param canPinMessages Optional. True, if the user is allowed to pin messages. Ignored in public supergroups
 * @param canManageTopics Optional. True, if the user is allowed to create forum topics. If omitted defaults to the value of can_pin_messages
 */
@Serializable
data class ChatPermissions(
    @SerialName("can_send_messages") val canSendMessages: Boolean? = null,
    @SerialName("can_send_audios") val canSendAudios: Boolean? = null,
    @SerialName("can_send_documents") val canSendDocuments: Boolean? = null,
    @SerialName("can_send_photos") val canSendPhotos: Boolean? = null,
    @SerialName("can_send_videos") val canSendVideos: Boolean? = null,
    @SerialName("can_send_video_notes") val canSendVideoNotes: Boolean? = null,
    @SerialName("can_send_voice_notes") val canSendVoiceNotes: Boolean? = null,
    @SerialName("can_send_polls") val canSendPolls: Boolean? = null,
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean? = null,
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean? = null,
    @SerialName("can_change_info") val canChangeInfo: Boolean? = null,
    @SerialName("can_invite_users") val canInviteUsers: Boolean? = null,
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
    @SerialName("can_manage_topics") val canManageTopics: Boolean? = null
)

