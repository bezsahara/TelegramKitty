@file:Suppress("UNCHECKED_CAST")

package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.TypeHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScopeImpl
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds

class TypeHandlerImpl<T : Update>(
    expect: UpdKind,
    private val testUpdate: (T, HandlerContext) -> Boolean,
    private val onSuccess: suspend HandlerScope<T>.() -> Unit,
) : TypeHandler<T>(expect) {
    override suspend fun handleUpdateTyped(
        update: T,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        if (!testUpdate(update, handlerContext)) return Decision.Next
        return apply(HandlerScopeImpl(update, bot, handlerContext))
    }

    private suspend fun apply(scope: HandlerScopeImpl<T>): Decision {
        onSuccess.invoke(scope)
        return Decision.Consumed
    }
}

inline fun <reified T : Update> FelineDispatcher.handleTypeOf(
    crossinline check: (T) -> Boolean,
    noinline onSuccess: suspend HandlerScope<T>.() -> Unit,
) {
    handleTypeOf({ u, _ -> check.invoke(u) }, onSuccess)
}

inline fun <reified T : Update> FelineDispatcher.handleTypeOf(
    noinline check: (T, HandlerContext) -> Boolean,
    noinline onSuccess: suspend HandlerScope<T>.() -> Unit,
) {
    addHandler(
        TypeHandlerImpl(typesMapToKind[T::class.java]!! as UpdKind, check, onSuccess)
    )
}

val typesMapToKind: Map<Class<out Update>, UpdKind> =
    telegramUpdateKinds.associateBy { it.clazz as Class<out Update> }
