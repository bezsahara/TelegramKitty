package org.bezsahara.kittybot

import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.KittyBot
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

fun echoBot(token: String) = KittyBot<PollingReceiver> {
    this.token = token
    dispatchers {
        text({ true }) {
            bot.sendMessage(chatId, message.text!!)
        }
    }
}.startPolling()