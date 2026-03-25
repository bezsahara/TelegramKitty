package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

class ContactScope(
    update: MessageUpdate,
    bot: KittyBot,
    handlerContext: HandlerContext
) : MessageScope(update, bot, handlerContext) {
    val contact get() = update.message.contact!!
}

fun HandlerStore.contact(block: suspend ContactScope.() -> Unit) {
    addHandler(setOf(MessageUpdate)) { update, bot, handlerContext ->
        if ((update as MessageUpdate).message.contact == null) {
            return@addHandler Decision.Next
        }

        ContactScope(update, bot, handlerContext).block()

        Decision.Consumed
    }
}