package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScopeImpl
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

/**
 * A handler that performs side effects on [HandlerContext] and then continues dispatch.
 *
 * This is useful for precomputing data that later handlers, routers, or flow handlers need.
 * The work may suspend, so expensive or I/O-backed lookups can be done here once and their
 * results stored in the context for cheap synchronous access later in the pipeline.
 *
 * Use [forUpdates] to limit execution to specific update kinds. Returning [Decision.Next]
 * means this handler never consumes the update on its own.
 */
class ContextManipulation(
    private val forUpdates: Set<UpdateKind<*>>? = null,
    private val contextBlock: suspend (update: Update, handlerContext: HandlerContext) -> Unit,
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>>?
        get() = forUpdates

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        contextBlock.invoke(update, handlerContext)
        return Decision.Next
    }
}

/**
 * Adds a handler that can populate or modify [HandlerContext] before later handlers run.
 *
 * This is the intended place for suspending or other preprocessing. For example, you can load a user,
 * parse some shared state, or derive routing inputs here and save them into the context, then
 * let downstream handlers read that data without doing extra asynchronous work.
 *
 * @param forUpdates optional set of accepted update kinds. `null` means all updates.
 * @param contextBlock suspending block that can read the [Update] and write derived values into
 * the [HandlerContext].
 */
fun HandlerStore.contextHook(
    forUpdates: Set<UpdateKind<*>>? = null,
    contextBlock: suspend (update: Update, handlerContext: HandlerContext) -> Unit,
) {
    addHandler(ContextManipulation(forUpdates, contextBlock))
}



fun HandlerStore.debugHook(
    forUpdates: Set<UpdateKind<*>>? = null,
    block: suspend HandlerScope<Update>.() -> Unit,
) {
    addHandler(object : Handler {
        override val allowedKinds: Set<UpdateKind<*>>?
            get() = forUpdates

        override suspend fun handleUpdate(update: Update, bot: KittyBot, handlerContext: HandlerContext): Decision {
            HandlerScopeImpl(update, bot, handlerContext).block()
            return Decision.Next
        }
    })
}