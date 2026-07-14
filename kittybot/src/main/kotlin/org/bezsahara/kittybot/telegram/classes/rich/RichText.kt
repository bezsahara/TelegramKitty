package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


@Serializable(with = RichTextSerializer::class)
sealed interface RichText {
    val type: String
}
