package org.bezsahara.kittybot.bot.builder

import kotlinx.serialization.builtins.ListSerializer
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.json.jsonInstance
import org.bezsahara.kittybot.telegram.classes.core.update.Update
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

fun deserializeUpdate(data: String): Update {
    return jsonInstance.decodeFromString(Update.serializer(), data)
}

fun deserializeListOfUpdates(data: String): List<Update> {
    return jsonInstance.decodeFromString(listOfUpdatesSr, data)
}

private val listOfUpdatesSr = ListSerializer(Update.serializer())