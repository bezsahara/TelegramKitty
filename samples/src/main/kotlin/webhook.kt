@file:Suppress("DuplicatedCode")

package org.bezsahara.samples

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bezsahara.kittybot.bot.onUpdate
import org.bezsahara.kittybot.bot.start
import org.bezsahara.kittybot.bot.updates.KittyBot
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.bot.updates.webhook

@Suppress("UNREACHABLE_CODE")
fun webhookBot(token: String) {
    val bot = KittyBot<WebhookReceiver> {
        this.token = token

        // set up webhook
        webhook(
            url = TODO(),
            deletePreviousWebhook = true
        )

        buildBot()
    }

    bot.start()
    // ktor server
    val server = embeddedServer(Netty, 8080, "0.0.0.0") {
        routing {
            post("/$token") {
                val data = call.receiveText()
                bot.onUpdate(data)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
    server.start(wait = true)
}
