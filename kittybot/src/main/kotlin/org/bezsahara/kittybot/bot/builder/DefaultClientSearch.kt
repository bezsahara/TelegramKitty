package org.bezsahara.kittybot.bot.builder

internal const val DEFAULT_CLIENT_PATH = "org.bezsahara.kittybot.telegram.client.BuildVertxClient"


fun tryFindDefaultClient(): ClientBuilder {
    return Class.forName(DEFAULT_CLIENT_PATH)
        .constructors[0]
        .newInstance() as ClientBuilder
}