package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.CallScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery

class CallHandler(
    private val check: (CallbackQuery) -> Boolean,
    private val onSuccess: suspend CallScope.() -> Unit
) : Handler {
    override val allowedKinds: Set<UpdKind> = setOf(CallbackQueryUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val p1 = (update as CallbackQueryUpdate).callbackQuery
        if (check(p1)) {
            CallScope(bot, update, handlerContext).onSuccess()
            return Decision.Consumed
        }
        return Decision.Next
    }
}

fun HandlerStore.callbackQuery(
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
