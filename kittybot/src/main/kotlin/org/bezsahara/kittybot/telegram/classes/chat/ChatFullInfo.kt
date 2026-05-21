package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.business.BusinessIntro
import org.bezsahara.kittybot.telegram.classes.business.BusinessLocation
import org.bezsahara.kittybot.telegram.classes.business.BusinessOpeningHours
import org.bezsahara.kittybot.telegram.classes.gifts.AcceptedGiftTypes
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftColors
import org.bezsahara.kittybot.telegram.classes.media.Audio
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import org.bezsahara.kittybot.telegram.classes.user.Birthdate
import org.bezsahara.kittybot.telegram.classes.user.UserRating
import org.bezsahara.kittybot.telegram.values.ChatType


/**
 * This object contains full information about a chat.
 * 
 * [link](https://core.telegram.org/bots/api#chatfullinfo): https://core.telegram.org/bots/api#chatfullinfo
 * 
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of the chat, can be either "private", "group", "supergroup" or "channel"
 * @param title Optional. Title, for supergroups, channels and group chats
 * @param username Optional. Username, for private chats, supergroups and channels if available
 * @param firstName Optional. First name of the other party in a private chat
 * @param lastName Optional. Last name of the other party in a private chat
 * @param isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 * @param isDirectMessages Optional. True, if the chat is the direct messages chat of a channel
 * @param accentColorId Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link preview. See accent colors for more details.
 * @param maxReactionCount The maximum number of reactions that can be set on a message in the chat
 * @param photo Optional. Chat photo
 * @param activeUsernames Optional. If non-empty, the list of all active chat usernames; for private chats, supergroups and channels
 * @param birthdate Optional. For private chats, the date of birth of the user
 * @param businessIntro Optional. For private chats with business accounts, the intro of the business
 * @param businessLocation Optional. For private chats with business accounts, the location of the business
 * @param businessOpeningHours Optional. For private chats with business accounts, the opening hours of the business
 * @param personalChat Optional. For private chats, the personal channel of the user
 * @param parentChat Optional. Information about the corresponding channel chat; for direct messages chats only
 * @param availableReactions Optional. List of available reactions allowed in the chat. If omitted, then all emoji reactions are allowed.
 * @param backgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for the reply header and link preview background
 * @param profileAccentColorId Optional. Identifier of the accent color for the chat's profile background. See profile accent colors for more details.
 * @param profileBackgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for its profile background
 * @param emojiStatusCustomEmojiId Optional. Custom emoji identifier of the emoji status of the chat or the other party in a private chat
 * @param emojiStatusExpirationDate Optional. Expiration date of the emoji status of the chat or the other party in a private chat, in Unix time, if any
 * @param bio Optional. Bio of the other party in a private chat
 * @param hasPrivateForwards Optional. True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links only in chats with the user
 * @param hasRestrictedVoiceAndVideoMessages Optional. True, if the privacy settings of the other party restrict sending voice and video note messages in the private chat
 * @param joinToSendMessages Optional. True, if users need to join the supergroup before they can send messages
 * @param joinByRequest Optional. True, if all users directly joining the supergroup without using an invite link need to be approved by supergroup administrators
 * @param description Optional. Description, for groups, supergroups and channel chats
 * @param inviteLink Optional. Primary invite link, for groups, supergroups and channel chats
 * @param pinnedMessage Optional. The most recent pinned message (by sending date)
 * @param permissions Optional. Default chat member permissions, for groups and supergroups
 * @param acceptedGiftTypes Information about types of gifts that are accepted by the chat or by the corresponding user for private chats
 * @param canSendPaidMedia Optional. True, if paid media messages can be sent or forwarded to the channel chat. The field is available only for channel chats.
 * @param slowModeDelay Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unprivileged user; in seconds
 * @param unrestrictBoostCount Optional. For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore slow mode and chat permissions
 * @param messageAutoDeleteTime Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds
 * @param hasAggressiveAntiSpamEnabled Optional. True, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators.
 * @param hasHiddenMembers Optional. True, if non-administrators can only get the list of bots and administrators in the chat
 * @param hasProtectedContent Optional. True, if messages from the chat can't be forwarded to other chats
 * @param hasVisibleHistory Optional. True, if new chat members will have access to old messages; available only to chat administrators
 * @param stickerSetName Optional. For supergroups, name of the group sticker set
 * @param canSetStickerSet Optional. True, if the bot can change the group sticker set
 * @param customEmojiStickerSetName Optional. For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by all users and bots in the group.
 * @param linkedChatId Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
 * @param location Optional. For supergroups, the location to which the supergroup is connected
 * @param rating Optional. For private chats, the rating of the user if any
 * @param firstProfileAudio Optional. For private chats, the first audio added to the profile of the user
 * @param uniqueGiftColors Optional. The color scheme based on a unique gift that must be used for the chat's name, message replies and link previews
 * @param paidMessageStarCount Optional. The number of Telegram Stars a general user have to pay to send a message to the chat
 */
@Serializable
data class ChatFullInfo(
    val id: Long,
    val type: ChatType,
    @SerialName("accent_color_id") val accentColorId: Long,
    @SerialName("max_reaction_count") val maxReactionCount: Long,
    @SerialName("accepted_gift_types") val acceptedGiftTypes: AcceptedGiftTypes,
    val title: String? = null,
    val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("is_forum") val isForum: Boolean? = null,
    @SerialName("is_direct_messages") val isDirectMessages: Boolean? = null,
    val photo: ChatPhoto? = null,
    @SerialName("active_usernames") val activeUsernames: List<String>? = null,
    val birthdate: Birthdate? = null,
    @SerialName("business_intro") val businessIntro: BusinessIntro? = null,
    @SerialName("business_location") val businessLocation: BusinessLocation? = null,
    @SerialName("business_opening_hours") val businessOpeningHours: BusinessOpeningHours? = null,
    @SerialName("personal_chat") val personalChat: Chat? = null,
    @SerialName("parent_chat") val parentChat: Chat? = null,
    @SerialName("available_reactions") val availableReactions: List<ReactionType>? = null,
    @SerialName("background_custom_emoji_id") val backgroundCustomEmojiId: String? = null,
    @SerialName("profile_accent_color_id") val profileAccentColorId: Long? = null,
    @SerialName("profile_background_custom_emoji_id") val profileBackgroundCustomEmojiId: String? = null,
    @SerialName("emoji_status_custom_emoji_id") val emojiStatusCustomEmojiId: String? = null,
    @SerialName("emoji_status_expiration_date") val emojiStatusExpirationDate: Long? = null,
    val bio: String? = null,
    @SerialName("has_private_forwards") val hasPrivateForwards: Boolean? = null,
    @SerialName("has_restricted_voice_and_video_messages") val hasRestrictedVoiceAndVideoMessages: Boolean? = null,
    @SerialName("join_to_send_messages") val joinToSendMessages: Boolean? = null,
    @SerialName("join_by_request") val joinByRequest: Boolean? = null,
    val description: String? = null,
    @SerialName("invite_link") val inviteLink: String? = null,
    @SerialName("pinned_message") val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    @SerialName("can_send_paid_media") val canSendPaidMedia: Boolean? = null,
    @SerialName("slow_mode_delay") val slowModeDelay: Long? = null,
    @SerialName("unrestrict_boost_count") val unrestrictBoostCount: Long? = null,
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Long? = null,
    @SerialName("has_aggressive_anti_spam_enabled") val hasAggressiveAntiSpamEnabled: Boolean? = null,
    @SerialName("has_hidden_members") val hasHiddenMembers: Boolean? = null,
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,
    @SerialName("has_visible_history") val hasVisibleHistory: Boolean? = null,
    @SerialName("sticker_set_name") val stickerSetName: String? = null,
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null,
    @SerialName("custom_emoji_sticker_set_name") val customEmojiStickerSetName: String? = null,
    @SerialName("linked_chat_id") val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
    val rating: UserRating? = null,
    @SerialName("first_profile_audio") val firstProfileAudio: Audio? = null,
    @SerialName("unique_gift_colors") val uniqueGiftColors: UniqueGiftColors? = null,
    @SerialName("paid_message_star_count") val paidMessageStarCount: Long? = null
)

