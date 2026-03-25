package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

class CommandScope(
    bot: KittyBot,
    upd: MessageUpdate,
    @JvmField val commandArgs: String?,
    payload: HandlerContext
) : MessageScope(upd, bot, payload)