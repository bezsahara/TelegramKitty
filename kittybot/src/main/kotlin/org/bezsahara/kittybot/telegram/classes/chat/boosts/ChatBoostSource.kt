package org.bezsahara.kittybot.telegram.classes.chat.boosts

import kotlinx.serialization.Serializable


@Serializable(with = ChatBoostSourceSerializer::class)
sealed interface ChatBoostSource {
    val source: String
}

