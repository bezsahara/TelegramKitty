package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.updates.Update

interface HandlerScope<T: Update> {
    val update: T
    val bot: KittyBot
}

class UpdateScopeImpl<T: Update>(
    override val bot: KittyBot,
    override val update: T
): HandlerScope<T>

val HandlerScope<MessageUpdate>.chatId: ChatId get() = ChatId(update.message.chat.id.toString())