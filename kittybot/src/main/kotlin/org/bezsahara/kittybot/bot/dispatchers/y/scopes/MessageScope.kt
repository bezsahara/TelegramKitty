package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.message.Message

interface PayloadScope {
    val handlerContext: HandlerContext
}

class BotScope(
    val bot: KittyBot
)

open class MessageScope(
    final override val update: MessageUpdate,
    final override val bot: KittyBot,
    final override val handlerContext: HandlerContext
) : HandlerScope<MessageUpdate> {
    val message: Message get() = update.message
}

open class TextScope(
    update: MessageUpdate,
    bot: KittyBot,
    handlerContext: HandlerContext,
    val text: String
) : MessageScope(update, bot, handlerContext)

open class UpdateScope(
    override val update: Update,
    override val bot: KittyBot,
    override val handlerContext: HandlerContext
) : HandlerScope<Update>

