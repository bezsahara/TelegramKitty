package org.bezsahara.kittybot.telegram.classes.chat.member

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.user.User


internal object ChatMemberSerializer : KSerializer<ChatMember> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ChatMember") {
        element<String>("status")
        element<User>("user")
        element<Boolean>("is_anonymous", isOptional = true)
        element<String?>("custom_title", isOptional = true)
        element<Boolean>("can_be_edited", isOptional = true)
        element<Boolean>("can_manage_chat", isOptional = true)
        element<Boolean>("can_delete_messages", isOptional = true)
        element<Boolean>("can_manage_video_chats", isOptional = true)
        element<Boolean>("can_restrict_members", isOptional = true)
        element<Boolean>("can_promote_members", isOptional = true)
        element<Boolean>("can_change_info", isOptional = true)
        element<Boolean>("can_invite_users", isOptional = true)
        element<Boolean>("can_post_stories", isOptional = true)
        element<Boolean>("can_edit_stories", isOptional = true)
        element<Boolean>("can_delete_stories", isOptional = true)
        element<Boolean?>("can_post_messages", isOptional = true)
        element<Boolean?>("can_edit_messages", isOptional = true)
        element<Boolean?>("can_pin_messages", isOptional = true)
        element<Boolean?>("can_manage_topics", isOptional = true)
        element<Boolean?>("can_manage_direct_messages", isOptional = true)
        element<Boolean?>("can_manage_tags", isOptional = true)
        element<String?>("tag", isOptional = true)
        element<Long?>("until_date", isOptional = true)
        element<Boolean>("is_member", isOptional = true)
        element<Boolean>("can_send_messages", isOptional = true)
        element<Boolean>("can_send_audios", isOptional = true)
        element<Boolean>("can_send_documents", isOptional = true)
        element<Boolean>("can_send_photos", isOptional = true)
        element<Boolean>("can_send_videos", isOptional = true)
        element<Boolean>("can_send_video_notes", isOptional = true)
        element<Boolean>("can_send_voice_notes", isOptional = true)
        element<Boolean>("can_send_polls", isOptional = true)
        element<Boolean>("can_send_other_messages", isOptional = true)
        element<Boolean>("can_add_web_page_previews", isOptional = true)
        element<Boolean>("can_react_to_messages", isOptional = true)
        element<Boolean>("can_edit_tag", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ChatMember) {
        when (value) {
            is ChatMemberOwner -> ChatMemberOwner.serializer().serialize(encoder, value)
            is ChatMemberAdministrator -> ChatMemberAdministrator.serializer().serialize(encoder, value)
            is ChatMemberMember -> ChatMemberMember.serializer().serialize(encoder, value)
            is ChatMemberRestricted -> ChatMemberRestricted.serializer().serialize(encoder, value)
            is ChatMemberLeft -> ChatMemberLeft.serializer().serialize(encoder, value)
            is ChatMemberBanned -> ChatMemberBanned.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): ChatMember {
        var nativeMask = IntMask.EMPTY
        var status: String? = null
        var user: User? = null
        var isAnonymous: Boolean = false
        var customTitle: String? = null
        var canBeEdited: Boolean = false
        var canManageChat: Boolean = false
        var canDeleteMessages: Boolean = false
        var canManageVideoChats: Boolean = false
        var canRestrictMembers: Boolean = false
        var canPromoteMembers: Boolean = false
        var canChangeInfo: Boolean = false
        var canInviteUsers: Boolean = false
        var canPostStories: Boolean = false
        var canEditStories: Boolean = false
        var canDeleteStories: Boolean = false
        var canPostMessages: Boolean? = null
        var canEditMessages: Boolean? = null
        var canPinMessages: Boolean? = null
        var canManageTopics: Boolean? = null
        var canManageDirectMessages: Boolean? = null
        var canManageTags: Boolean? = null
        var tag: String? = null
        var untilDate: Long? = null
        var isMember: Boolean = false
        var canSendMessages: Boolean = false
        var canSendAudios: Boolean = false
        var canSendDocuments: Boolean = false
        var canSendPhotos: Boolean = false
        var canSendVideos: Boolean = false
        var canSendVideoNotes: Boolean = false
        var canSendVoiceNotes: Boolean = false
        var canSendPolls: Boolean = false
        var canSendOtherMessages: Boolean = false
        var canAddWebPagePreviews: Boolean = false
        var canReactToMessages: Boolean = false
        var canEditTag: Boolean = false
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> status = dec.decodeStringElement(descriptor, 0)
                1 -> user = dec.decodeSerializableElement(descriptor, 1, User.serializer())
                2 -> {
                    isAnonymous = dec.decodeBooleanElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(0)
                }
                3 -> customTitle = dec.decodeNullableSerializableElement(descriptor, 3, String.serializer(), null)
                4 -> {
                    canBeEdited = dec.decodeBooleanElement(descriptor, 4)
                    nativeMask = nativeMask.setBit(1)
                }
                5 -> {
                    canManageChat = dec.decodeBooleanElement(descriptor, 5)
                    nativeMask = nativeMask.setBit(2)
                }
                6 -> {
                    canDeleteMessages = dec.decodeBooleanElement(descriptor, 6)
                    nativeMask = nativeMask.setBit(3)
                }
                7 -> {
                    canManageVideoChats = dec.decodeBooleanElement(descriptor, 7)
                    nativeMask = nativeMask.setBit(4)
                }
                8 -> {
                    canRestrictMembers = dec.decodeBooleanElement(descriptor, 8)
                    nativeMask = nativeMask.setBit(5)
                }
                9 -> {
                    canPromoteMembers = dec.decodeBooleanElement(descriptor, 9)
                    nativeMask = nativeMask.setBit(6)
                }
                10 -> {
                    canChangeInfo = dec.decodeBooleanElement(descriptor, 10)
                    nativeMask = nativeMask.setBit(7)
                }
                11 -> {
                    canInviteUsers = dec.decodeBooleanElement(descriptor, 11)
                    nativeMask = nativeMask.setBit(8)
                }
                12 -> {
                    canPostStories = dec.decodeBooleanElement(descriptor, 12)
                    nativeMask = nativeMask.setBit(9)
                }
                13 -> {
                    canEditStories = dec.decodeBooleanElement(descriptor, 13)
                    nativeMask = nativeMask.setBit(10)
                }
                14 -> {
                    canDeleteStories = dec.decodeBooleanElement(descriptor, 14)
                    nativeMask = nativeMask.setBit(11)
                }
                15 -> canPostMessages = dec.decodeNullableSerializableElement(descriptor, 15, Boolean.serializer(), null)
                16 -> canEditMessages = dec.decodeNullableSerializableElement(descriptor, 16, Boolean.serializer(), null)
                17 -> canPinMessages = dec.decodeNullableSerializableElement(descriptor, 17, Boolean.serializer(), null)
                18 -> canManageTopics = dec.decodeNullableSerializableElement(descriptor, 18, Boolean.serializer(), null)
                19 -> canManageDirectMessages = dec.decodeNullableSerializableElement(descriptor, 19, Boolean.serializer(), null)
                20 -> canManageTags = dec.decodeNullableSerializableElement(descriptor, 20, Boolean.serializer(), null)
                21 -> tag = dec.decodeNullableSerializableElement(descriptor, 21, String.serializer(), null)
                22 -> untilDate = dec.decodeNullableSerializableElement(descriptor, 22, Long.serializer(), null)
                23 -> {
                    isMember = dec.decodeBooleanElement(descriptor, 23)
                    nativeMask = nativeMask.setBit(12)
                }
                24 -> {
                    canSendMessages = dec.decodeBooleanElement(descriptor, 24)
                    nativeMask = nativeMask.setBit(13)
                }
                25 -> {
                    canSendAudios = dec.decodeBooleanElement(descriptor, 25)
                    nativeMask = nativeMask.setBit(14)
                }
                26 -> {
                    canSendDocuments = dec.decodeBooleanElement(descriptor, 26)
                    nativeMask = nativeMask.setBit(15)
                }
                27 -> {
                    canSendPhotos = dec.decodeBooleanElement(descriptor, 27)
                    nativeMask = nativeMask.setBit(16)
                }
                28 -> {
                    canSendVideos = dec.decodeBooleanElement(descriptor, 28)
                    nativeMask = nativeMask.setBit(17)
                }
                29 -> {
                    canSendVideoNotes = dec.decodeBooleanElement(descriptor, 29)
                    nativeMask = nativeMask.setBit(18)
                }
                30 -> {
                    canSendVoiceNotes = dec.decodeBooleanElement(descriptor, 30)
                    nativeMask = nativeMask.setBit(19)
                }
                31 -> {
                    canSendPolls = dec.decodeBooleanElement(descriptor, 31)
                    nativeMask = nativeMask.setBit(20)
                }
                32 -> {
                    canSendOtherMessages = dec.decodeBooleanElement(descriptor, 32)
                    nativeMask = nativeMask.setBit(21)
                }
                33 -> {
                    canAddWebPagePreviews = dec.decodeBooleanElement(descriptor, 33)
                    nativeMask = nativeMask.setBit(22)
                }
                34 -> {
                    canReactToMessages = dec.decodeBooleanElement(descriptor, 34)
                    nativeMask = nativeMask.setBit(23)
                }
                35 -> {
                    canEditTag = dec.decodeBooleanElement(descriptor, 35)
                    nativeMask = nativeMask.setBit(24)
                }
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing ChatMember")
            }
        }
        dec.endStructure(descriptor)
        return when (status ?: throwMissingField("status")) {
            "creator" -> ChatMemberOwner(
                user = user ?: throwMissingField("user"),
                isAnonymous = if (nativeMask.has(0)) isAnonymous else throwMissingField("is_anonymous"),
                customTitle = customTitle
            )
            "administrator" -> ChatMemberAdministrator(
                user = user ?: throwMissingField("user"),
                canBeEdited = if (nativeMask.has(1)) canBeEdited else throwMissingField("can_be_edited"),
                isAnonymous = if (nativeMask.has(0)) isAnonymous else throwMissingField("is_anonymous"),
                canManageChat = if (nativeMask.has(2)) canManageChat else throwMissingField("can_manage_chat"),
                canDeleteMessages = if (nativeMask.has(3)) canDeleteMessages else throwMissingField("can_delete_messages"),
                canManageVideoChats = if (nativeMask.has(4)) canManageVideoChats else throwMissingField("can_manage_video_chats"),
                canRestrictMembers = if (nativeMask.has(5)) canRestrictMembers else throwMissingField("can_restrict_members"),
                canPromoteMembers = if (nativeMask.has(6)) canPromoteMembers else throwMissingField("can_promote_members"),
                canChangeInfo = if (nativeMask.has(7)) canChangeInfo else throwMissingField("can_change_info"),
                canInviteUsers = if (nativeMask.has(8)) canInviteUsers else throwMissingField("can_invite_users"),
                canPostStories = if (nativeMask.has(9)) canPostStories else throwMissingField("can_post_stories"),
                canEditStories = if (nativeMask.has(10)) canEditStories else throwMissingField("can_edit_stories"),
                canDeleteStories = if (nativeMask.has(11)) canDeleteStories else throwMissingField("can_delete_stories"),
                canPostMessages = canPostMessages,
                canEditMessages = canEditMessages,
                canPinMessages = canPinMessages,
                canManageTopics = canManageTopics,
                canManageDirectMessages = canManageDirectMessages,
                canManageTags = canManageTags,
                customTitle = customTitle
            )
            "member" -> ChatMemberMember(
                tag = tag,
                user = user ?: throwMissingField("user"),
                untilDate = untilDate
            )
            "restricted" -> ChatMemberRestricted(
                tag = tag,
                user = user ?: throwMissingField("user"),
                isMember = if (nativeMask.has(12)) isMember else throwMissingField("is_member"),
                canSendMessages = if (nativeMask.has(13)) canSendMessages else throwMissingField("can_send_messages"),
                canSendAudios = if (nativeMask.has(14)) canSendAudios else throwMissingField("can_send_audios"),
                canSendDocuments = if (nativeMask.has(15)) canSendDocuments else throwMissingField("can_send_documents"),
                canSendPhotos = if (nativeMask.has(16)) canSendPhotos else throwMissingField("can_send_photos"),
                canSendVideos = if (nativeMask.has(17)) canSendVideos else throwMissingField("can_send_videos"),
                canSendVideoNotes = if (nativeMask.has(18)) canSendVideoNotes else throwMissingField("can_send_video_notes"),
                canSendVoiceNotes = if (nativeMask.has(19)) canSendVoiceNotes else throwMissingField("can_send_voice_notes"),
                canSendPolls = if (nativeMask.has(20)) canSendPolls else throwMissingField("can_send_polls"),
                canSendOtherMessages = if (nativeMask.has(21)) canSendOtherMessages else throwMissingField("can_send_other_messages"),
                canAddWebPagePreviews = if (nativeMask.has(22)) canAddWebPagePreviews else throwMissingField("can_add_web_page_previews"),
                canReactToMessages = if (nativeMask.has(23)) canReactToMessages else throwMissingField("can_react_to_messages"),
                canEditTag = if (nativeMask.has(24)) canEditTag else throwMissingField("can_edit_tag"),
                canChangeInfo = if (nativeMask.has(7)) canChangeInfo else throwMissingField("can_change_info"),
                canInviteUsers = if (nativeMask.has(8)) canInviteUsers else throwMissingField("can_invite_users"),
                canPinMessages = canPinMessages ?: throwMissingField("can_pin_messages"),
                canManageTopics = canManageTopics ?: throwMissingField("can_manage_topics"),
                untilDate = untilDate ?: throwMissingField("until_date")
            )
            "left" -> ChatMemberLeft(
                user = user ?: throwMissingField("user")
            )
            "kicked" -> ChatMemberBanned(
                user = user ?: throwMissingField("user"),
                untilDate = untilDate ?: throwMissingField("until_date")
            )
            else -> throw SerializationException("Serializer wasn't found for ChatMember with status $status")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing ChatMember")
    }
}
