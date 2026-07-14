package org.bezsahara.kittybot.bot.updates.furballs

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.*

abstract class UpdateVisitor {
    open suspend fun onMessageUpdate(bot: KittyBot, update: MessageUpdate) {}
    open suspend fun onEditedMessageUpdate(bot: KittyBot, update: EditedMessageUpdate) {}
    open suspend fun onChannelPostUpdate(bot: KittyBot, update: ChannelPostUpdate) {}
    open suspend fun onEditedChannelPostUpdate(bot: KittyBot, update: EditedChannelPostUpdate) {}
    open suspend fun onBusinessConnectionUpdate(bot: KittyBot, update: BusinessConnectionUpdate) {}
    open suspend fun onBusinessMessageUpdate(bot: KittyBot, update: BusinessMessageUpdate) {}
    open suspend fun onEditedBusinessMessageUpdate(bot: KittyBot, update: EditedBusinessMessageUpdate) {}
    open suspend fun onDeletedBusinessMessagesUpdate(bot: KittyBot, update: DeletedBusinessMessagesUpdate) {}
    open suspend fun onMessageReactionUpdate(bot: KittyBot, update: MessageReactionUpdate) {}
    open suspend fun onMessageReactionCountUpdate(bot: KittyBot, update: MessageReactionCountUpdate) {}
    open suspend fun onInlineQueryUpdate(bot: KittyBot, update: InlineQueryUpdate) {}
    open suspend fun onChosenInlineResultUpdate(bot: KittyBot, update: ChosenInlineResultUpdate) {}
    open suspend fun onCallbackQueryUpdate(bot: KittyBot, update: CallbackQueryUpdate) {}
    open suspend fun onShippingQueryUpdate(bot: KittyBot, update: ShippingQueryUpdate) {}
    open suspend fun onPreCheckoutQueryUpdate(bot: KittyBot, update: PreCheckoutQueryUpdate) {}
    open suspend fun onPaidMediaPurchasedUpdate(bot: KittyBot, update: PaidMediaPurchasedUpdate) {}
    open suspend fun onPollUpdate(bot: KittyBot, update: PollUpdate) {}
    open suspend fun onPollAnswerUpdate(bot: KittyBot, update: PollAnswerUpdate) {}
    open suspend fun onMyChatMemberUpdate(bot: KittyBot, update: MyChatMemberUpdate) {}
    open suspend fun onChatMemberUpdate(bot: KittyBot, update: ChatMemberUpdate) {}
    open suspend fun onChatJoinRequestUpdate(bot: KittyBot, update: ChatJoinRequestUpdate) {}
    open suspend fun onChatBoostUpdate(bot: KittyBot, update: ChatBoostUpdate) {}
    open suspend fun onRemovedChatBoostUpdate(bot: KittyBot, update: RemovedChatBoostUpdate) {}
    open suspend fun onManagedBotUpdate(bot: KittyBot, update: ManagedBotUpdate) {}
    open suspend fun onUnknownUpdate(bot: KittyBot, update: UnknownUpdate) {}
    open suspend fun onSyntheticUpdate(bot: KittyBot, update: SyntheticUpdate) {}

    open suspend fun onUpdate(bot: KittyBot, update: Update) {
        when (update.ordinal) {
            0  -> return onMessageUpdate(bot, update as MessageUpdate)
            1  -> return onEditedMessageUpdate(bot, update as EditedMessageUpdate)
            2  -> return onChannelPostUpdate(bot, update as ChannelPostUpdate)
            3  -> return onEditedChannelPostUpdate(bot, update as EditedChannelPostUpdate)
            4  -> return onBusinessConnectionUpdate(bot, update as BusinessConnectionUpdate)
            5  -> return onBusinessMessageUpdate(bot, update as BusinessMessageUpdate)
            6  -> return onEditedBusinessMessageUpdate(bot, update as EditedBusinessMessageUpdate)
            7  -> return onDeletedBusinessMessagesUpdate(bot, update as DeletedBusinessMessagesUpdate)
            8  -> return onMessageReactionUpdate(bot, update as MessageReactionUpdate)
            9  -> return onMessageReactionCountUpdate(bot, update as MessageReactionCountUpdate)
            10 -> return onInlineQueryUpdate(bot, update as InlineQueryUpdate)
            11 -> return onChosenInlineResultUpdate(bot, update as ChosenInlineResultUpdate)
            12 -> return onCallbackQueryUpdate(bot, update as CallbackQueryUpdate)
            13 -> return onShippingQueryUpdate(bot, update as ShippingQueryUpdate)
            14 -> return onPreCheckoutQueryUpdate(bot, update as PreCheckoutQueryUpdate)
            15 -> return onPaidMediaPurchasedUpdate(bot, update as PaidMediaPurchasedUpdate)
            16 -> return onPollUpdate(bot, update as PollUpdate)
            17 -> return onPollAnswerUpdate(bot, update as PollAnswerUpdate)
            18 -> return onMyChatMemberUpdate(bot, update as MyChatMemberUpdate)
            19 -> return onChatMemberUpdate(bot, update as ChatMemberUpdate)
            20 -> return onChatJoinRequestUpdate(bot, update as ChatJoinRequestUpdate)
            21 -> return onChatBoostUpdate(bot, update as ChatBoostUpdate)
            22 -> return onRemovedChatBoostUpdate(bot, update as RemovedChatBoostUpdate)
            23 -> return onManagedBotUpdate(bot, update as ManagedBotUpdate)
            24 -> return onUnknownUpdate(bot, update as UnknownUpdate)
            25 -> return onSyntheticUpdate(bot, update as SyntheticUpdate)
            else -> error("Unexpected update type ${update.javaClass.name}")
        }
    }

    // Will be run only in visitor handler
    open suspend fun handlerUpdate(bot: KittyBot, update: Update, context: HandlerContext): Decision {
        onUpdate(bot, update)
        return Decision.Consumed
    }
}
