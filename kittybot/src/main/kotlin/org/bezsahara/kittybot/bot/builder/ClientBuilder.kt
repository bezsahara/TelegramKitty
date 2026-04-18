package org.bezsahara.kittybot.bot.builder

import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot

fun interface ClientBuilder {
    fun build(botApiServerConfig: BotApiServerConfig, json: Json): KittyBot

    fun close() {}
}