package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.Serializable


@Serializable(with = BackgroundFillSerializer::class)
sealed interface BackgroundFill {
    val type: String
}

