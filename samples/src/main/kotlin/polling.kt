package org.bezsahara.samples

import kotlinx.coroutines.delay
import org.bezsahara.kittybot.bot.KittyBotConfig
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.bot.purrBlocking
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.stopPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

var botRef: KittyBotConfig<PollingReceiver>? = null

fun pollingBot(token: String) {
    val bot = KittyBot<PollingReceiver> {
        this.token = token

        // SingleThread is enough for most bots.
        // If you need ordered parallelism per chat, switch to UpdaterMode.MultiThread(...).
        updaterMode = UpdaterMode.SingleThread

        // You can replace the default Vert.x transport with useCustomClient(...) or apiClientBuilder = ...
        // useCustomClient(KtorCustomClient(HttpClient(CIO)))
        // useCustomClient(JavaCustomClient.createDefault())

        dispatchers {
            command("/shutdown") {
                bot.sendMessage(chatId, "Bot is shut down")
                botRef!!.stopPolling()
                botRef = null
            }
        }

        buildBot()
    }

    // Polling and webhooks cannot be active at the same time.
    // Clearing a previous webhook is a common first step for polling bots.
    bot.purrBlocking {
        deleteWebhook()
    }

    botRef = bot

    bot.startPolling()
}
