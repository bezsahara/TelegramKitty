package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.Serializable


@Serializable(with = BotCommandScopeSerializer::class)
sealed interface BotCommandScope {
    val type: String
}

