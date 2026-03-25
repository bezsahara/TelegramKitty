package org.bezsahara.kittybot.telegram.classes.core.update

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