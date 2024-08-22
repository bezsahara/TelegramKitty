package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.CallScope
import org.bezsahara.kittybot.telegram.classes.queries.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.updates.Update

class CallHandler(
    private val check: (CallbackQuery) -> Boolean,
    private val onSuccess: suspend CallScope.() -> Unit
) : Handler {
    override fun checkUpdate(update: Update): Boolean {
        val call = update.callbackQuery ?: return false
        return check.invoke(call)
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        CallScope(
            bot,
            update.callbackQuery!!
        ).onSuccess()
    }
}

fun FelineDispatcher.callback(
    check: (CallbackQuery) -> Boolean,
    onSuccess: suspend CallScope.() -> Unit
) {
    addHandler(
        CallHandler(
            check,
            onSuccess
        )
    )
}
