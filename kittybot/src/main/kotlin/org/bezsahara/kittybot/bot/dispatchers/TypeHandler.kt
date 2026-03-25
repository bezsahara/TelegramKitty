package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

/**
 * Can be used to create Handlers that will be triggered only if the specified type of update comes.
 *
 * @param expect type of update that applies
 */
abstract class TypeHandler<T: Update>(
    private val expect: UpdateKind<T>
) : Handler {
    final override val allowedKinds: Set<UpdateKind<*>> = setOf(expect)
    final override val identity: HandlerIdentity by HandlerIdentityDelegate()

    abstract suspend fun handleUpdateTyped(
        update: T, bot: KittyBot, handlerContext: HandlerContext
    ): Decision

    final override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision = handleUpdateTyped(update as T, bot, handlerContext)
}
