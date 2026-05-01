package org.bezsahara.kittybot.bot.action.flow

data class FlowPayload<T>(
    val id: Int,
    val args: T?
)
