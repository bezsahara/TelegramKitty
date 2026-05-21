package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.Serializable


@Serializable(with = BackgroundTypeSerializer::class)
sealed interface BackgroundType {
    val type: String
}

