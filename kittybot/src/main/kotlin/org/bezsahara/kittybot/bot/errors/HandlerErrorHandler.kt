package org.bezsahara.kittybot.bot.errors

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface HandlerErrorHandler {

    suspend fun handleException(
        throwable: Throwable,
        bot: KittyBot,
        update: Update,
        handlerContext: HandlerContext,
        errHandler: Handler
    ): Decision
}

