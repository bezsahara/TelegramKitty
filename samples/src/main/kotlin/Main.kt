package org.bezsahara.samples

fun main() {
    val token = System.getenv("BOT_TOKEN")
        ?: System.getProperty("BOT_TOKEN") ?: error("Set BOT_TOKEN to run the samples")

    // Pick the sample you want to run.
    pollingBot(token)
    // echoBot(token)
    // webhookBot(token)
}
