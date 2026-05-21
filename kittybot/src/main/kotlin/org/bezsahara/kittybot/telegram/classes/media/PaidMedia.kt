package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.Serializable


@Serializable(with = PaidMediaSerializer::class)
sealed interface PaidMedia {
    val type: String
}

