@file:Suppress("UNCHECKED_CAST")

package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.TypeHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.UpdateScopeImpl
import org.bezsahara.kittybot.telegram.classes.updates.Update

class TypeHandlerImpl<T: Update>(
    expect: Class<T>,
    private val testUpdate: (T) -> Boolean,
    private val onSuccess: suspend HandlerScope<T>.() -> Unit,
) : TypeHandler<T>(expect) {
    override fun check(update: T): Boolean {
        return testUpdate(update)
    }

    override suspend fun handle(update: T, bot: KittyBot) {
        UpdateScopeImpl(bot, update).apply {
            onSuccess(this)
        }
    }
}

inline fun <reified T: Update> FelineDispatcher.handleTypeOf(
    noinline check: (T) -> Boolean,
    noinline onSuccess: suspend HandlerScope<T>.() -> Unit
) {
    addHandler(TypeHandlerImpl<T>(T::class.java, check, onSuccess))
}