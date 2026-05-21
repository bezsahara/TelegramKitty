package org.bezsahara.kittybot.telegram.classes.chat.member

import kotlinx.serialization.Serializable


@Serializable(with = ChatMemberSerializer::class)
sealed interface ChatMember {
    val status: String
}

