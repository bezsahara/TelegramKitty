package org.bezsahara.kittybot.bot.builder

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.json.jsonInstance
import java.net.URI

// You can use this if u just want a bot, without system of handlers and other stuff
fun createTelegramBot(
    token: String,
    baseUri: URI = URI.create("https://api.telegram.org"),
    timeoutSec: Long = 61,
    clientBuilder: ClientBuilder = tryFindDefaultClient()
): KittyBot {
    return clientBuilder.build(BotApiServerConfig(token, baseUri, timeoutSec), jsonInstance)
}