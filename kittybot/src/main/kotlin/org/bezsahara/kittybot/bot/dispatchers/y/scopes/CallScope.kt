package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.queries.CallbackQuery

class CallScope(
    val bot: KittyBot,
    val callbackQuery: CallbackQuery
)
