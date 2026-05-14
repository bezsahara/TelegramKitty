package org.bezsahara.kittybot.bot.builder

import java.net.URI

// Timeout in seconds (for polling)
data class BotApiServerConfig(
    val token: String,
    val baseURI: URI,
    val timeout: Long
) {
    init {
        val scheme = baseURI.scheme
        require(scheme.equals("https", ignoreCase = true) || scheme.equals("http", ignoreCase = true)) {
            "Unsupported scheme $scheme"
        }
        require(baseURI.host != null) { "Server URI must have a host" }
    }

    fun buildLink(): String {
        return baseURI.resolve("/bot$token").toString()
    }
}
