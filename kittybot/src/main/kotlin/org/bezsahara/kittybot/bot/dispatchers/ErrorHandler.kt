package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.updates.Update

fun interface ErrorHandler {
    fun handleError(e: Throwable, bot: KittyBot, update: Update)
}