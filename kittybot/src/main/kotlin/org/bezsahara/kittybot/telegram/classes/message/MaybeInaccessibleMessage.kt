package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


@Serializable(with = MaybeInaccessibleMessageSerializer::class)
sealed interface MaybeInaccessibleMessage {
    val chat: Chat
    @SerialName("message_id") val messageId: Long
    val date: Long
}

