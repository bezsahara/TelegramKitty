package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

abstract class TextHandler(
    val onSuccess: suspend MessageScope.() -> Unit
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>> = setOf(MessageUpdate)

    override suspend fun handleUpdate(update: Update, bot: KittyBot, handlerContext: HandlerContext): Decision {
        if (!isFine(update as MessageUpdate)) return Decision.Next
        
        MessageScope(
            update,
            bot,
            handlerContext
        ).onSuccess()
        return Decision.Consumed
    }
    
    abstract fun isFine(update: MessageUpdate): Boolean
}

fun HandlerStore.text(regex: Regex, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun isFine(update: MessageUpdate): Boolean {
            return regex.matches(update.message.text ?: return false)
        }
    })
}

fun HandlerStore.text(text: String, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun isFine(update: MessageUpdate): Boolean {
            return update.message.text == text
        }
    })
}

fun HandlerStore.text(onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : Handler {
        override val allowedKinds: Set<UpdateKind<*>>
            get() = setOf(MessageUpdate)

        override suspend fun handleUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): Decision {
            val k = MessageScope(update as MessageUpdate, bot, handlerContext)
            k.onSuccess()
            return Decision.Consumed
        }
    })
}

fun HandlerStore.text(check: (String) -> Boolean, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun isFine(update: MessageUpdate): Boolean {
            return check(update.message.text ?: return false)
        }
    })
}