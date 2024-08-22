package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.updates.Update


interface Handler {
    fun checkUpdate(update: Update): Boolean

    suspend fun handleUpdate(update: Update, bot: KittyBot)
}