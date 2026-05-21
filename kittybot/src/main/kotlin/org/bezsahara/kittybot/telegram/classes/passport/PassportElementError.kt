package org.bezsahara.kittybot.telegram.classes.passport

import kotlinx.serialization.Serializable


@Serializable(with = PassportElementErrorSerializer::class)
sealed interface PassportElementError {
    val source: String
}

