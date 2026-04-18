package org.bezsahara.kittybot.bot.builder

import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.client.TConsumeBot

class ConsumeClientBuilder(val delegate: ClientBuilder) : ClientBuilder {
    override fun build(
        botApiServerConfig: BotApiServerConfig,
        json: Json,
    ): KittyBot {
        return TConsumeBot(delegate.build(botApiServerConfig, json))
    }

    override fun close() {
        delegate.close()
    }
}