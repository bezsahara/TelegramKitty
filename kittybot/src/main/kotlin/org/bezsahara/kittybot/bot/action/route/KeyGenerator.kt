package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface KeyGeneratorAny<T> {
    // return null to indicate no key
    // If a key is returned that is not present, the default section will be chosen if defined
    fun generate(update: Update, handlerContext: HandlerContext): T?
}

fun interface KeyGeneratorInt {
    // Return Int.MIN_VALUE to indicate no key
    // If a key is returned that is not present, the default section will be chosen if defined
    fun generate(update: Update, handlerContext: HandlerContext): Int
}