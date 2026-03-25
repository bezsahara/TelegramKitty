package org.bezsahara.samples

import org.bezsahara.kittybot.bot.start
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.webhook
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver

fun webhookBot(token: String) {
    val bot = KittyBot<WebhookReceiver> {
        this.token = token

        // The library can register the webhook for you,
        // but you still need your own HTTP endpoint to receive Telegram POST requests.
        webhook(
            url = "https://example.com/telegram/$token",
            deletePreviousWebhook = true
        )

        buildBot()
    }

    // In webhook mode the bot does not fetch updates by itself.
    // start() only starts the handler machinery, then your server forwards payloads through onUpdate(...).
    bot.start()

    // Example server integration:
    // val server = embeddedServer(Netty, 8080, "0.0.0.0") {
    //     routing {
    //         post("/$token") {
    //             val data = call.receiveText()
    //             bot.onUpdate(data)
    //             call.respond(HttpStatusCode.OK)
    //         }
    //     }
    // }
    // server.start(wait = true)
}
