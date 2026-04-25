package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface HandlerCheck {
    fun check(update: Update, handlerContext: HandlerContext): Boolean
}