package org.bezsahara.kittybot.telegram.classes.message.reactions

import kotlinx.serialization.Serializable


@Serializable(with = ReactionTypeSerializer::class)
sealed interface ReactionType {
    val type: String
}

