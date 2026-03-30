package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery

class CallScope(
    override val bot: KittyBot,
    override val update: CallbackQueryUpdate,
    override val handlerContext: HandlerContext
) : HandlerScope<CallbackQueryUpdate> {
    val callbackQuery: CallbackQuery get() = update.callbackQuery
}
