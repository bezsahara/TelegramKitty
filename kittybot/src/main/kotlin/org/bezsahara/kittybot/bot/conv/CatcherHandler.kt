package org.bezsahara.kittybot.bot.conv

import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

/**
 * One-shot matcher used by a conversation to suspend until a matching update arrives.
 *
 * A catcher is registered into a conversation waiter registry and is tested only against
 * updates of [updateKind]. The first non-null value returned from [catchOrNull] completes
 * the waiting call and removes this catcher from the registry.
 */
interface CatcherHandler<T> {
    /**
     * Telegram update kind this catcher wants to inspect.
     */
    val updateKind: UpdateKind<*>

    /**
     * Tries to extract a value from [update].
     *
     * Return `null` when the update should be ignored, or a value of type [T] when the
     * waiter should complete and consume that update for the conversation.
     */
    suspend fun catchOrNull(update: Update, handlerContext: HandlerContext): T?
}
