package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class ChatFullInfoType {
    @SerialName("private")
    PRIVATE,
    @SerialName("group")
    GROUP,
    @SerialName("supergroup")
    SUPERGROUP,
    @SerialName("channel")
    CHANNEL
}

