package org.bezsahara.kittybot.bot.builder

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.json.jsonInstance

// You can use this if u just want a bot, without system of handlers and other stuff
fun createTelegramBot(
    token: String,
    clientBuilder: ClientBuilder = tryFindDefaultClient()
): KittyBot {
    return clientBuilder.build(token, jsonInstance)
}