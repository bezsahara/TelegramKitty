@file:Suppress("NOTHING_TO_INLINE")

package org.bezsahara.kittybot.bot.errors

import org.bezsahara.kittybot.bot.dispatchers.Handler

class HandlerAwareError(val originalThrowable: Throwable, val originalHandler: Handler) : RuntimeException()

inline fun Throwable.throwWithHandler(handler: Handler): Nothing { throw HandlerAwareError(this, handler) }