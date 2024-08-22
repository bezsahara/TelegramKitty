package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message

class CommandScope(
    bot: KittyBot,
    message: Message,
    @JvmField val commandArgs: String?
) : MessageScope(message, bot)