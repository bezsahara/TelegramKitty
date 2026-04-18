package org.bezsahara.kittybot.telegram.client

import io.vertx.core.Vertx
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.BotApiServerConfig
import org.bezsahara.kittybot.bot.builder.ClientBuilder

class BuildVertxClient : ClientBuilder {
    private val v = Vertx.vertx()
    override fun build(
        botApiServerConfig: BotApiServerConfig,
        json: Json,
    ): KittyBot {
        return TApiClient(v, json, TPath(botApiServerConfig.buildLink(), botApiServerConfig.token))
    }

    override fun close() {
        v.close()
    }
}