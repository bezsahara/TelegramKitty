package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface KeyGeneratorAny {
    // return null to indicate no key
    fun generate(update: Update, handlerContext: HandlerContext): Any?
}

fun interface KeyGeneratorInt {
    // Return Int.MIN_VALUE to indicate no key
    fun generate(update: Update, handlerContext: HandlerContext): Int
}