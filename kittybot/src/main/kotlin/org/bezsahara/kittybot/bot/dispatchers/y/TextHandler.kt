package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.updates.Update

abstract class TextHandler(
    val onSuccess: suspend MessageScope.() -> Unit
) : Handler {
    override fun checkUpdate(update: Update): Boolean {
        return update is MessageUpdate && update.message.text != null
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        MessageScope(
            update.message!!,
            bot
        ).onSuccess()
    }
}

fun FelineDispatcher.text(regex: Regex, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun checkUpdate(update: Update): Boolean {
            if (!super.checkUpdate(update))
                return false
            return regex.matches(update.message!!.text!!)
        }
    })
}

fun FelineDispatcher.text(text: String, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun checkUpdate(update: Update): Boolean {
            if (!super.checkUpdate(update))
                return false
            return update.message?.text == text
        }
    })
}

fun FelineDispatcher.text(check: (String) -> Boolean, onSuccess: suspend MessageScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun checkUpdate(update: Update): Boolean {
            if (!super.checkUpdate(update))
                return false
            return check(update.message!!.text!!)
        }
    })
}