package org.bezsahara.kittybot.bot.errors

class KittyError(message: String?) : Error(message)

fun hiss(message: String): Nothing = throw KittyError(message)