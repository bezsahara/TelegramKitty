package org.bezsahara.samples

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.bezsahara.kittybot.bot.purrBlocking
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.client.ktor.KtorCustomClient

fun pollingBot(token: String) {
    val bot = KittyBot<PollingReceiver> {
        this.token = token

        // SingleThread is enough for most bots.
        // If you need ordered parallelism per chat, switch to UpdaterMode.MultiThread(...).
        updaterMode = UpdaterMode.SingleThread

        // You can replace the default Vert.x transport with useCustomClient(...) or apiClientBuilder = ...
        // useCustomClient(KtorCustomClient(HttpClient(CIO)))

        buildBot()
    }

    // Polling and webhooks cannot be active at the same time.
    // Clearing a previous webhook is a common first step for polling bots.
    bot.purrBlocking {
        deleteWebhook()
    }

    bot.startPolling()
}
