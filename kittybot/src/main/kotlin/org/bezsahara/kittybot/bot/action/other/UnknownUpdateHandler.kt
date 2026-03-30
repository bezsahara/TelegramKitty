package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScopeImpl
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UnknownUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

class UnknownUpdateHandler(
    val block: suspend HandlerScope<UnknownUpdate>.() -> Unit
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>>
        get() = setOf(UnknownUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        HandlerScopeImpl(
            update as UnknownUpdate,
            bot,
            handlerContext
        ).block()
        return Decision.Consumed
    }
}

// Future compatability solution
fun HandlerStore.handleUnknownUpdate(block: suspend HandlerScope<UnknownUpdate>.() -> Unit) {
    addHandler(UnknownUpdateHandler(block))
}