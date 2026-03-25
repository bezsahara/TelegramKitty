package org.bezsahara.samples

import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

fun echoBot(token: String) {
    KittyBot<PollingReceiver> {
        this.token = token

        dispatchers {
            // The smallest possible example: reply with the same text that was received.
            text {
                bot.sendMessage(chatId, message.text!!)
            }
        }
    }.startPolling()
}
