package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.dispatchers.*

class RoutingPart(internal val original: HandlerStore) : TransparentHandlerStore {
    private val handlersP = FreeRef(arrayListOf<Handler>())

    val handlers get() = handlersP.get()

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

    internal fun free() = handlersP.free()

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}