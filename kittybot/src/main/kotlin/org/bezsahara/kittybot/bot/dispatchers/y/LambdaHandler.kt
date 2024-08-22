package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.LambdaScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.updates.Update

class LambdaHandler(
    val check: (Update) -> Boolean,
    val onSuccess: suspend LambdaScope.() -> Unit
) : Handler {
    override fun checkUpdate(update: Update): Boolean {
        return check(update)
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        LambdaScope(
            bot, update
        ).onSuccess()
    }
}

class MessageLambdaHandler(
    val check: (Message) -> Boolean,
    val onSuccess: suspend MessageScope.() -> Unit
) : Handler {
    override fun checkUpdate(update: Update): Boolean {
        return check(update.message ?: return false)
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        MessageScope(
            update.message!!, bot
        ).onSuccess()
    }
}

fun FelineDispatcher.messageHandler(check: (Message) -> Boolean, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(MessageLambdaHandler(check, onSuccess))
}

fun FelineDispatcher.handler(check: (Update) -> Boolean, onSuccess: suspend LambdaScope.() -> Unit) {
    addHandler(LambdaHandler(check, onSuccess))
}