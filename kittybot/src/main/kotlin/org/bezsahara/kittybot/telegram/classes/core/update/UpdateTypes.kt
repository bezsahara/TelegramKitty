package org.bezsahara.kittybot.telegram.classes.core.update

import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.business.BusinessMessagesDeleted
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.chat.ChatJoinRequest
import org.bezsahara.kittybot.telegram.classes.chat.ChatMemberUpdated
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSource
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourceGiftCode
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourceGiveaway
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourcePremium
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostRemoved
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostUpdated
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberAdministrator
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberBanned
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberLeft
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberMember
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberOwner
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberRestricted
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.ManagedBotUpdated
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.inline.ChosenInlineResult
import org.bezsahara.kittybot.telegram.classes.inline.InlineQuery
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.message.polls.PollAnswer
import org.bezsahara.kittybot.telegram.classes.message.reactions.MessageReactionCountUpdated
import org.bezsahara.kittybot.telegram.classes.message.reactions.MessageReactionUpdated
import org.bezsahara.kittybot.telegram.classes.payments.PaidMediaPurchased
import org.bezsahara.kittybot.telegram.classes.payments.PreCheckoutQuery
import org.bezsahara.kittybot.telegram.classes.payments.ShippingQuery

val telegramUpdateKinds: Set<UpdateKind<*>> = setOf(
    MessageUpdate,
    EditedMessageUpdate,
    ChannelPostUpdate,
    EditedChannelPostUpdate,
    BusinessConnectionUpdate,
    BusinessMessageUpdate,
    EditedBusinessMessageUpdate,
    DeletedBusinessMessagesUpdate,
    MessageReactionUpdate,
    MessageReactionCountUpdate,
    InlineQueryUpdate,
    ChosenInlineResultUpdate,
    CallbackQueryUpdate,
    ShippingQueryUpdate,
    PreCheckoutQueryUpdate,
    PaidMediaPurchasedUpdate,
    PollUpdate,
    PollAnswerUpdate,
    MyChatMemberUpdate,
    ChatMemberUpdate,
    ChatJoinRequestUpdate,
    ChatBoostUpdate,
    RemovedChatBoostUpdate,
    ManagedBotUpdate,
    UnknownUpdate,
    SyntheticUpdate
)

fun UpdateKind<out Update>.toSet(): Set<UpdateKind<out Update>> = setOf(this)



/**
 * This object represents an incoming update containing a new message.
 *
 * @property message New incoming message of any kind - text, photo, sticker, etc.
 */
data class MessageUpdate(
    override val updateId: Long,
    override val message: Message,
) : Update() {
    override val ordinal: Int get() = 0
    override fun chatIdOrNull(): ChatId = message.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = message.userIdOrNull()

    companion object : UpdateKind<MessageUpdate>(
        0, MessageUpdate::class.java,
        "message"
    )
}


/**
 * This object represents an incoming update containing an edited message.
 *
 * @property editedMessage New version of a message that is known to the bot and was edited.
 *                         This update may at times be triggered by changes to message fields
 *                         that are either unavailable or not actively used by your bot.
 */
data class EditedMessageUpdate(
    override val updateId: Long,
    override val editedMessage: Message,
) : Update() {
    override val ordinal: Int get() = 1
    override fun chatIdOrNull(): ChatId = editedMessage.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = editedMessage.userIdOrNull()

    companion object : UpdateKind<EditedMessageUpdate>(
        1, EditedMessageUpdate::class.java,
        "edited_message"
    )
}

/**
 * This object represents an incoming update containing a new channel post.
 *
 * @property channelPost New incoming channel post of any kind - text, photo, sticker, etc.
 */
data class ChannelPostUpdate(
    override val updateId: Long,
    override val channelPost: Message,
) : Update() {
    override val ordinal: Int get() = 2
    override fun chatIdOrNull(): ChatId = channelPost.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = channelPost.userIdOrNull()

    companion object : UpdateKind<ChannelPostUpdate>(
        2, ChannelPostUpdate::class.java,
        "channel_post"
    )
}

/**
 * This object represents an incoming update containing an edited channel post.
 *
 * @property editedChannelPost New version of a channel post that is known to the bot and was edited.
 *                             This update may at times be triggered by changes to message fields
 *                             that are either unavailable or not actively used by your bot.
 */
data class EditedChannelPostUpdate(
    override val updateId: Long,
    override val editedChannelPost: Message,
) : Update() {
    override val ordinal: Int get() = 3
    override fun chatIdOrNull(): ChatId = editedChannelPost.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = editedChannelPost.userIdOrNull()

    companion object : UpdateKind<EditedChannelPostUpdate>(
        3, EditedChannelPostUpdate::class.java,
        "edited_channel_post"
    )
}

/**
 * This object represents an incoming update containing a business connection event.
 *
 * @property businessConnection The bot was connected to or disconnected from a business account,
 *                              or a user edited an existing connection with the bot.
 */
data class BusinessConnectionUpdate(
    override val updateId: Long,
    override val businessConnection: BusinessConnection,
) : Update() {
    override val ordinal: Int get() = 4
    override fun chatIdOrNull(): ChatId = businessConnection.userChatId.toChatId()
    override fun userIdOrNull(): ChatId = businessConnection.user.id.toChatId()

    companion object : UpdateKind<BusinessConnectionUpdate>(
        4, BusinessConnectionUpdate::class.java,
        "business_connection"
    )
}

/**
 * This object represents an incoming update containing a new message from a connected business account.
 *
 * @property businessMessage New message from a connected business account.
 */
data class BusinessMessageUpdate(
    override val updateId: Long,
    override val businessMessage: Message,
) : Update() {
    override val ordinal: Int get() = 5
    override fun chatIdOrNull(): ChatId = businessMessage.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = businessMessage.userIdOrNull()

    companion object : UpdateKind<BusinessMessageUpdate>(
        5, BusinessMessageUpdate::class.java,
        "business_message"
    )
}

/**
 * This object represents an incoming update containing an edited message from a connected business account.
 *
 * @property editedBusinessMessage New version of a message from a connected business account.
 */
data class EditedBusinessMessageUpdate(
    override val updateId: Long,
    override val editedBusinessMessage: Message,
) : Update() {
    override val ordinal: Int get() = 6
    override fun chatIdOrNull(): ChatId = editedBusinessMessage.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = editedBusinessMessage.userIdOrNull()

    companion object : UpdateKind<EditedBusinessMessageUpdate>(
        6, EditedBusinessMessageUpdate::class.java,
        "edited_business_message"
    )
}

/**
 * This object represents an incoming update containing deleted messages from a connected business account.
 *
 * @property deletedBusinessMessages Messages were deleted from a connected business account.
 */
data class DeletedBusinessMessagesUpdate(
    override val updateId: Long,
    override val deletedBusinessMessages: BusinessMessagesDeleted,
) : Update() {
    override val ordinal: Int get() = 7
    override fun chatIdOrNull(): ChatId = deletedBusinessMessages.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = null

    companion object : UpdateKind<DeletedBusinessMessagesUpdate>(
        7, DeletedBusinessMessagesUpdate::class.java,
        "deleted_business_messages"
    )
}

/**
 * This object represents an incoming update containing a message reaction event.
 *
 * @property messageReaction A reaction to a message was changed by a user.
 *                           The bot must be an administrator in the chat and must explicitly
 *                           specify "message_reaction" in the list of allowed_updates to receive these updates.
 *                           The update isn't received for reactions set by bots.
 */
data class MessageReactionUpdate(
    override val updateId: Long,
    override val messageReaction: MessageReactionUpdated,
) : Update() {
    override val ordinal: Int get() = 8
    override fun chatIdOrNull(): ChatId = messageReaction.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = messageReaction.user?.id?.toChatId()

    companion object : UpdateKind<MessageReactionUpdate>(
        8, MessageReactionUpdate::class.java,
        "message_reaction"
    )
}

/**
 * This object represents an incoming update containing a change in the count of message reactions.
 *
 * @property messageReactionCount Reactions to a message with anonymous reactions were changed.
 *                                The bot must be an administrator in the chat and must explicitly
 *                                specify "message_reaction_count" in the list of allowed_updates to receive these updates.
 *                                The updates are grouped and can be sent with delay up to a few minutes.
 */
data class MessageReactionCountUpdate(
    override val updateId: Long,
    override val messageReactionCount: MessageReactionCountUpdated,
) : Update() {
    override val ordinal: Int get() = 9
    override fun chatIdOrNull(): ChatId = messageReactionCount.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = null

    companion object : UpdateKind<MessageReactionCountUpdate>(
        9, MessageReactionCountUpdate::class.java,
        "message_reaction_count"
    )
}

/**
 * This object represents an incoming update containing a new inline query.
 *
 * @property inlineQuery New incoming inline query.
 */
data class InlineQueryUpdate(
    override val updateId: Long,
    override val inlineQuery: InlineQuery,
) : Update() {
    override val ordinal: Int get() = 10
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId = inlineQuery.from.id.toChatId()

    companion object : UpdateKind<InlineQueryUpdate>(
        10, InlineQueryUpdate::class.java,
        "inline_query"
    )
}

/**
 * This object represents an incoming update containing a chosen inline result.
 *
 * @property chosenInlineResult The result of an inline query that was chosen by a user and sent to their chat partner.
 *                              Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
 */
data class ChosenInlineResultUpdate(
    override val updateId: Long,
    override val chosenInlineResult: ChosenInlineResult,
) : Update() {
    override val ordinal: Int get() = 11
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId = chosenInlineResult.from.id.toChatId()

    companion object : UpdateKind<ChosenInlineResultUpdate>(
        11, ChosenInlineResultUpdate::class.java,
        "chosen_inline_result"
    )
}

/**
 * This object represents an incoming update containing a new callback query.
 *
 * @property callbackQuery New incoming callback query.
 */
data class CallbackQueryUpdate(
    override val updateId: Long,
    override val callbackQuery: CallbackQuery,
) : Update() {
    override val ordinal: Int get() = 12
    override fun chatIdOrNull(): ChatId? = callbackQuery.message?.chat?.id?.toChatId()
    override fun userIdOrNull(): ChatId = callbackQuery.from.id.toChatId()

    companion object : UpdateKind<CallbackQueryUpdate>(
        12, CallbackQueryUpdate::class.java,
        "callback_query"
    )
}

/**
 * This object represents an incoming update containing a new shipping query.
 *
 * @property shippingQuery New incoming shipping query. Only for invoices with flexible price.
 */
data class ShippingQueryUpdate(
    override val updateId: Long,
    override val shippingQuery: ShippingQuery,
) : Update() {
    override val ordinal: Int get() = 13
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId = shippingQuery.from.id.toChatId()

    companion object : UpdateKind<ShippingQueryUpdate>(
        13, ShippingQueryUpdate::class.java,
        "shipping_query"
    )
}

/**
 * This object represents an incoming update containing a new pre-checkout query.
 *
 * @property preCheckoutQuery New incoming pre-checkout query. Contains full information about checkout.
 */
data class PreCheckoutQueryUpdate(
    override val updateId: Long,
    override val preCheckoutQuery: PreCheckoutQuery,
) : Update() {
    override val ordinal: Int get() = 14
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId = preCheckoutQuery.from.id.toChatId()

    companion object : UpdateKind<PreCheckoutQueryUpdate>(
        14, PreCheckoutQueryUpdate::class.java,
        "pre_checkout_query"
    )
}

// TODO add docs
data class PaidMediaPurchasedUpdate(
    override val updateId: Long,
    override val purchasedPaidMedia: PaidMediaPurchased,
) : Update() {
    override val ordinal: Int get() = 15
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId = purchasedPaidMedia.from.id.toChatId()

    companion object : UpdateKind<PaidMediaPurchasedUpdate>(
        15, PaidMediaPurchasedUpdate::class.java,
        "purchased_paid_media"
    )
}

/**
 * This object represents an incoming update containing a new poll state.
 *
 * @property poll New poll state. Bots receive only updates about manually stopped polls
 *               and polls, which are sent by the bot.
 */
data class PollUpdate(
    override val updateId: Long,
    override val poll: Poll,
) : Update() {
    override val ordinal: Int get() = 16
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId? = null

    companion object : UpdateKind<PollUpdate>(
        16, PollUpdate::class.java,
        "poll"
    )
}

/**
 * This object represents an incoming update containing a poll answer update.
 *
 * @property pollAnswer A user changed their answer in a non-anonymous poll.
 *                      Bots receive new votes only in polls that were sent by the bot itself.
 */
data class PollAnswerUpdate(
    override val updateId: Long,
    override val pollAnswer: PollAnswer,
) : Update() {
    override val ordinal: Int get() = 17
    override fun chatIdOrNull(): ChatId? = pollAnswer.voterChat?.id?.toChatId()
    override fun userIdOrNull(): ChatId? = pollAnswer.user?.id?.toChatId()

    companion object : UpdateKind<PollAnswerUpdate>(
        17, PollAnswerUpdate::class.java,
        "poll_answer"
    )
}

/**
 * This object represents an incoming update containing an update to the bot's chat member status.
 *
 * @property myChatMember The bot's chat member status was updated in a chat.
 *                        For private chats, this update is received only when the bot is blocked or unblocked by the user.
 */
data class MyChatMemberUpdate(
    override val updateId: Long,
    override val myChatMember: ChatMemberUpdated,
) : Update() {
    override val ordinal: Int get() = 18
    override fun chatIdOrNull(): ChatId = myChatMember.chat.id.toChatId()
    override fun userIdOrNull(): ChatId = myChatMember.newChatMember.userId()

    companion object : UpdateKind<MyChatMemberUpdate>(
        18, MyChatMemberUpdate::class.java,
        "my_chat_member"
    )
}

/**
 * This object represents an incoming update containing an update to a chat member's status.
 *
 * @property chatMember A chat member's status was updated in a chat.
 *                      The bot must be an administrator in the chat and must explicitly
 *                      specify "chat_member" in the list of allowed_updates to receive these updates.
 */
data class ChatMemberUpdate(
    override val updateId: Long,
    override val chatMember: ChatMemberUpdated,
) : Update() {
    override val ordinal: Int get() = 19
    override fun chatIdOrNull(): ChatId = chatMember.chat.id.toChatId()
    override fun userIdOrNull(): ChatId = chatMember.newChatMember.userId()

    companion object : UpdateKind<ChatMemberUpdate>(
        19, ChatMemberUpdate::class.java,
        "chat_member"
    )
}

/**
 * This object represents an incoming update containing a chat join request.
 *
 * @property chatJoinRequest A request to join the chat has been sent.
 *                           The bot must have the can_invite_users administrator right in the chat to receive these updates.
 */
data class ChatJoinRequestUpdate(
    override val updateId: Long,
    override val chatJoinRequest: ChatJoinRequest,
) : Update() {
    override val ordinal: Int get() = 20
    override fun chatIdOrNull(): ChatId = chatJoinRequest.chat.id.toChatId()
    override fun userIdOrNull(): ChatId = chatJoinRequest.from.id.toChatId()

    companion object : UpdateKind<ChatJoinRequestUpdate>(
        20, ChatJoinRequestUpdate::class.java,
        "chat_join_request"
    )
}

/**
 * This object represents an incoming update containing a chat boost event.
 *
 * @property chatBoost A chat boost was added or changed.
 *                     The bot must be an administrator in the chat to receive these updates.
 */
data class ChatBoostUpdate(
    override val updateId: Long,
    override val chatBoost: ChatBoostUpdated,
) : Update() {
    override val ordinal: Int get() = 21
    override fun chatIdOrNull(): ChatId = chatBoost.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = chatBoost.boost.source.userIdOrNull()

    companion object : UpdateKind<ChatBoostUpdate>(
        21, ChatBoostUpdate::class.java,
        "chat_boost"
    )
}

/**
 * This object represents an incoming update containing the removal of a chat boost.
 *
 * @property removedChatBoost A boost was removed from a chat.
 *                            The bot must be an administrator in the chat to receive these updates.
 */
data class RemovedChatBoostUpdate(
    override val updateId: Long,
    override val removedChatBoost: ChatBoostRemoved,
) : Update() {
    override val ordinal: Int get() = 22
    override fun chatIdOrNull(): ChatId = removedChatBoost.chat.id.toChatId()
    override fun userIdOrNull(): ChatId? = removedChatBoost.source.userIdOrNull()

    companion object : UpdateKind<RemovedChatBoostUpdate>(
        22, RemovedChatBoostUpdate::class.java,
        "removed_chat_boost"
    )
}



data class ManagedBotUpdate(
    override val updateId: Long,
    override val managedBot: ManagedBotUpdated
) : Update() {
    override val ordinal: Int get() = 23

    override fun chatIdOrNull(): ChatId = managedBot.bot.id.toChatId()
    override fun userIdOrNull(): ChatId = managedBot.bot.id.toChatId()

    companion object : UpdateKind<ManagedBotUpdate>(
        23, ManagedBotUpdate::class.java,
        "managed_bot"
    )
}

private fun Message.userIdOrNull(): ChatId? = from?.id?.toChatId()

private fun ChatMember.userId(): ChatId = when (this) {
    is ChatMemberAdministrator -> user.id.toChatId()
    is ChatMemberBanned -> user.id.toChatId()
    is ChatMemberLeft -> user.id.toChatId()
    is ChatMemberMember -> user.id.toChatId()
    is ChatMemberOwner -> user.id.toChatId()
    is ChatMemberRestricted -> user.id.toChatId()
}

private fun ChatBoostSource.userIdOrNull(): ChatId? = when (this) {
    is ChatBoostSourceGiftCode -> user.id.toChatId()
    is ChatBoostSourceGiveaway -> user?.id?.toChatId()
    is ChatBoostSourcePremium -> user.id.toChatId()
}