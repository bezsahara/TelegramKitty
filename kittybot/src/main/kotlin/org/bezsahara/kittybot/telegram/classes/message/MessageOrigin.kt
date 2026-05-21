package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.Serializable


@Serializable(with = MessageOriginSerializer::class)
sealed interface MessageOrigin {
    val type: String
}

