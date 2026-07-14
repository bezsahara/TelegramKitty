package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.addHandlerWithCheck
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.media.Document

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

class DocumentScope(
    update: MessageUpdate,
    bot: KittyBot,
    handlerContext: HandlerContext,
    val document: Document
) : MessageScope(update, bot, handlerContext)

fun HandlerStore.document(block: suspend DocumentScope.() -> Unit) {
    addHandler(MessageUpdate.toSet()) { update, bot, handlerContext ->
        val document = (update as MessageUpdate).message.document ?: return@addHandler Decision.Next
        DocumentScope(update, bot, handlerContext, document).block()
        Decision.Consumed
    }
}