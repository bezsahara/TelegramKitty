package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.UpdateScope
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

class LambdaHandler(
    val check: (Update) -> Boolean,
    override val allowedKinds: Set<UpdateKind<*>>?,
    val onSuccess: suspend UpdateScope.() -> Unit
) : Handler {
    override suspend fun handleUpdate(update: Update, bot: KittyBot, handlerContext: HandlerContext): Decision {
        if (check(update)) {
            UpdateScope(
                update, bot, handlerContext
            ).onSuccess()
            return Decision.Consumed
        }
        return Decision.Next
    }
}


fun HandlerStore.handler(check: (Update) -> Boolean, updateKinds: Set<UpdateKind<*>>? = null, onSuccess: suspend UpdateScope.() -> Unit) {
    addHandler(LambdaHandler(check, updateKinds, onSuccess))
}