package org.bezsahara.kittybot.telegram.classes.core.update

import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.message.Message

abstract class SyntheticUpdate() : Update() {
    final override val ordinal: Int
        get() = 25

    override val updateId: Long
        get() = 0

    companion object : UpdKind(25, SyntheticUpdate::class.java, "SyntheticUpdate")
}

// An Update kind for Grouped Media messages
class GroupedMediaUpdate(
    val mediaMessagesGrouped: List<Message>
) : SyntheticUpdate() {
    val firstMessage get() = mediaMessagesGrouped[0]

    override fun chatIdOrNull(): ChatId = firstMessage.chat.id.toChatId()

    override fun userIdOrNull(): ChatId? = firstMessage.from?.id?.toChatId()
}