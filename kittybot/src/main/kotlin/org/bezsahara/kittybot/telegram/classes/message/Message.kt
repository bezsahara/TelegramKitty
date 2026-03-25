package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.TextQuote
import org.bezsahara.kittybot.telegram.classes.media.PaidMediaInfo
import org.bezsahara.kittybot.telegram.classes.media.Animation
import org.bezsahara.kittybot.telegram.classes.message.service.ChecklistTasksDone
import org.bezsahara.kittybot.telegram.classes.message.service.MessageAutoDeleteTimerChanged
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.media.Voice
import org.bezsahara.kittybot.telegram.classes.message.service.DirectMessagePriceChanged
import org.bezsahara.kittybot.telegram.classes.passport.PassportData
import org.bezsahara.kittybot.telegram.classes.message.service.ProximityAlertTriggered
import org.bezsahara.kittybot.telegram.classes.message.service.ForumTopicReopened
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostInfo
import org.bezsahara.kittybot.telegram.classes.chat.background.ChatBackground
import org.bezsahara.kittybot.telegram.classes.message.service.SuggestedPostRefunded
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.payments.RefundedPayment
import org.bezsahara.kittybot.telegram.classes.media.Contact
import org.bezsahara.kittybot.telegram.classes.message.service.GiveawayCreated
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.ExternalReplyInfo
import org.bezsahara.kittybot.telegram.classes.message.service.ChatBoostAdded
import org.bezsahara.kittybot.telegram.classes.message.service.SuggestedPostPaid
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.keyboard.ChatShared
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftInfo
import org.bezsahara.kittybot.telegram.classes.message.MessageOrigin
import org.bezsahara.kittybot.telegram.classes.gifts.GiftInfo
import org.bezsahara.kittybot.telegram.classes.message.service.GeneralForumTopicUnhidden
import org.bezsahara.kittybot.telegram.classes.message.service.SuggestedPostApprovalFailed
import org.bezsahara.kittybot.telegram.classes.message.service.SuggestedPostDeclined
import org.bezsahara.kittybot.telegram.classes.message.service.VideoChatScheduled
import org.bezsahara.kittybot.telegram.classes.media.Video
import org.bezsahara.kittybot.telegram.classes.media.Dice
import org.bezsahara.kittybot.telegram.classes.message.service.ForumTopicCreated
import org.bezsahara.kittybot.telegram.classes.message.checklists.Checklist
import org.bezsahara.kittybot.telegram.classes.message.service.PaidMessagePriceChanged
import org.bezsahara.kittybot.telegram.classes.message.service.ForumTopicClosed
import org.bezsahara.kittybot.telegram.classes.message.service.Giveaway
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.classes.media.VideoNote
import org.bezsahara.kittybot.telegram.classes.payments.Invoice
import org.bezsahara.kittybot.telegram.classes.payments.SuccessfulPayment
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.chat.DirectMessagesTopic
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import org.bezsahara.kittybot.telegram.classes.message.service.SuggestedPostApproved
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.message.service.WriteAccessAllowed
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppData
import org.bezsahara.kittybot.telegram.classes.keyboard.UsersShared
import org.bezsahara.kittybot.telegram.classes.message.service.GiveawayWinners
import org.bezsahara.kittybot.telegram.classes.games.Game
import org.bezsahara.kittybot.telegram.classes.message.service.VideoChatEnded
import org.bezsahara.kittybot.telegram.classes.message.service.GeneralForumTopicHidden
import org.bezsahara.kittybot.telegram.classes.message.service.VideoChatStarted
import org.bezsahara.kittybot.telegram.classes.media.geo.Venue
import org.bezsahara.kittybot.telegram.classes.message.service.ForumTopicEdited
import org.bezsahara.kittybot.telegram.classes.media.Audio
import org.bezsahara.kittybot.telegram.classes.message.service.ChecklistTasksAdded
import org.bezsahara.kittybot.telegram.classes.media.Document
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import org.bezsahara.kittybot.telegram.classes.message.service.GiveawayCompleted
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.message.service.VideoChatParticipantsInvited


/**
 * This object represents a message.
 * 
 * [link](https://core.telegram.org/bots/api#message): https://core.telegram.org/bots/api#message
 * 
 * @param messageId Unique message identifier inside this chat. In specific instances (e.g., message containing a video sent to a big chat), the server might automatically schedule a message instead of sending it immediately. In such cases, this field will be 0 and the relevant message will be unusable until it is actually sent
 * @param messageThreadId Optional. Unique identifier of a message thread to which the message belongs; for supergroups only
 * @param directMessagesTopic Optional. Information about the direct messages chat topic that contains the message
 * @param from Optional. Sender of the message; may be empty for messages sent to channels. For backward compatibility, if the message was sent on behalf of a chat, the field contains a fake sender user in non-channel chats
 * @param senderChat Optional. Sender of the message when sent on behalf of a chat. For example, the supergroup itself for messages sent by its anonymous administrators or a linked channel for messages automatically forwarded to the channel's discussion group. For backward compatibility, if the message was sent on behalf of a chat, the field from contains a fake sender user in non-channel chats.
 * @param senderBoostCount Optional. If the sender of the message boosted the chat, the number of boosts added by the user
 * @param senderBusinessBot Optional. The bot that actually sent the message on behalf of the business account. Available only for outgoing messages sent on behalf of the connected business account.
 * @param date Date the message was sent in Unix time. It is always a positive number, representing a valid date.
 * @param businessConnectionId Optional. Unique identifier of the business connection from which the message was received. If non-empty, the message belongs to a chat of the corresponding business account that is independent from any potential bot chat which might share the same identifier.
 * @param chat Chat the message belongs to
 * @param forwardOrigin Optional. Information about the original message for forwarded messages
 * @param isTopicMessage Optional. True, if the message is sent to a forum topic
 * @param isAutomaticForward Optional. True, if the message is a channel post that was automatically forwarded to the connected discussion group
 * @param replyToMessage Optional. For replies in the same chat and message thread, the original message. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @param externalReply Optional. Information about the message that is being replied to, which may come from another chat or forum topic
 * @param quote Optional. For replies that quote part of the original message, the quoted part of the message
 * @param replyToStory Optional. For replies to a story, the original story
 * @param replyToChecklistTaskId Optional. Identifier of the specific checklist task that is being replied to
 * @param viaBot Optional. Bot through which the message was sent
 * @param editDate Optional. Date the message was last edited in Unix time
 * @param hasProtectedContent Optional. True, if the message can't be forwarded
 * @param isFromOffline Optional. True, if the message was sent by an implicit action, for example, as an away or a greeting business message, or as a scheduled message
 * @param isPaidPost Optional. True, if the message is a paid post. Note that such posts must not be deleted for 24 hours to receive the payment and can't be edited.
 * @param mediaGroupId Optional. The unique identifier of a media message group this message belongs to
 * @param authorSignature Optional. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
 * @param paidStarCount Optional. The number of Telegram Stars that were paid by the sender of the message to send it
 * @param text Optional. For text messages, the actual UTF-8 text of the message
 * @param entities Optional. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
 * @param linkPreviewOptions Optional. Options used for link preview generation for the message, if it is a text message and link preview options were changed
 * @param suggestedPostInfo Optional. Information about suggested post parameters if the message is a suggested post in a channel direct messages chat. If the message is an approved or declined suggested post, then it can't be edited.
 * @param effectId Optional. Unique identifier of the message effect added to the message
 * @param animation Optional. Message is an animation, information about the animation. For backward compatibility, when this field is set, the document field will also be set
 * @param audio Optional. Message is an audio file, information about the file
 * @param document Optional. Message is a general file, information about the file
 * @param paidMedia Optional. Message contains paid media; information about the paid media
 * @param photo Optional. Message is a photo, available sizes of the photo
 * @param sticker Optional. Message is a sticker, information about the sticker
 * @param story Optional. Message is a forwarded story
 * @param video Optional. Message is a video, information about the video
 * @param videoNote Optional. Message is a video note, information about the video message
 * @param voice Optional. Message is a voice message, information about the file
 * @param caption Optional. Caption for the animation, audio, document, paid media, photo, video or voice
 * @param captionEntities Optional. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
 * @param showCaptionAboveMedia Optional. True, if the caption must be shown above the message media
 * @param hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @param checklist Optional. Message is a checklist
 * @param contact Optional. Message is a shared contact, information about the contact
 * @param dice Optional. Message is a dice with random value
 * @param game Optional. Message is a game, information about the game. More about games: https://core.telegram.org/bots/api#games
 * @param poll Optional. Message is a native poll, information about the poll
 * @param venue Optional. Message is a venue, information about the venue. For backward compatibility, when this field is set, the location field will also be set
 * @param location Optional. Message is a shared location, information about the location
 * @param newChatMembers Optional. New members that were added to the group or supergroup and information about them (the bot itself may be one of these members)
 * @param leftChatMember Optional. A member was removed from the group, information about them (this member may be the bot itself)
 * @param newChatTitle Optional. A chat title was changed to this value
 * @param newChatPhoto Optional. A chat photo was change to this value
 * @param deleteChatPhoto Optional. Service message: the chat photo was deleted
 * @param groupChatCreated Optional. Service message: the group has been created
 * @param supergroupChatCreated Optional. Service message: the supergroup has been created. This field can't be received in a message coming through updates, because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message if someone replies to a very first message in a directly created supergroup.
 * @param channelChatCreated Optional. Service message: the channel has been created. This field can't be received in a message coming through updates, because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if someone replies to a very first message in a channel.
 * @param messageAutoDeleteTimerChanged Optional. Service message: auto-delete timer settings changed in the chat
 * @param migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param migrateFromChatId Optional. The supergroup has been migrated from a group with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param pinnedMessage Optional. Specified message was pinned. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @param invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments: https://core.telegram.org/bots/api#payments
 * @param successfulPayment Optional. Message is a service message about a successful payment, information about the payment. More about payments: https://core.telegram.org/bots/api#payments
 * @param refundedPayment Optional. Message is a service message about a refunded payment, information about the payment. More about payments: https://core.telegram.org/bots/api#payments
 * @param usersShared Optional. Service message: users were shared with the bot
 * @param chatShared Optional. Service message: a chat was shared with the bot
 * @param gift Optional. Service message: a regular gift was sent or received
 * @param uniqueGift Optional. Service message: a unique gift was sent or received
 * @param connectedWebsite Optional. The domain name of the website on which the user has logged in. More about Telegram Login: https://core.telegram.org/widgets/login
 * @param writeAccessAllowed Optional. Service message: the user allowed the bot to write messages after adding it to the attachment or side menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess
 * @param passportData Optional. Telegram Passport data
 * @param proximityAlertTriggered Optional. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
 * @param boostAdded Optional. Service message: user boosted the chat
 * @param chatBackgroundSet Optional. Service message: chat background set
 * @param checklistTasksDone Optional. Service message: some tasks in a checklist were marked as done or not done
 * @param checklistTasksAdded Optional. Service message: tasks were added to a checklist
 * @param directMessagePriceChanged Optional. Service message: the price for paid messages in the corresponding direct messages chat of a channel has changed
 * @param forumTopicCreated Optional. Service message: forum topic created
 * @param forumTopicEdited Optional. Service message: forum topic edited
 * @param forumTopicClosed Optional. Service message: forum topic closed
 * @param forumTopicReopened Optional. Service message: forum topic reopened
 * @param generalForumTopicHidden Optional. Service message: the 'General' forum topic hidden
 * @param generalForumTopicUnhidden Optional. Service message: the 'General' forum topic unhidden
 * @param giveawayCreated Optional. Service message: a scheduled giveaway was created
 * @param giveaway Optional. The message is a scheduled giveaway message
 * @param giveawayWinners Optional. A giveaway with public winners was completed
 * @param giveawayCompleted Optional. Service message: a giveaway without public winners was completed
 * @param paidMessagePriceChanged Optional. Service message: the price for paid messages has changed in the chat
 * @param suggestedPostApproved Optional. Service message: a suggested post was approved
 * @param suggestedPostApprovalFailed Optional. Service message: approval of a suggested post has failed
 * @param suggestedPostDeclined Optional. Service message: a suggested post was declined
 * @param suggestedPostPaid Optional. Service message: payment for a suggested post was received
 * @param suggestedPostRefunded Optional. Service message: payment for a suggested post was refunded
 * @param videoChatScheduled Optional. Service message: video chat scheduled
 * @param videoChatStarted Optional. Service message: video chat started
 * @param videoChatEnded Optional. Service message: video chat ended
 * @param videoChatParticipantsInvited Optional. Service message: new participants invited to a video chat
 * @param webAppData Optional. Service message: data sent by a Web App
 * @param replyMarkup Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
 */
@Serializable
data class Message(
    @SerialName("message_id") val messageId: Long,
    val date: Long,
    override val chat: Chat,
    @SerialName("message_thread_id") val messageThreadId: Long? = null,
    @SerialName("direct_messages_topic") val directMessagesTopic: DirectMessagesTopic? = null,
    val from: User? = null,
    @SerialName("sender_chat") val senderChat: Chat? = null,
    @SerialName("sender_boost_count") val senderBoostCount: Long? = null,
    @SerialName("sender_business_bot") val senderBusinessBot: User? = null,
    @SerialName("business_connection_id") val businessConnectionId: String? = null,
    @SerialName("forward_origin") val forwardOrigin: MessageOrigin? = null,
    @SerialName("is_topic_message") val isTopicMessage: Boolean? = null,
    @SerialName("is_automatic_forward") val isAutomaticForward: Boolean? = null,
    @SerialName("reply_to_message") val replyToMessage: Message? = null,
    @SerialName("external_reply") val externalReply: ExternalReplyInfo? = null,
    val quote: TextQuote? = null,
    @SerialName("reply_to_story") val replyToStory: Story? = null,
    @SerialName("reply_to_checklist_task_id") val replyToChecklistTaskId: Long? = null,
    @SerialName("via_bot") val viaBot: User? = null,
    @SerialName("edit_date") val editDate: Long? = null,
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,
    @SerialName("is_from_offline") val isFromOffline: Boolean? = null,
    @SerialName("is_paid_post") val isPaidPost: Boolean? = null,
    @SerialName("media_group_id") val mediaGroupId: String? = null,
    @SerialName("author_signature") val authorSignature: String? = null,
    @SerialName("paid_star_count") val paidStarCount: Long? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    @SerialName("link_preview_options") val linkPreviewOptions: LinkPreviewOptions? = null,
    @SerialName("suggested_post_info") val suggestedPostInfo: SuggestedPostInfo? = null,
    @SerialName("effect_id") val effectId: String? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    @SerialName("paid_media") val paidMedia: PaidMediaInfo? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    @SerialName("video_note") val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val caption: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    @SerialName("has_media_spoiler") val hasMediaSpoiler: Boolean? = null,
    val checklist: Checklist? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val location: Location? = null,
    @SerialName("new_chat_members") val newChatMembers: List<User>? = null,
    @SerialName("left_chat_member") val leftChatMember: User? = null,
    @SerialName("new_chat_title") val newChatTitle: String? = null,
    @SerialName("new_chat_photo") val newChatPhoto: List<PhotoSize>? = null,
    @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
    @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
    @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
    @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
    @SerialName("message_auto_delete_timer_changed") val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long? = null,
    @SerialName("migrate_from_chat_id") val migrateFromChatId: Long? = null,
    @SerialName("pinned_message") val pinnedMessage: MaybeInaccessibleMessage? = null,
    val invoice: Invoice? = null,
    @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null,
    @SerialName("refunded_payment") val refundedPayment: RefundedPayment? = null,
    @SerialName("users_shared") val usersShared: UsersShared? = null,
    @SerialName("chat_shared") val chatShared: ChatShared? = null,
    val gift: GiftInfo? = null,
    @SerialName("unique_gift") val uniqueGift: UniqueGiftInfo? = null,
    @SerialName("connected_website") val connectedWebsite: String? = null,
    @SerialName("write_access_allowed") val writeAccessAllowed: WriteAccessAllowed? = null,
    @SerialName("passport_data") val passportData: PassportData? = null,
    @SerialName("proximity_alert_triggered") val proximityAlertTriggered: ProximityAlertTriggered? = null,
    @SerialName("boost_added") val boostAdded: ChatBoostAdded? = null,
    @SerialName("chat_background_set") val chatBackgroundSet: ChatBackground? = null,
    @SerialName("checklist_tasks_done") val checklistTasksDone: ChecklistTasksDone? = null,
    @SerialName("checklist_tasks_added") val checklistTasksAdded: ChecklistTasksAdded? = null,
    @SerialName("direct_message_price_changed") val directMessagePriceChanged: DirectMessagePriceChanged? = null,
    @SerialName("forum_topic_created") val forumTopicCreated: ForumTopicCreated? = null,
    @SerialName("forum_topic_edited") val forumTopicEdited: ForumTopicEdited? = null,
    @SerialName("forum_topic_closed") val forumTopicClosed: ForumTopicClosed? = null,
    @SerialName("forum_topic_reopened") val forumTopicReopened: ForumTopicReopened? = null,
    @SerialName("general_forum_topic_hidden") val generalForumTopicHidden: GeneralForumTopicHidden? = null,
    @SerialName("general_forum_topic_unhidden") val generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null,
    @SerialName("giveaway_created") val giveawayCreated: GiveawayCreated? = null,
    val giveaway: Giveaway? = null,
    @SerialName("giveaway_winners") val giveawayWinners: GiveawayWinners? = null,
    @SerialName("giveaway_completed") val giveawayCompleted: GiveawayCompleted? = null,
    @SerialName("paid_message_price_changed") val paidMessagePriceChanged: PaidMessagePriceChanged? = null,
    @SerialName("suggested_post_approved") val suggestedPostApproved: SuggestedPostApproved? = null,
    @SerialName("suggested_post_approval_failed") val suggestedPostApprovalFailed: SuggestedPostApprovalFailed? = null,
    @SerialName("suggested_post_declined") val suggestedPostDeclined: SuggestedPostDeclined? = null,
    @SerialName("suggested_post_paid") val suggestedPostPaid: SuggestedPostPaid? = null,
    @SerialName("suggested_post_refunded") val suggestedPostRefunded: SuggestedPostRefunded? = null,
    @SerialName("video_chat_scheduled") val videoChatScheduled: VideoChatScheduled? = null,
    @SerialName("video_chat_started") val videoChatStarted: VideoChatStarted? = null,
    @SerialName("video_chat_ended") val videoChatEnded: VideoChatEnded? = null,
    @SerialName("video_chat_participants_invited") val videoChatParticipantsInvited: VideoChatParticipantsInvited? = null,
    @SerialName("web_app_data") val webAppData: WebAppData? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : MaybeInaccessibleMessage

