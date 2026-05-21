package org.bezsahara.kittybot.bot.updates.furballs

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.core.update.Update

class FurballVisitor(
    private val bot: KittyBot,
    private val visitor: UpdateVisitor,
) : Furball() {
    override suspend fun applyHandlers(update: Update) = visitor.onUpdate(bot, update)
}
