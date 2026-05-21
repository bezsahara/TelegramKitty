package org.bezsahara.kittybot.telegram.client

import io.vertx.core.Vertx
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.BotApiServerConfig
import org.bezsahara.kittybot.bot.builder.ClientBuilder

class BuildVertxClient : ClientBuilder {
    private val v = acquireVertx()
    private var closed = false

    @Synchronized
    override fun build(
        botApiServerConfig: BotApiServerConfig,
        json: Json,
    ): KittyBot {
        require(!closed) { "BuildVertxClient closed" }
        return TApiClient(v, json, TPath(botApiServerConfig.buildLink(), botApiServerConfig.token, botApiServerConfig.timeout))
    }

    @Synchronized
    override fun close() {
        if (closed) return
        closed = true
        closeVertx()
    }

    private companion object {
        private var counter = 0
        private var cache: Vertx? = null

        @Synchronized
        private fun acquireVertx(): Vertx {
            counter += 1
            var v = cache
            if (v == null) {
                v = Vertx.vertx()!!
                cache = v
            }
            return v
        }

        @Synchronized
        private fun closeVertx() {
            val newCounter = counter - 1

            check(newCounter >= 0) { "Vertx is closed more times than opened" }
            counter = newCounter

            if (newCounter == 0) {
                cache?.close() ?: error("Cache was null when counter was 0 for vertx")
                cache = null
            }
        }
    }
}
