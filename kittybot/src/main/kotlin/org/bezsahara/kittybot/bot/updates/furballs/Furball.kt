package org.bezsahara.kittybot.bot.updates.furballs

import org.bezsahara.kittybot.telegram.classes.core.update.Update

abstract class Furball {
    abstract suspend fun applyHandlers(update: Update)
}