package org.bezsahara.kittybot.bot.conv

import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.telegram.classes.chat.ChatId

data class ChatState(
    val chatId: ChatId,
)