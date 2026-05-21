@file:OptIn(ExperimentalContracts::class)

package org.bezsahara.kittybot.telegram.classes.core.update

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


fun Update.asMessageUpdate(): MessageUpdate {
    contract {
        returns() implies (this@asMessageUpdate is MessageUpdate)
    }
    return this as MessageUpdate
}

fun Update.asEditedMessageUpdate(): EditedMessageUpdate {
    contract {
        returns() implies (this@asEditedMessageUpdate is EditedMessageUpdate)
    }
    return this as EditedMessageUpdate
}

fun Update.asChannelPostUpdate(): ChannelPostUpdate {
    contract {
        returns() implies (this@asChannelPostUpdate is ChannelPostUpdate)
    }
    return this as ChannelPostUpdate
}

fun Update.asEditedChannelPostUpdate(): EditedChannelPostUpdate {
    contract {
        returns() implies (this@asEditedChannelPostUpdate is EditedChannelPostUpdate)
    }
    return this as EditedChannelPostUpdate
}

fun Update.asBusinessConnectionUpdate(): BusinessConnectionUpdate {
    contract {
        returns() implies (this@asBusinessConnectionUpdate is BusinessConnectionUpdate)
    }
    return this as BusinessConnectionUpdate
}

fun Update.asBusinessMessageUpdate(): BusinessMessageUpdate {
    contract {
        returns() implies (this@asBusinessMessageUpdate is BusinessMessageUpdate)
    }
    return this as BusinessMessageUpdate
}

fun Update.asEditedBusinessMessageUpdate(): EditedBusinessMessageUpdate {
    contract {
        returns() implies (this@asEditedBusinessMessageUpdate is EditedBusinessMessageUpdate)
    }
    return this as EditedBusinessMessageUpdate
}

fun Update.asDeletedBusinessMessagesUpdate(): DeletedBusinessMessagesUpdate {
    contract {
        returns() implies (this@asDeletedBusinessMessagesUpdate is DeletedBusinessMessagesUpdate)
    }
    return this as DeletedBusinessMessagesUpdate
}

fun Update.asMessageReactionUpdate(): MessageReactionUpdate {
    contract {
        returns() implies (this@asMessageReactionUpdate is MessageReactionUpdate)
    }
    return this as MessageReactionUpdate
}

fun Update.asMessageReactionCountUpdate(): MessageReactionCountUpdate {
    contract {
        returns() implies (this@asMessageReactionCountUpdate is MessageReactionCountUpdate)
    }
    return this as MessageReactionCountUpdate
}

fun Update.asInlineQueryUpdate(): InlineQueryUpdate {
    contract {
        returns() implies (this@asInlineQueryUpdate is InlineQueryUpdate)
    }
    return this as InlineQueryUpdate
}

fun Update.asChosenInlineResultUpdate(): ChosenInlineResultUpdate {
    contract {
        returns() implies (this@asChosenInlineResultUpdate is ChosenInlineResultUpdate)
    }
    return this as ChosenInlineResultUpdate
}

fun Update.asCallbackQueryUpdate(): CallbackQueryUpdate {
    contract {
        returns() implies (this@asCallbackQueryUpdate is CallbackQueryUpdate)
    }
    return this as CallbackQueryUpdate
}

fun Update.asShippingQueryUpdate(): ShippingQueryUpdate {
    contract {
        returns() implies (this@asShippingQueryUpdate is ShippingQueryUpdate)
    }
    return this as ShippingQueryUpdate
}

fun Update.asPreCheckoutQueryUpdate(): PreCheckoutQueryUpdate {
    contract {
        returns() implies (this@asPreCheckoutQueryUpdate is PreCheckoutQueryUpdate)
    }
    return this as PreCheckoutQueryUpdate
}

fun Update.asPaidMediaPurchasedUpdate(): PaidMediaPurchasedUpdate {
    contract {
        returns() implies (this@asPaidMediaPurchasedUpdate is PaidMediaPurchasedUpdate)
    }
    return this as PaidMediaPurchasedUpdate
}

fun Update.asPollUpdate(): PollUpdate {
    contract {
        returns() implies (this@asPollUpdate is PollUpdate)
    }
    return this as PollUpdate
}

fun Update.asPollAnswerUpdate(): PollAnswerUpdate {
    contract {
        returns() implies (this@asPollAnswerUpdate is PollAnswerUpdate)
    }
    return this as PollAnswerUpdate
}

fun Update.asMyChatMemberUpdate(): MyChatMemberUpdate {
    contract {
        returns() implies (this@asMyChatMemberUpdate is MyChatMemberUpdate)
    }
    return this as MyChatMemberUpdate
}

fun Update.asChatMemberUpdate(): ChatMemberUpdate {
    contract {
        returns() implies (this@asChatMemberUpdate is ChatMemberUpdate)
    }
    return this as ChatMemberUpdate
}

fun Update.asChatJoinRequestUpdate(): ChatJoinRequestUpdate {
    contract {
        returns() implies (this@asChatJoinRequestUpdate is ChatJoinRequestUpdate)
    }
    return this as ChatJoinRequestUpdate
}

fun Update.asChatBoostUpdate(): ChatBoostUpdate {
    contract {
        returns() implies (this@asChatBoostUpdate is ChatBoostUpdate)
    }
    return this as ChatBoostUpdate
}

fun Update.asRemovedChatBoostUpdate(): RemovedChatBoostUpdate {
    contract {
        returns() implies (this@asRemovedChatBoostUpdate is RemovedChatBoostUpdate)
    }
    return this as RemovedChatBoostUpdate
}


fun Update.asMessageUpdateOrNull(): MessageUpdate? {
    contract {
        returnsNotNull() implies (this@asMessageUpdateOrNull is MessageUpdate)
    }
    return if (javaClass === MessageUpdate::class.java) this as MessageUpdate else null
}

fun Update.asEditedMessageUpdateOrNull(): EditedMessageUpdate? {
    contract {
        returnsNotNull() implies (this@asEditedMessageUpdateOrNull is EditedMessageUpdate)
    }
    return if (javaClass === EditedMessageUpdate::class.java) this as EditedMessageUpdate else null
}

fun Update.asChannelPostUpdateOrNull(): ChannelPostUpdate? {
    contract {
        returnsNotNull() implies (this@asChannelPostUpdateOrNull is ChannelPostUpdate)
    }
    return if (javaClass === ChannelPostUpdate::class.java) this as ChannelPostUpdate else null
}

fun Update.asEditedChannelPostUpdateOrNull(): EditedChannelPostUpdate? {
    contract {
        returnsNotNull() implies (this@asEditedChannelPostUpdateOrNull is EditedChannelPostUpdate)
    }
    return if (javaClass === EditedChannelPostUpdate::class.java) this as EditedChannelPostUpdate else null
}

fun Update.asBusinessConnectionUpdateOrNull(): BusinessConnectionUpdate? {
    contract {
        returnsNotNull() implies (this@asBusinessConnectionUpdateOrNull is BusinessConnectionUpdate)
    }
    return if (javaClass === BusinessConnectionUpdate::class.java) this as BusinessConnectionUpdate else null
}

fun Update.asBusinessMessageUpdateOrNull(): BusinessMessageUpdate? {
    contract {
        returnsNotNull() implies (this@asBusinessMessageUpdateOrNull is BusinessMessageUpdate)
    }
    return if (javaClass === BusinessMessageUpdate::class.java) this as BusinessMessageUpdate else null
}

fun Update.asEditedBusinessMessageUpdateOrNull(): EditedBusinessMessageUpdate? {
    contract {
        returnsNotNull() implies (this@asEditedBusinessMessageUpdateOrNull is EditedBusinessMessageUpdate)
    }
    return if (javaClass === EditedBusinessMessageUpdate::class.java) this as EditedBusinessMessageUpdate else null
}

fun Update.asDeletedBusinessMessagesUpdateOrNull(): DeletedBusinessMessagesUpdate? {
    contract {
        returnsNotNull() implies (this@asDeletedBusinessMessagesUpdateOrNull is DeletedBusinessMessagesUpdate)
    }
    return if (javaClass === DeletedBusinessMessagesUpdate::class.java) this as DeletedBusinessMessagesUpdate else null
}

fun Update.asMessageReactionUpdateOrNull(): MessageReactionUpdate? {
    contract {
        returnsNotNull() implies (this@asMessageReactionUpdateOrNull is MessageReactionUpdate)
    }
    return if (javaClass === MessageReactionUpdate::class.java) this as MessageReactionUpdate else null
}

fun Update.asMessageReactionCountUpdateOrNull(): MessageReactionCountUpdate? {
    contract {
        returnsNotNull() implies (this@asMessageReactionCountUpdateOrNull is MessageReactionCountUpdate)
    }
    return if (javaClass === MessageReactionCountUpdate::class.java) this as MessageReactionCountUpdate else null
}

fun Update.asInlineQueryUpdateOrNull(): InlineQueryUpdate? {
    contract {
        returnsNotNull() implies (this@asInlineQueryUpdateOrNull is InlineQueryUpdate)
    }
    return if (javaClass === InlineQueryUpdate::class.java) this as InlineQueryUpdate else null
}

fun Update.asChosenInlineResultUpdateOrNull(): ChosenInlineResultUpdate? {
    contract {
        returnsNotNull() implies (this@asChosenInlineResultUpdateOrNull is ChosenInlineResultUpdate)
    }
    return if (javaClass === ChosenInlineResultUpdate::class.java) this as ChosenInlineResultUpdate else null
}

fun Update.asCallbackQueryUpdateOrNull(): CallbackQueryUpdate? {
    contract {
        returnsNotNull() implies (this@asCallbackQueryUpdateOrNull is CallbackQueryUpdate)
    }
    return if (javaClass === CallbackQueryUpdate::class.java) this as CallbackQueryUpdate else null
}

fun Update.asShippingQueryUpdateOrNull(): ShippingQueryUpdate? {
    contract {
        returnsNotNull() implies (this@asShippingQueryUpdateOrNull is ShippingQueryUpdate)
    }
    return if (javaClass === ShippingQueryUpdate::class.java) this as ShippingQueryUpdate else null
}

fun Update.asPreCheckoutQueryUpdateOrNull(): PreCheckoutQueryUpdate? {
    contract {
        returnsNotNull() implies (this@asPreCheckoutQueryUpdateOrNull is PreCheckoutQueryUpdate)
    }
    return if (javaClass === PreCheckoutQueryUpdate::class.java) this as PreCheckoutQueryUpdate else null
}

fun Update.asPaidMediaPurchasedUpdateOrNull(): PaidMediaPurchasedUpdate? {
    contract {
        returnsNotNull() implies (this@asPaidMediaPurchasedUpdateOrNull is PaidMediaPurchasedUpdate)
    }
    return if (javaClass === PaidMediaPurchasedUpdate::class.java) this as PaidMediaPurchasedUpdate else null
}

fun Update.asPollUpdateOrNull(): PollUpdate? {
    contract {
        returnsNotNull() implies (this@asPollUpdateOrNull is PollUpdate)
    }
    return if (javaClass === PollUpdate::class.java) this as PollUpdate else null
}

fun Update.asPollAnswerUpdateOrNull(): PollAnswerUpdate? {
    contract {
        returnsNotNull() implies (this@asPollAnswerUpdateOrNull is PollAnswerUpdate)
    }
    return if (javaClass === PollAnswerUpdate::class.java) this as PollAnswerUpdate else null
}

fun Update.asMyChatMemberUpdateOrNull(): MyChatMemberUpdate? {
    contract {
        returnsNotNull() implies (this@asMyChatMemberUpdateOrNull is MyChatMemberUpdate)
    }
    return if (javaClass === MyChatMemberUpdate::class.java) this as MyChatMemberUpdate else null
}

fun Update.asChatMemberUpdateOrNull(): ChatMemberUpdate? {
    contract {
        returnsNotNull() implies (this@asChatMemberUpdateOrNull is ChatMemberUpdate)
    }
    return if (javaClass === ChatMemberUpdate::class.java) this as ChatMemberUpdate else null
}

fun Update.asChatJoinRequestUpdateOrNull(): ChatJoinRequestUpdate? {
    contract {
        returnsNotNull() implies (this@asChatJoinRequestUpdateOrNull is ChatJoinRequestUpdate)
    }
    return if (javaClass === ChatJoinRequestUpdate::class.java) this as ChatJoinRequestUpdate else null
}

fun Update.asChatBoostUpdateOrNull(): ChatBoostUpdate? {
    contract {
        returnsNotNull() implies (this@asChatBoostUpdateOrNull is ChatBoostUpdate)
    }
    return if (javaClass === ChatBoostUpdate::class.java) this as ChatBoostUpdate else null
}

fun Update.asRemovedChatBoostUpdateOrNull(): RemovedChatBoostUpdate? {
    contract {
        returnsNotNull() implies (this@asRemovedChatBoostUpdateOrNull is RemovedChatBoostUpdate)
    }
    return if (javaClass === RemovedChatBoostUpdate::class.java) this as RemovedChatBoostUpdate else null
}




@OptIn(ExperimentalContracts::class)
fun Update.isMessageUpdate(): Boolean {
    contract {
        returns(true) implies (this@isMessageUpdate is MessageUpdate)
    }
    return this is MessageUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isEditedMessageUpdate(): Boolean {
    contract {
        returns(true) implies (this@isEditedMessageUpdate is EditedMessageUpdate)
    }
    return this is EditedMessageUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isChannelPostUpdate(): Boolean {
    contract {
        returns(true) implies (this@isChannelPostUpdate is ChannelPostUpdate)
    }
    return this is ChannelPostUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isEditedChannelPostUpdate(): Boolean {
    contract {
        returns(true) implies (this@isEditedChannelPostUpdate is EditedChannelPostUpdate)
    }
    return this is EditedChannelPostUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isBusinessConnectionUpdate(): Boolean {
    contract {
        returns(true) implies (this@isBusinessConnectionUpdate is BusinessConnectionUpdate)
    }
    return this is BusinessConnectionUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isBusinessMessageUpdate(): Boolean {
    contract {
        returns(true) implies (this@isBusinessMessageUpdate is BusinessMessageUpdate)
    }
    return this is BusinessMessageUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isEditedBusinessMessageUpdate(): Boolean {
    contract {
        returns(true) implies (this@isEditedBusinessMessageUpdate is EditedBusinessMessageUpdate)
    }
    return this is EditedBusinessMessageUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isDeletedBusinessMessagesUpdate(): Boolean {
    contract {
        returns(true) implies (this@isDeletedBusinessMessagesUpdate is DeletedBusinessMessagesUpdate)
    }
    return this is DeletedBusinessMessagesUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isMessageReactionUpdate(): Boolean {
    contract {
        returns(true) implies (this@isMessageReactionUpdate is MessageReactionUpdate)
    }
    return this is MessageReactionUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isMessageReactionCountUpdate(): Boolean {
    contract {
        returns(true) implies (this@isMessageReactionCountUpdate is MessageReactionCountUpdate)
    }
    return this is MessageReactionCountUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isInlineQueryUpdate(): Boolean {
    contract {
        returns(true) implies (this@isInlineQueryUpdate is InlineQueryUpdate)
    }
    return this is InlineQueryUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isChosenInlineResultUpdate(): Boolean {
    contract {
        returns(true) implies (this@isChosenInlineResultUpdate is ChosenInlineResultUpdate)
    }
    return this is ChosenInlineResultUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isCallbackQueryUpdate(): Boolean {
    contract {
        returns(true) implies (this@isCallbackQueryUpdate is CallbackQueryUpdate)
    }
    return this is CallbackQueryUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isShippingQueryUpdate(): Boolean {
    contract {
        returns(true) implies (this@isShippingQueryUpdate is ShippingQueryUpdate)
    }
    return this is ShippingQueryUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isPreCheckoutQueryUpdate(): Boolean {
    contract {
        returns(true) implies (this@isPreCheckoutQueryUpdate is PreCheckoutQueryUpdate)
    }
    return this is PreCheckoutQueryUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isPaidMediaPurchasedUpdate(): Boolean {
    contract {
        returns(true) implies (this@isPaidMediaPurchasedUpdate is PaidMediaPurchasedUpdate)
    }
    return this is PaidMediaPurchasedUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isPollUpdate(): Boolean {
    contract {
        returns(true) implies (this@isPollUpdate is PollUpdate)
    }
    return this is PollUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isPollAnswerUpdate(): Boolean {
    contract {
        returns(true) implies (this@isPollAnswerUpdate is PollAnswerUpdate)
    }
    return this is PollAnswerUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isMyChatMemberUpdate(): Boolean {
    contract {
        returns(true) implies (this@isMyChatMemberUpdate is MyChatMemberUpdate)
    }
    return this is MyChatMemberUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isChatMemberUpdate(): Boolean {
    contract {
        returns(true) implies (this@isChatMemberUpdate is ChatMemberUpdate)
    }
    return this is ChatMemberUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isChatJoinRequestUpdate(): Boolean {
    contract {
        returns(true) implies (this@isChatJoinRequestUpdate is ChatJoinRequestUpdate)
    }
    return this is ChatJoinRequestUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isChatBoostUpdate(): Boolean {
    contract {
        returns(true) implies (this@isChatBoostUpdate is ChatBoostUpdate)
    }
    return this is ChatBoostUpdate
}
@OptIn(ExperimentalContracts::class)
fun Update.isRemovedChatBoostUpdate(): Boolean {
    contract {
        returns(true) implies (this@isRemovedChatBoostUpdate is RemovedChatBoostUpdate)
    }
    return this is RemovedChatBoostUpdate
}