package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.mgroup.setupMediaGroupHandler
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.mediaGroup

fun FelineDispatcher.mediaGroupExample() {
    setupMediaGroupHandler(200)

    mediaGroup({ true }) {
        bot.sendMessage(chatId, "Received media group. Total number is ${update.mediaMessagesGrouped.size}")
    }
}