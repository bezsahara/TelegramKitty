package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.updates.Update


/**
 * Filters updates. Filters are executed before handlers.
 */
interface FilterHandler {
    /**
     * Return true if you would like to apply this filter.
     */
    suspend fun applicable(update: Update): Boolean

    /**
     * Return true if you would like to proceed to other filters OR
     * return false if you do not want to pass this update to handlers (other filters will not be tested in this case).
     */
    suspend fun process(update: Update, bot: KittyBot): Boolean
}