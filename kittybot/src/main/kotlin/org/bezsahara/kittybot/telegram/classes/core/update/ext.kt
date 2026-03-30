package org.bezsahara.kittybot.telegram.classes.core.update

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private inline fun <reified T : Update> Update.asType(): T =
    this as? T ?: error("Expected ${T::class.simpleName}, got ${this::class.simpleName}")

fun Update.asMessageUpdate(): MessageUpdate = asType()

fun Update.asEditedMessageUpdate(): EditedMessageUpdate = asType()

fun Update.asChannelPostUpdate(): ChannelPostUpdate = asType()

fun Update.asEditedChannelPostUpdate(): EditedChannelPostUpdate = asType()

fun Update.asBusinessConnectionUpdate(): BusinessConnectionUpdate = asType()

fun Update.asBusinessMessageUpdate(): BusinessMessageUpdate = asType()

fun Update.asEditedBusinessMessageUpdate(): EditedBusinessMessageUpdate = asType()

fun Update.asDeletedBusinessMessagesUpdate(): DeletedBusinessMessagesUpdate = asType()

fun Update.asMessageReactionUpdate(): MessageReactionUpdate = asType()

fun Update.asMessageReactionCountUpdate(): MessageReactionCountUpdate = asType()

fun Update.asInlineQueryUpdate(): InlineQueryUpdate = asType()

fun Update.asChosenInlineResultUpdate(): ChosenInlineResultUpdate = asType()

fun Update.asCallbackQueryUpdate(): CallbackQueryUpdate = asType()

fun Update.asShippingQueryUpdate(): ShippingQueryUpdate = asType()

fun Update.asPreCheckoutQueryUpdate(): PreCheckoutQueryUpdate = asType()

fun Update.asPaidMediaPurchasedUpdate(): PaidMediaPurchasedUpdate = asType()

fun Update.asPollUpdate(): PollUpdate = asType()

fun Update.asPollAnswerUpdate(): PollAnswerUpdate = asType()

fun Update.asMyChatMemberUpdate(): MyChatMemberUpdate = asType()

fun Update.asChatMemberUpdate(): ChatMemberUpdate = asType()

fun Update.asChatJoinRequestUpdate(): ChatJoinRequestUpdate = asType()

fun Update.asChatBoostUpdate(): ChatBoostUpdate = asType()

fun Update.asRemovedChatBoostUpdate(): RemovedChatBoostUpdate = asType()


fun Update.asMessageUpdateOrNull(): MessageUpdate? = this as? MessageUpdate

fun Update.asEditedMessageUpdateOrNull(): EditedMessageUpdate? = this as? EditedMessageUpdate

fun Update.asChannelPostUpdateOrNull(): ChannelPostUpdate? = this as? ChannelPostUpdate

fun Update.asEditedChannelPostUpdateOrNull(): EditedChannelPostUpdate? = this as? EditedChannelPostUpdate

fun Update.asBusinessConnectionUpdateOrNull(): BusinessConnectionUpdate? = this as? BusinessConnectionUpdate

fun Update.asBusinessMessageUpdateOrNull(): BusinessMessageUpdate? = this as? BusinessMessageUpdate

fun Update.asEditedBusinessMessageUpdateOrNull(): EditedBusinessMessageUpdate? = this as? EditedBusinessMessageUpdate

fun Update.asDeletedBusinessMessagesUpdateOrNull(): DeletedBusinessMessagesUpdate? = this as? DeletedBusinessMessagesUpdate

fun Update.asMessageReactionUpdateOrNull(): MessageReactionUpdate? = this as? MessageReactionUpdate

fun Update.asMessageReactionCountUpdateOrNull(): MessageReactionCountUpdate? = this as? MessageReactionCountUpdate

fun Update.asInlineQueryUpdateOrNull(): InlineQueryUpdate? = this as? InlineQueryUpdate

fun Update.asChosenInlineResultUpdateOrNull(): ChosenInlineResultUpdate? = this as? ChosenInlineResultUpdate

fun Update.asCallbackQueryUpdateOrNull(): CallbackQueryUpdate? = this as? CallbackQueryUpdate

fun Update.asShippingQueryUpdateOrNull(): ShippingQueryUpdate? = this as? ShippingQueryUpdate

fun Update.asPreCheckoutQueryUpdateOrNull(): PreCheckoutQueryUpdate? = this as? PreCheckoutQueryUpdate

fun Update.asPaidMediaPurchasedUpdateOrNull(): PaidMediaPurchasedUpdate? = this as? PaidMediaPurchasedUpdate

fun Update.asPollUpdateOrNull(): PollUpdate? = this as? PollUpdate

fun Update.asPollAnswerUpdateOrNull(): PollAnswerUpdate? = this as? PollAnswerUpdate

fun Update.asMyChatMemberUpdateOrNull(): MyChatMemberUpdate? = this as? MyChatMemberUpdate

fun Update.asChatMemberUpdateOrNull(): ChatMemberUpdate? = this as? ChatMemberUpdate

fun Update.asChatJoinRequestUpdateOrNull(): ChatJoinRequestUpdate? = this as? ChatJoinRequestUpdate

fun Update.asChatBoostUpdateOrNull(): ChatBoostUpdate? = this as? ChatBoostUpdate

fun Update.asRemovedChatBoostUpdateOrNull(): RemovedChatBoostUpdate? = this as? RemovedChatBoostUpdate




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