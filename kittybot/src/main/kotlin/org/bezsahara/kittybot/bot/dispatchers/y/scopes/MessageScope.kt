package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message

open class MessageScope(
    val message: Message,
    val bot: KittyBot
) {
    val chatId = ChatId(message.chat.id.toString())
}