package org.bezsahara.kittybot.telegram.client

import io.vertx.core.Vertx
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.ClientBuilder

class BuildVertxClient : ClientBuilder {
    private val v = Vertx.vertx()
    override fun build(
        token: String,
        json: Json,
    ): KittyBot {
        return TApiClient(v, json, TPath("https://api.telegram.org/bot$token"))
    }

    override fun close() {
        v.close()
    }
}