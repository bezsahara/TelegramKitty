package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity

class RoutingPart(private val original: HandlerStore) : HandlerStore {
    internal val handlers = arrayListOf<Handler>()

    fun isEmpty() = handlers.isEmpty()

    private var first = true
    override fun addHandler(handler: Handler) {
        if (first) {
            first = false
            handlers.add(handler.ensureHasIdentity())
            return
        }
        handlers.add(handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}