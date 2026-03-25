package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class PollType {
    @SerialName("regular")
    REGULAR,
    @SerialName("quiz")
    QUIZ
}

