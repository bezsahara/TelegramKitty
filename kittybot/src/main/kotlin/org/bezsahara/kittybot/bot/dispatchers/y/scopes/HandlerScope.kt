package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update


abstract class HandlerScope<T : Update> : PayloadScope {
    abstract val bot: KittyBot
    abstract val update: T
}

class HandlerScopeImpl<T : Update>(
    override val update: T,
    override val bot: KittyBot,
    override val handlerContext: HandlerContext,
) : HandlerScope<T>()


@get:JvmName("getChatIdMU")
val HandlerScope<MessageUpdate>.chatId: ChatId get() = ChatId(update.message.chat.id.toString())

@get:JvmName("getChatIdCQU")
val HandlerScope<CallbackQueryUpdate>.chatId: ChatId get() = ChatId(update.callbackQuery.from.id)