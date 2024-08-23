@file:Suppress("DuplicatedCode")

package org.bezsahara.samples

import org.bezsahara.kittybot.bot.*
import org.bezsahara.kittybot.bot.updates.KittyBot
import org.bezsahara.kittybot.bot.updates.UpdaterMode
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

fun pollingBot(token: String) {
    val bot = KittyBot<PollingReceiver> {
        this.token = token
        updaterMode = UpdaterMode.SingleThread

        buildBot()
    }
    bot.purrBlocking {
        deleteWebhook()
    }
    bot.startPolling(true)
}
