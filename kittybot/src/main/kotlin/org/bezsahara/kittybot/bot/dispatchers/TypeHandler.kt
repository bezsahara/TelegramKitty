package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.updates.Update

/**
 * Can be used to create Handlers that will be triggered only if the specified type of update comes.
 *
 * @param expect type of update that applies
 */
abstract class TypeHandler<T: Update>(
    private val expect: Class<T>
) : Handler {

    // Implement this
    abstract fun check(update: T): Boolean

    // Implement this
    abstract suspend fun handle(update: T, bot: KittyBot)

    // Do not implement the following:

    override fun checkUpdate(update: Update): Boolean {
        return expect.isInstance(update) && check(update as T)
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        handle(update as T, bot)
    }
}
