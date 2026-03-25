@file:Suppress("UNCHECKED_CAST")

package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.TypeHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScopeImpl
import org.bezsahara.kittybot.telegram.classes.core.update.*

class TypeHandlerImpl<T: Update>(
    expect: UpdateKind<T>,
    private val testUpdate: (T) -> Boolean,
    private val onSuccess: suspend HandlerScope<T>.() -> Unit,
) : TypeHandler<T>(expect) {
    override suspend fun handleUpdateTyped(
        update: T,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        if (!testUpdate(update)) return Decision.Next
        HandlerScopeImpl(update, bot, handlerContext).apply {
            onSuccess(this)
        }
        return Decision.Consumed
    }
}

inline fun <reified T: Update> FelineDispatcher.handleTypeOf(
    noinline check: (T) -> Boolean,
    noinline onSuccess: suspend HandlerScope<T>.() -> Unit
) {
    addHandler(
        TypeHandlerImpl(typesMapToKind[T::class.java]!! as UpdateKind<T>, check, onSuccess)
    )
}

val typesMapToKind: Map<Class<out Update>, UpdateKind<*>> = mapOf(
    MessageUpdate::class.java to MessageUpdate,
    EditedMessageUpdate::class.java to EditedMessageUpdate,
    ChannelPostUpdate::class.java to ChannelPostUpdate,
    EditedChannelPostUpdate::class.java to EditedChannelPostUpdate,
    BusinessConnectionUpdate::class.java to BusinessConnectionUpdate,
    BusinessMessageUpdate::class.java to BusinessMessageUpdate,
    EditedBusinessMessageUpdate::class.java to EditedBusinessMessageUpdate,
    DeletedBusinessMessagesUpdate::class.java to DeletedBusinessMessagesUpdate,
    MessageReactionUpdate::class.java to MessageReactionUpdate,
    MessageReactionCountUpdate::class.java to MessageReactionCountUpdate,
    InlineQueryUpdate::class.java to InlineQueryUpdate,
    ChosenInlineResultUpdate::class.java to ChosenInlineResultUpdate,
    CallbackQueryUpdate::class.java to CallbackQueryUpdate,
    ShippingQueryUpdate::class.java to ShippingQueryUpdate,
    PreCheckoutQueryUpdate::class.java to PreCheckoutQueryUpdate,
    PaidMediaPurchasedUpdate::class.java to PaidMediaPurchasedUpdate,
    PollUpdate::class.java to PollUpdate,
    PollAnswerUpdate::class.java to PollAnswerUpdate,
    MyChatMemberUpdate::class.java to MyChatMemberUpdate,
    ChatMemberUpdate::class.java to ChatMemberUpdate,
    ChatJoinRequestUpdate::class.java to ChatJoinRequestUpdate,
    ChatBoostUpdate::class.java to ChatBoostUpdate,
    RemovedChatBoostUpdate::class.java to RemovedChatBoostUpdate,
)
