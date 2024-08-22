package org.bezsahara.kittybot.telegram.classes.updates

import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.business.BusinessMessagesDeleted
import org.bezsahara.kittybot.telegram.classes.chats.boosts.ChatBoostRemoved
import org.bezsahara.kittybot.telegram.classes.chats.boosts.ChatBoostUpdated
import org.bezsahara.kittybot.telegram.classes.chats.members.ChatMemberUpdated
import org.bezsahara.kittybot.telegram.classes.chats.requests.ChatJoinRequest
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.messages.reactions.MessageReactionCountUpdated
import org.bezsahara.kittybot.telegram.classes.messages.reactions.MessageReactionUpdated
import org.bezsahara.kittybot.telegram.classes.payments.PreCheckoutQuery
import org.bezsahara.kittybot.telegram.classes.payments.ShippingQuery
import org.bezsahara.kittybot.telegram.classes.polls.Poll
import org.bezsahara.kittybot.telegram.classes.polls.PollAnswer
import org.bezsahara.kittybot.telegram.classes.queries.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.queries.InlineQuery
import org.bezsahara.kittybot.telegram.classes.queries.results.ChosenInlineResult

/**
 * This object represents an incoming update containing a new message.
 *
 * @property message New incoming message of any kind - text, photo, sticker, etc.
 */
data class MessageUpdate(
    override val updateId: Long,
    override val message: Message
) : Update()

/**
 * This object represents an incoming update containing an edited message.
 *
 * @property editedMessage New version of a message that is known to the bot and was edited.
 *                         This update may at times be triggered by changes to message fields
 *                         that are either unavailable or not actively used by your bot.
 */
data class EditedMessageUpdate(
    override val updateId: Long,
    override val editedMessage: Message
) : Update()

/**
 * This object represents an incoming update containing a new channel post.
 *
 * @property channelPost New incoming channel post of any kind - text, photo, sticker, etc.
 */
data class ChannelPostUpdate(
    override val updateId: Long,
    override val channelPost: Message
) : Update()

/**
 * This object represents an incoming update containing an edited channel post.
 *
 * @property editedChannelPost New version of a channel post that is known to the bot and was edited.
 *                             This update may at times be triggered by changes to message fields
 *                             that are either unavailable or not actively used by your bot.
 */
data class EditedChannelPostUpdate(
    override val updateId: Long,
    override val editedChannelPost: Message
) : Update()

/**
 * This object represents an incoming update containing a business connection event.
 *
 * @property businessConnection The bot was connected to or disconnected from a business account,
 *                              or a user edited an existing connection with the bot.
 */
data class BusinessConnectionUpdate(
    override val updateId: Long,
    override val businessConnection: BusinessConnection
) : Update()

/**
 * This object represents an incoming update containing a new message from a connected business account.
 *
 * @property businessMessage New message from a connected business account.
 */
data class BusinessMessageUpdate(
    override val updateId: Long,
    override val businessMessage: Message
) : Update()

/**
 * This object represents an incoming update containing an edited message from a connected business account.
 *
 * @property editedBusinessMessage New version of a message from a connected business account.
 */
data class EditedBusinessMessageUpdate(
    override val updateId: Long,
    override val editedBusinessMessage: Message
) : Update()

/**
 * This object represents an incoming update containing deleted messages from a connected business account.
 *
 * @property deletedBusinessMessages Messages were deleted from a connected business account.
 */
data class DeletedBusinessMessagesUpdate(
    override val updateId: Long,
    override val deletedBusinessMessages: BusinessMessagesDeleted
) : Update()

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
    override val messageReaction: MessageReactionUpdated
) : Update()

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
    override val messageReactionCount: MessageReactionCountUpdated
) : Update()

/**
 * This object represents an incoming update containing a new inline query.
 *
 * @property inlineQuery New incoming inline query.
 */
data class InlineQueryUpdate(
    override val updateId: Long,
    override val inlineQuery: InlineQuery
) : Update()

/**
 * This object represents an incoming update containing a chosen inline result.
 *
 * @property chosenInlineResult The result of an inline query that was chosen by a user and sent to their chat partner.
 *                              Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
 */
data class ChosenInlineResultUpdate(
    override val updateId: Long,
    override val chosenInlineResult: ChosenInlineResult
) : Update()

/**
 * This object represents an incoming update containing a new callback query.
 *
 * @property callbackQuery New incoming callback query.
 */
data class CallbackQueryUpdate(
    override val updateId: Long,
    override val callbackQuery: CallbackQuery
) : Update()

/**
 * This object represents an incoming update containing a new shipping query.
 *
 * @property shippingQuery New incoming shipping query. Only for invoices with flexible price.
 */
data class ShippingQueryUpdate(
    override val updateId: Long,
    override val shippingQuery: ShippingQuery
) : Update()

/**
 * This object represents an incoming update containing a new pre-checkout query.
 *
 * @property preCheckoutQuery New incoming pre-checkout query. Contains full information about checkout.
 */
data class PreCheckoutQueryUpdate(
    override val updateId: Long,
    override val preCheckoutQuery: PreCheckoutQuery
) : Update()

/**
 * This object represents an incoming update containing a new poll state.
 *
 * @property poll New poll state. Bots receive only updates about manually stopped polls
 *               and polls, which are sent by the bot.
 */
data class PollUpdate(
    override val updateId: Long,
    override val poll: Poll
) : Update()

/**
 * This object represents an incoming update containing a poll answer update.
 *
 * @property pollAnswer A user changed their answer in a non-anonymous poll.
 *                      Bots receive new votes only in polls that were sent by the bot itself.
 */
data class PollAnswerUpdate(
    override val updateId: Long,
    override val pollAnswer: PollAnswer
) : Update()

/**
 * This object represents an incoming update containing an update to the bot's chat member status.
 *
 * @property myChatMember The bot's chat member status was updated in a chat.
 *                        For private chats, this update is received only when the bot is blocked or unblocked by the user.
 */
data class MyChatMemberUpdate(
    override val updateId: Long,
    override val myChatMember: ChatMemberUpdated
) : Update()

/**
 * This object represents an incoming update containing an update to a chat member's status.
 *
 * @property chatMember A chat member's status was updated in a chat.
 *                      The bot must be an administrator in the chat and must explicitly
 *                      specify "chat_member" in the list of allowed_updates to receive these updates.
 */
data class ChatMemberUpdate(
    override val updateId: Long,
    override val chatMember: ChatMemberUpdated
) : Update()

/**
 * This object represents an incoming update containing a chat join request.
 *
 * @property chatJoinRequest A request to join the chat has been sent.
 *                           The bot must have the can_invite_users administrator right in the chat to receive these updates.
 */
data class ChatJoinRequestUpdate(
    override val updateId: Long,
    override val chatJoinRequest: ChatJoinRequest
) : Update()

/**
 * This object represents an incoming update containing a chat boost event.
 *
 * @property chatBoost A chat boost was added or changed.
 *                     The bot must be an administrator in the chat to receive these updates.
 */
data class ChatBoostUpdate(
    override val updateId: Long,
    override val chatBoost: ChatBoostUpdated
) : Update()

/**
 * This object represents an incoming update containing the removal of a chat boost.
 *
 * @property removedChatBoost A boost was removed from a chat.
 *                            The bot must be an administrator in the chat to receive these updates.
 */
data class RemovedChatBoostUpdate(
    override val updateId: Long,
    override val removedChatBoost: ChatBoostRemoved
) : Update()
