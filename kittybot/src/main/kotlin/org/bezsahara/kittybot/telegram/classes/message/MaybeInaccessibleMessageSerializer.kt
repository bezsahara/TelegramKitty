package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.bot.json.opt.deferSerial
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.chat.ChatOwnerChanged
import org.bezsahara.kittybot.telegram.classes.chat.ChatOwnerLeft
import org.bezsahara.kittybot.telegram.classes.chat.DirectMessagesTopic
import org.bezsahara.kittybot.telegram.classes.chat.background.ChatBackground
import org.bezsahara.kittybot.telegram.classes.core.ManagedBotCreated
import org.bezsahara.kittybot.telegram.classes.games.Game
import org.bezsahara.kittybot.telegram.classes.gifts.GiftInfo
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftInfo
import org.bezsahara.kittybot.telegram.classes.keyboard.ChatShared
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboard.UsersShared
import org.bezsahara.kittybot.telegram.classes.media.*
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.media.geo.Venue
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.classes.message.checklists.Checklist
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.message.polls.PollOptionAdded
import org.bezsahara.kittybot.telegram.classes.message.polls.PollOptionDeleted
import org.bezsahara.kittybot.telegram.classes.message.service.*
import org.bezsahara.kittybot.telegram.classes.passport.PassportData
import org.bezsahara.kittybot.telegram.classes.payments.Invoice
import org.bezsahara.kittybot.telegram.classes.payments.RefundedPayment
import org.bezsahara.kittybot.telegram.classes.payments.SuccessfulPayment
import org.bezsahara.kittybot.telegram.classes.rich.RichMessage
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppData


internal object MaybeInaccessibleMessageSerializer : KSerializer<MaybeInaccessibleMessage> {
    private val listSerializer0 = ListSerializer(MessageEntity.serializer())
    private val listSerializer1 = ListSerializer(PhotoSize.serializer())
    private val listSerializer2 = ListSerializer(User.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MaybeInaccessibleMessage") {
        element<Long>("message_id")
        element<Long?>("message_thread_id", isOptional = true)
        element<DirectMessagesTopic?>("direct_messages_topic", isOptional = true)
        element<User?>("from", isOptional = true)
        element<Chat?>("sender_chat", isOptional = true)
        element<Long?>("sender_boost_count", isOptional = true)
        element<User?>("sender_business_bot", isOptional = true)
        element<String?>("sender_tag", isOptional = true)
        element<Long>("date")
        element<String?>("guest_query_id", isOptional = true)
        element<String?>("business_connection_id", isOptional = true)
        element<Chat>("chat")
        element<MessageOrigin?>("forward_origin", isOptional = true)
        element<Boolean?>("is_topic_message", isOptional = true)
        element<Boolean?>("is_automatic_forward", isOptional = true)
        element("reply_to_message", deferSerial { Message.serializer().nullable.descriptor }, isOptional = true)
        element<ExternalReplyInfo?>("external_reply", isOptional = true)
        element<TextQuote?>("quote", isOptional = true)
        element<Story?>("reply_to_story", isOptional = true)
        element<Long?>("reply_to_checklist_task_id", isOptional = true)
        element<String?>("reply_to_poll_option_id", isOptional = true)
        element<User?>("via_bot", isOptional = true)
        element<User?>("guest_bot_caller_user", isOptional = true)
        element<Chat?>("guest_bot_caller_chat", isOptional = true)
        element<Long?>("edit_date", isOptional = true)
        element<Boolean?>("has_protected_content", isOptional = true)
        element<Boolean?>("is_from_offline", isOptional = true)
        element<Boolean?>("is_paid_post", isOptional = true)
        element<String?>("media_group_id", isOptional = true)
        element<String?>("author_signature", isOptional = true)
        element<Long?>("paid_star_count", isOptional = true)
        element<String?>("text", isOptional = true)
        element<List<MessageEntity>?>("entities", isOptional = true)
        element<LinkPreviewOptions?>("link_preview_options", isOptional = true)
        element<SuggestedPostInfo?>("suggested_post_info", isOptional = true)
        element<String?>("effect_id", isOptional = true)
        element<RichMessage?>("rich_message", isOptional = true)
        element<Animation?>("animation", isOptional = true)
        element<Audio?>("audio", isOptional = true)
        element<Document?>("document", isOptional = true)
        element<LivePhoto?>("live_photo", isOptional = true)
        element<PaidMediaInfo?>("paid_media", isOptional = true)
        element<List<PhotoSize>?>("photo", isOptional = true)
        element<Sticker?>("sticker", isOptional = true)
        element<Story?>("story", isOptional = true)
        element<Video?>("video", isOptional = true)
        element<VideoNote?>("video_note", isOptional = true)
        element<Voice?>("voice", isOptional = true)
        element<String?>("caption", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media", isOptional = true)
        element<Boolean?>("has_media_spoiler", isOptional = true)
        element<Checklist?>("checklist", isOptional = true)
        element<Contact?>("contact", isOptional = true)
        element<Dice?>("dice", isOptional = true)
        element<Game?>("game", isOptional = true)
        element<Poll?>("poll", isOptional = true)
        element<Venue?>("venue", isOptional = true)
        element<Location?>("location", isOptional = true)
        element<List<User>?>("new_chat_members", isOptional = true)
        element<User?>("left_chat_member", isOptional = true)
        element<ChatOwnerLeft?>("chat_owner_left", isOptional = true)
        element<ChatOwnerChanged?>("chat_owner_changed", isOptional = true)
        element<String?>("new_chat_title", isOptional = true)
        element<List<PhotoSize>?>("new_chat_photo", isOptional = true)
        element<Boolean?>("delete_chat_photo", isOptional = true)
        element<Boolean?>("group_chat_created", isOptional = true)
        element<Boolean?>("supergroup_chat_created", isOptional = true)
        element<Boolean?>("channel_chat_created", isOptional = true)
        element<MessageAutoDeleteTimerChanged?>("message_auto_delete_timer_changed", isOptional = true)
        element<Long?>("migrate_to_chat_id", isOptional = true)
        element<Long?>("migrate_from_chat_id", isOptional = true)
        element("pinned_message", deferSerial { MaybeInaccessibleMessage.serializer().nullable.descriptor }, isOptional = true)
        element<Invoice?>("invoice", isOptional = true)
        element<SuccessfulPayment?>("successful_payment", isOptional = true)
        element<RefundedPayment?>("refunded_payment", isOptional = true)
        element<UsersShared?>("users_shared", isOptional = true)
        element<ChatShared?>("chat_shared", isOptional = true)
        element<GiftInfo?>("gift", isOptional = true)
        element<UniqueGiftInfo?>("unique_gift", isOptional = true)
        element<GiftInfo?>("gift_upgrade_sent", isOptional = true)
        element<String?>("connected_website", isOptional = true)
        element<WriteAccessAllowed?>("write_access_allowed", isOptional = true)
        element<PassportData?>("passport_data", isOptional = true)
        element<ProximityAlertTriggered?>("proximity_alert_triggered", isOptional = true)
        element<ChatBoostAdded?>("boost_added", isOptional = true)
        element<ChatBackground?>("chat_background_set", isOptional = true)
        element("checklist_tasks_done", deferSerial { ChecklistTasksDone.serializer().nullable.descriptor }, isOptional = true)
        element("checklist_tasks_added", deferSerial { ChecklistTasksAdded.serializer().nullable.descriptor }, isOptional = true)
        element<DirectMessagePriceChanged?>("direct_message_price_changed", isOptional = true)
        element<ForumTopicCreated?>("forum_topic_created", isOptional = true)
        element<ForumTopicEdited?>("forum_topic_edited", isOptional = true)
        element<ForumTopicClosed?>("forum_topic_closed", isOptional = true)
        element<ForumTopicReopened?>("forum_topic_reopened", isOptional = true)
        element<GeneralForumTopicHidden?>("general_forum_topic_hidden", isOptional = true)
        element<GeneralForumTopicUnhidden?>("general_forum_topic_unhidden", isOptional = true)
        element<GiveawayCreated?>("giveaway_created", isOptional = true)
        element<Giveaway?>("giveaway", isOptional = true)
        element<GiveawayWinners?>("giveaway_winners", isOptional = true)
        element("giveaway_completed", deferSerial { GiveawayCompleted.serializer().nullable.descriptor }, isOptional = true)
        element<ManagedBotCreated?>("managed_bot_created", isOptional = true)
        element<PaidMessagePriceChanged?>("paid_message_price_changed", isOptional = true)
        element("poll_option_added", deferSerial { PollOptionAdded.serializer().nullable.descriptor }, isOptional = true)
        element("poll_option_deleted", deferSerial { PollOptionDeleted.serializer().nullable.descriptor }, isOptional = true)
        element("suggested_post_approved", deferSerial { SuggestedPostApproved.serializer().nullable.descriptor }, isOptional = true)
        element("suggested_post_approval_failed", deferSerial { SuggestedPostApprovalFailed.serializer().nullable.descriptor }, isOptional = true)
        element("suggested_post_declined", deferSerial { SuggestedPostDeclined.serializer().nullable.descriptor }, isOptional = true)
        element("suggested_post_paid", deferSerial { SuggestedPostPaid.serializer().nullable.descriptor }, isOptional = true)
        element("suggested_post_refunded", deferSerial { SuggestedPostRefunded.serializer().nullable.descriptor }, isOptional = true)
        element<VideoChatScheduled?>("video_chat_scheduled", isOptional = true)
        element<VideoChatStarted?>("video_chat_started", isOptional = true)
        element<VideoChatEnded?>("video_chat_ended", isOptional = true)
        element<VideoChatParticipantsInvited?>("video_chat_participants_invited", isOptional = true)
        element<WebAppData?>("web_app_data", isOptional = true)
        element<InlineKeyboardMarkup?>("reply_markup", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: MaybeInaccessibleMessage) {
        when (value) {
            is Message -> Message.serializer().serialize(encoder, value)
            is InaccessibleMessage -> InaccessibleMessage.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): MaybeInaccessibleMessage {
        var nativeMask = IntMask.EMPTY
        var messageId: Long = 0L
        var messageThreadId: Long? = null
        var directMessagesTopic: DirectMessagesTopic? = null
        var from: User? = null
        var senderChat: Chat? = null
        var senderBoostCount: Long? = null
        var senderBusinessBot: User? = null
        var senderTag: String? = null
        var date: Long = 0L
        var guestQueryId: String? = null
        var businessConnectionId: String? = null
        var chat: Chat? = null
        var forwardOrigin: MessageOrigin? = null
        var isTopicMessage: Boolean? = null
        var isAutomaticForward: Boolean? = null
        var replyToMessage: Message? = null
        var externalReply: ExternalReplyInfo? = null
        var quote: TextQuote? = null
        var replyToStory: Story? = null
        var replyToChecklistTaskId: Long? = null
        var replyToPollOptionId: String? = null
        var viaBot: User? = null
        var guestBotCallerUser: User? = null
        var guestBotCallerChat: Chat? = null
        var editDate: Long? = null
        var hasProtectedContent: Boolean? = null
        var isFromOffline: Boolean? = null
        var isPaidPost: Boolean? = null
        var mediaGroupId: String? = null
        var authorSignature: String? = null
        var paidStarCount: Long? = null
        var text: String? = null
        var entities: List<MessageEntity>? = null
        var linkPreviewOptions: LinkPreviewOptions? = null
        var suggestedPostInfo: SuggestedPostInfo? = null
        var effectId: String? = null
        var richMessage: RichMessage? = null
        var animation: Animation? = null
        var audio: Audio? = null
        var document: Document? = null
        var livePhoto: LivePhoto? = null
        var paidMedia: PaidMediaInfo? = null
        var photo: List<PhotoSize>? = null
        var sticker: Sticker? = null
        var story: Story? = null
        var video: Video? = null
        var videoNote: VideoNote? = null
        var voice: Voice? = null
        var caption: String? = null
        var captionEntities: List<MessageEntity>? = null
        var showCaptionAboveMedia: Boolean? = null
        var hasMediaSpoiler: Boolean? = null
        var checklist: Checklist? = null
        var contact: Contact? = null
        var dice: Dice? = null
        var game: Game? = null
        var poll: Poll? = null
        var venue: Venue? = null
        var location: Location? = null
        var newChatMembers: List<User>? = null
        var leftChatMember: User? = null
        var chatOwnerLeft: ChatOwnerLeft? = null
        var chatOwnerChanged: ChatOwnerChanged? = null
        var newChatTitle: String? = null
        var newChatPhoto: List<PhotoSize>? = null
        var deleteChatPhoto: Boolean? = null
        var groupChatCreated: Boolean? = null
        var supergroupChatCreated: Boolean? = null
        var channelChatCreated: Boolean? = null
        var messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null
        var migrateToChatId: Long? = null
        var migrateFromChatId: Long? = null
        var pinnedMessage: MaybeInaccessibleMessage? = null
        var invoice: Invoice? = null
        var successfulPayment: SuccessfulPayment? = null
        var refundedPayment: RefundedPayment? = null
        var usersShared: UsersShared? = null
        var chatShared: ChatShared? = null
        var gift: GiftInfo? = null
        var uniqueGift: UniqueGiftInfo? = null
        var giftUpgradeSent: GiftInfo? = null
        var connectedWebsite: String? = null
        var writeAccessAllowed: WriteAccessAllowed? = null
        var passportData: PassportData? = null
        var proximityAlertTriggered: ProximityAlertTriggered? = null
        var boostAdded: ChatBoostAdded? = null
        var chatBackgroundSet: ChatBackground? = null
        var checklistTasksDone: ChecklistTasksDone? = null
        var checklistTasksAdded: ChecklistTasksAdded? = null
        var directMessagePriceChanged: DirectMessagePriceChanged? = null
        var forumTopicCreated: ForumTopicCreated? = null
        var forumTopicEdited: ForumTopicEdited? = null
        var forumTopicClosed: ForumTopicClosed? = null
        var forumTopicReopened: ForumTopicReopened? = null
        var generalForumTopicHidden: GeneralForumTopicHidden? = null
        var generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null
        var giveawayCreated: GiveawayCreated? = null
        var giveaway: Giveaway? = null
        var giveawayWinners: GiveawayWinners? = null
        var giveawayCompleted: GiveawayCompleted? = null
        var managedBotCreated: ManagedBotCreated? = null
        var paidMessagePriceChanged: PaidMessagePriceChanged? = null
        var pollOptionAdded: PollOptionAdded? = null
        var pollOptionDeleted: PollOptionDeleted? = null
        var suggestedPostApproved: SuggestedPostApproved? = null
        var suggestedPostApprovalFailed: SuggestedPostApprovalFailed? = null
        var suggestedPostDeclined: SuggestedPostDeclined? = null
        var suggestedPostPaid: SuggestedPostPaid? = null
        var suggestedPostRefunded: SuggestedPostRefunded? = null
        var videoChatScheduled: VideoChatScheduled? = null
        var videoChatStarted: VideoChatStarted? = null
        var videoChatEnded: VideoChatEnded? = null
        var videoChatParticipantsInvited: VideoChatParticipantsInvited? = null
        var webAppData: WebAppData? = null
        var replyMarkup: InlineKeyboardMarkup? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> {
                    messageId = dec.decodeLongElement(descriptor, 0)
                    nativeMask = nativeMask.setBit(0)
                }
                1 -> messageThreadId = dec.decodeNullableSerializableElement(descriptor, 1, Long.serializer(), null)
                2 -> directMessagesTopic = dec.decodeNullableSerializableElement(descriptor, 2, DirectMessagesTopic.serializer(), null)
                3 -> from = dec.decodeNullableSerializableElement(descriptor, 3, User.serializer(), null)
                4 -> senderChat = dec.decodeNullableSerializableElement(descriptor, 4, Chat.serializer(), null)
                5 -> senderBoostCount = dec.decodeNullableSerializableElement(descriptor, 5, Long.serializer(), null)
                6 -> senderBusinessBot = dec.decodeNullableSerializableElement(descriptor, 6, User.serializer(), null)
                7 -> senderTag = dec.decodeNullableSerializableElement(descriptor, 7, String.serializer(), null)
                8 -> {
                    date = dec.decodeLongElement(descriptor, 8)
                    nativeMask = nativeMask.setBit(1)
                }
                9 -> guestQueryId = dec.decodeNullableSerializableElement(descriptor, 9, String.serializer(), null)
                10 -> businessConnectionId = dec.decodeNullableSerializableElement(descriptor, 10, String.serializer(), null)
                11 -> chat = dec.decodeSerializableElement(descriptor, 11, Chat.serializer())
                12 -> forwardOrigin = dec.decodeNullableSerializableElement(descriptor, 12, MessageOrigin.serializer(), null)
                13 -> isTopicMessage = dec.decodeNullableSerializableElement(descriptor, 13, Boolean.serializer(), null)
                14 -> isAutomaticForward = dec.decodeNullableSerializableElement(descriptor, 14, Boolean.serializer(), null)
                15 -> replyToMessage = dec.decodeNullableSerializableElement(descriptor, 15, Message.serializer(), null)
                16 -> externalReply = dec.decodeNullableSerializableElement(descriptor, 16, ExternalReplyInfo.serializer(), null)
                17 -> quote = dec.decodeNullableSerializableElement(descriptor, 17, TextQuote.serializer(), null)
                18 -> replyToStory = dec.decodeNullableSerializableElement(descriptor, 18, Story.serializer(), null)
                19 -> replyToChecklistTaskId = dec.decodeNullableSerializableElement(descriptor, 19, Long.serializer(), null)
                20 -> replyToPollOptionId = dec.decodeNullableSerializableElement(descriptor, 20, String.serializer(), null)
                21 -> viaBot = dec.decodeNullableSerializableElement(descriptor, 21, User.serializer(), null)
                22 -> guestBotCallerUser = dec.decodeNullableSerializableElement(descriptor, 22, User.serializer(), null)
                23 -> guestBotCallerChat = dec.decodeNullableSerializableElement(descriptor, 23, Chat.serializer(), null)
                24 -> editDate = dec.decodeNullableSerializableElement(descriptor, 24, Long.serializer(), null)
                25 -> hasProtectedContent = dec.decodeNullableSerializableElement(descriptor, 25, Boolean.serializer(), null)
                26 -> isFromOffline = dec.decodeNullableSerializableElement(descriptor, 26, Boolean.serializer(), null)
                27 -> isPaidPost = dec.decodeNullableSerializableElement(descriptor, 27, Boolean.serializer(), null)
                28 -> mediaGroupId = dec.decodeNullableSerializableElement(descriptor, 28, String.serializer(), null)
                29 -> authorSignature = dec.decodeNullableSerializableElement(descriptor, 29, String.serializer(), null)
                30 -> paidStarCount = dec.decodeNullableSerializableElement(descriptor, 30, Long.serializer(), null)
                31 -> text = dec.decodeNullableSerializableElement(descriptor, 31, String.serializer(), null)
                32 -> entities = dec.decodeNullableSerializableElement(descriptor, 32, listSerializer0, null)
                33 -> linkPreviewOptions = dec.decodeNullableSerializableElement(descriptor, 33, LinkPreviewOptions.serializer(), null)
                34 -> suggestedPostInfo = dec.decodeNullableSerializableElement(descriptor, 34, SuggestedPostInfo.serializer(), null)
                35 -> effectId = dec.decodeNullableSerializableElement(descriptor, 35, String.serializer(), null)
                36 -> richMessage = dec.decodeNullableSerializableElement(descriptor, 36, RichMessage.serializer(), null)
                37 -> animation = dec.decodeNullableSerializableElement(descriptor, 37, Animation.serializer(), null)
                38 -> audio = dec.decodeNullableSerializableElement(descriptor, 38, Audio.serializer(), null)
                39 -> document = dec.decodeNullableSerializableElement(descriptor, 39, Document.serializer(), null)
                40 -> livePhoto = dec.decodeNullableSerializableElement(descriptor, 40, LivePhoto.serializer(), null)
                41 -> paidMedia = dec.decodeNullableSerializableElement(descriptor, 41, PaidMediaInfo.serializer(), null)
                42 -> photo = dec.decodeNullableSerializableElement(descriptor, 42, listSerializer1, null)
                43 -> sticker = dec.decodeNullableSerializableElement(descriptor, 43, Sticker.serializer(), null)
                44 -> story = dec.decodeNullableSerializableElement(descriptor, 44, Story.serializer(), null)
                45 -> video = dec.decodeNullableSerializableElement(descriptor, 45, Video.serializer(), null)
                46 -> videoNote = dec.decodeNullableSerializableElement(descriptor, 46, VideoNote.serializer(), null)
                47 -> voice = dec.decodeNullableSerializableElement(descriptor, 47, Voice.serializer(), null)
                48 -> caption = dec.decodeNullableSerializableElement(descriptor, 48, String.serializer(), null)
                49 -> captionEntities = dec.decodeNullableSerializableElement(descriptor, 49, listSerializer0, null)
                50 -> showCaptionAboveMedia = dec.decodeNullableSerializableElement(descriptor, 50, Boolean.serializer(), null)
                51 -> hasMediaSpoiler = dec.decodeNullableSerializableElement(descriptor, 51, Boolean.serializer(), null)
                52 -> checklist = dec.decodeNullableSerializableElement(descriptor, 52, Checklist.serializer(), null)
                53 -> contact = dec.decodeNullableSerializableElement(descriptor, 53, Contact.serializer(), null)
                54 -> dice = dec.decodeNullableSerializableElement(descriptor, 54, Dice.serializer(), null)
                55 -> game = dec.decodeNullableSerializableElement(descriptor, 55, Game.serializer(), null)
                56 -> poll = dec.decodeNullableSerializableElement(descriptor, 56, Poll.serializer(), null)
                57 -> venue = dec.decodeNullableSerializableElement(descriptor, 57, Venue.serializer(), null)
                58 -> location = dec.decodeNullableSerializableElement(descriptor, 58, Location.serializer(), null)
                59 -> newChatMembers = dec.decodeNullableSerializableElement(descriptor, 59, listSerializer2, null)
                60 -> leftChatMember = dec.decodeNullableSerializableElement(descriptor, 60, User.serializer(), null)
                61 -> chatOwnerLeft = dec.decodeNullableSerializableElement(descriptor, 61, ChatOwnerLeft.serializer(), null)
                62 -> chatOwnerChanged = dec.decodeNullableSerializableElement(descriptor, 62, ChatOwnerChanged.serializer(), null)
                63 -> newChatTitle = dec.decodeNullableSerializableElement(descriptor, 63, String.serializer(), null)
                64 -> newChatPhoto = dec.decodeNullableSerializableElement(descriptor, 64, listSerializer1, null)
                65 -> deleteChatPhoto = dec.decodeNullableSerializableElement(descriptor, 65, Boolean.serializer(), null)
                66 -> groupChatCreated = dec.decodeNullableSerializableElement(descriptor, 66, Boolean.serializer(), null)
                67 -> supergroupChatCreated = dec.decodeNullableSerializableElement(descriptor, 67, Boolean.serializer(), null)
                68 -> channelChatCreated = dec.decodeNullableSerializableElement(descriptor, 68, Boolean.serializer(), null)
                69 -> messageAutoDeleteTimerChanged = dec.decodeNullableSerializableElement(descriptor, 69, MessageAutoDeleteTimerChanged.serializer(), null)
                70 -> migrateToChatId = dec.decodeNullableSerializableElement(descriptor, 70, Long.serializer(), null)
                71 -> migrateFromChatId = dec.decodeNullableSerializableElement(descriptor, 71, Long.serializer(), null)
                72 -> pinnedMessage = dec.decodeNullableSerializableElement(descriptor, 72, MaybeInaccessibleMessage.serializer(), null)
                73 -> invoice = dec.decodeNullableSerializableElement(descriptor, 73, Invoice.serializer(), null)
                74 -> successfulPayment = dec.decodeNullableSerializableElement(descriptor, 74, SuccessfulPayment.serializer(), null)
                75 -> refundedPayment = dec.decodeNullableSerializableElement(descriptor, 75, RefundedPayment.serializer(), null)
                76 -> usersShared = dec.decodeNullableSerializableElement(descriptor, 76, UsersShared.serializer(), null)
                77 -> chatShared = dec.decodeNullableSerializableElement(descriptor, 77, ChatShared.serializer(), null)
                78 -> gift = dec.decodeNullableSerializableElement(descriptor, 78, GiftInfo.serializer(), null)
                79 -> uniqueGift = dec.decodeNullableSerializableElement(descriptor, 79, UniqueGiftInfo.serializer(), null)
                80 -> giftUpgradeSent = dec.decodeNullableSerializableElement(descriptor, 80, GiftInfo.serializer(), null)
                81 -> connectedWebsite = dec.decodeNullableSerializableElement(descriptor, 81, String.serializer(), null)
                82 -> writeAccessAllowed = dec.decodeNullableSerializableElement(descriptor, 82, WriteAccessAllowed.serializer(), null)
                83 -> passportData = dec.decodeNullableSerializableElement(descriptor, 83, PassportData.serializer(), null)
                84 -> proximityAlertTriggered = dec.decodeNullableSerializableElement(descriptor, 84, ProximityAlertTriggered.serializer(), null)
                85 -> boostAdded = dec.decodeNullableSerializableElement(descriptor, 85, ChatBoostAdded.serializer(), null)
                86 -> chatBackgroundSet = dec.decodeNullableSerializableElement(descriptor, 86, ChatBackground.serializer(), null)
                87 -> checklistTasksDone = dec.decodeNullableSerializableElement(descriptor, 87, ChecklistTasksDone.serializer(), null)
                88 -> checklistTasksAdded = dec.decodeNullableSerializableElement(descriptor, 88, ChecklistTasksAdded.serializer(), null)
                89 -> directMessagePriceChanged = dec.decodeNullableSerializableElement(descriptor, 89, DirectMessagePriceChanged.serializer(), null)
                90 -> forumTopicCreated = dec.decodeNullableSerializableElement(descriptor, 90, ForumTopicCreated.serializer(), null)
                91 -> forumTopicEdited = dec.decodeNullableSerializableElement(descriptor, 91, ForumTopicEdited.serializer(), null)
                92 -> forumTopicClosed = dec.decodeNullableSerializableElement(descriptor, 92, ForumTopicClosed.serializer(), null)
                93 -> forumTopicReopened = dec.decodeNullableSerializableElement(descriptor, 93, ForumTopicReopened.serializer(), null)
                94 -> generalForumTopicHidden = dec.decodeNullableSerializableElement(descriptor, 94, GeneralForumTopicHidden.serializer(), null)
                95 -> generalForumTopicUnhidden = dec.decodeNullableSerializableElement(descriptor, 95, GeneralForumTopicUnhidden.serializer(), null)
                96 -> giveawayCreated = dec.decodeNullableSerializableElement(descriptor, 96, GiveawayCreated.serializer(), null)
                97 -> giveaway = dec.decodeNullableSerializableElement(descriptor, 97, Giveaway.serializer(), null)
                98 -> giveawayWinners = dec.decodeNullableSerializableElement(descriptor, 98, GiveawayWinners.serializer(), null)
                99 -> giveawayCompleted = dec.decodeNullableSerializableElement(descriptor, 99, GiveawayCompleted.serializer(), null)
                100 -> managedBotCreated = dec.decodeNullableSerializableElement(descriptor, 100, ManagedBotCreated.serializer(), null)
                101 -> paidMessagePriceChanged = dec.decodeNullableSerializableElement(descriptor, 101, PaidMessagePriceChanged.serializer(), null)
                102 -> pollOptionAdded = dec.decodeNullableSerializableElement(descriptor, 102, PollOptionAdded.serializer(), null)
                103 -> pollOptionDeleted = dec.decodeNullableSerializableElement(descriptor, 103, PollOptionDeleted.serializer(), null)
                104 -> suggestedPostApproved = dec.decodeNullableSerializableElement(descriptor, 104, SuggestedPostApproved.serializer(), null)
                105 -> suggestedPostApprovalFailed = dec.decodeNullableSerializableElement(descriptor, 105, SuggestedPostApprovalFailed.serializer(), null)
                106 -> suggestedPostDeclined = dec.decodeNullableSerializableElement(descriptor, 106, SuggestedPostDeclined.serializer(), null)
                107 -> suggestedPostPaid = dec.decodeNullableSerializableElement(descriptor, 107, SuggestedPostPaid.serializer(), null)
                108 -> suggestedPostRefunded = dec.decodeNullableSerializableElement(descriptor, 108, SuggestedPostRefunded.serializer(), null)
                109 -> videoChatScheduled = dec.decodeNullableSerializableElement(descriptor, 109, VideoChatScheduled.serializer(), null)
                110 -> videoChatStarted = dec.decodeNullableSerializableElement(descriptor, 110, VideoChatStarted.serializer(), null)
                111 -> videoChatEnded = dec.decodeNullableSerializableElement(descriptor, 111, VideoChatEnded.serializer(), null)
                112 -> videoChatParticipantsInvited = dec.decodeNullableSerializableElement(descriptor, 112, VideoChatParticipantsInvited.serializer(), null)
                113 -> webAppData = dec.decodeNullableSerializableElement(descriptor, 113, WebAppData.serializer(), null)
                114 -> replyMarkup = dec.decodeNullableSerializableElement(descriptor, 114, InlineKeyboardMarkup.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing MaybeInaccessibleMessage")
            }
        }
        dec.endStructure(descriptor)
        return when (if (nativeMask.has(1)) date else throwMissingField("date")) {
            0L -> InaccessibleMessage(
                chat = chat ?: throwMissingField("chat"),
                messageId = if (nativeMask.has(0)) messageId else throwMissingField("message_id"),
                date = if (nativeMask.has(1)) date else throwMissingField("date")
            )
            else -> Message(
                messageId = if (nativeMask.has(0)) messageId else throwMissingField("message_id"),
                messageThreadId = messageThreadId,
                directMessagesTopic = directMessagesTopic,
                from = from,
                senderChat = senderChat,
                senderBoostCount = senderBoostCount,
                senderBusinessBot = senderBusinessBot,
                senderTag = senderTag,
                date = if (nativeMask.has(1)) date else throwMissingField("date"),
                guestQueryId = guestQueryId,
                businessConnectionId = businessConnectionId,
                chat = chat ?: throwMissingField("chat"),
                forwardOrigin = forwardOrigin,
                isTopicMessage = isTopicMessage,
                isAutomaticForward = isAutomaticForward,
                replyToMessage = replyToMessage,
                externalReply = externalReply,
                quote = quote,
                replyToStory = replyToStory,
                replyToChecklistTaskId = replyToChecklistTaskId,
                replyToPollOptionId = replyToPollOptionId,
                viaBot = viaBot,
                guestBotCallerUser = guestBotCallerUser,
                guestBotCallerChat = guestBotCallerChat,
                editDate = editDate,
                hasProtectedContent = hasProtectedContent,
                isFromOffline = isFromOffline,
                isPaidPost = isPaidPost,
                mediaGroupId = mediaGroupId,
                authorSignature = authorSignature,
                paidStarCount = paidStarCount,
                text = text,
                entities = entities,
                linkPreviewOptions = linkPreviewOptions,
                suggestedPostInfo = suggestedPostInfo,
                effectId = effectId,
                richMessage = richMessage,
                animation = animation,
                audio = audio,
                document = document,
                livePhoto = livePhoto,
                paidMedia = paidMedia,
                photo = photo,
                sticker = sticker,
                story = story,
                video = video,
                videoNote = videoNote,
                voice = voice,
                caption = caption,
                captionEntities = captionEntities,
                showCaptionAboveMedia = showCaptionAboveMedia,
                hasMediaSpoiler = hasMediaSpoiler,
                checklist = checklist,
                contact = contact,
                dice = dice,
                game = game,
                poll = poll,
                venue = venue,
                location = location,
                newChatMembers = newChatMembers,
                leftChatMember = leftChatMember,
                chatOwnerLeft = chatOwnerLeft,
                chatOwnerChanged = chatOwnerChanged,
                newChatTitle = newChatTitle,
                newChatPhoto = newChatPhoto,
                deleteChatPhoto = deleteChatPhoto,
                groupChatCreated = groupChatCreated,
                supergroupChatCreated = supergroupChatCreated,
                channelChatCreated = channelChatCreated,
                messageAutoDeleteTimerChanged = messageAutoDeleteTimerChanged,
                migrateToChatId = migrateToChatId,
                migrateFromChatId = migrateFromChatId,
                pinnedMessage = pinnedMessage,
                invoice = invoice,
                successfulPayment = successfulPayment,
                refundedPayment = refundedPayment,
                usersShared = usersShared,
                chatShared = chatShared,
                gift = gift,
                uniqueGift = uniqueGift,
                giftUpgradeSent = giftUpgradeSent,
                connectedWebsite = connectedWebsite,
                writeAccessAllowed = writeAccessAllowed,
                passportData = passportData,
                proximityAlertTriggered = proximityAlertTriggered,
                boostAdded = boostAdded,
                chatBackgroundSet = chatBackgroundSet,
                checklistTasksDone = checklistTasksDone,
                checklistTasksAdded = checklistTasksAdded,
                directMessagePriceChanged = directMessagePriceChanged,
                forumTopicCreated = forumTopicCreated,
                forumTopicEdited = forumTopicEdited,
                forumTopicClosed = forumTopicClosed,
                forumTopicReopened = forumTopicReopened,
                generalForumTopicHidden = generalForumTopicHidden,
                generalForumTopicUnhidden = generalForumTopicUnhidden,
                giveawayCreated = giveawayCreated,
                giveaway = giveaway,
                giveawayWinners = giveawayWinners,
                giveawayCompleted = giveawayCompleted,
                managedBotCreated = managedBotCreated,
                paidMessagePriceChanged = paidMessagePriceChanged,
                pollOptionAdded = pollOptionAdded,
                pollOptionDeleted = pollOptionDeleted,
                suggestedPostApproved = suggestedPostApproved,
                suggestedPostApprovalFailed = suggestedPostApprovalFailed,
                suggestedPostDeclined = suggestedPostDeclined,
                suggestedPostPaid = suggestedPostPaid,
                suggestedPostRefunded = suggestedPostRefunded,
                videoChatScheduled = videoChatScheduled,
                videoChatStarted = videoChatStarted,
                videoChatEnded = videoChatEnded,
                videoChatParticipantsInvited = videoChatParticipantsInvited,
                webAppData = webAppData,
                replyMarkup = replyMarkup
            )
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing MaybeInaccessibleMessage")
    }
}
