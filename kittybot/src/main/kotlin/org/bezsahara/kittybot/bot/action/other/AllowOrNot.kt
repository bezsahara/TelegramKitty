package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.telegram.classes.core.update.Update



inline fun HandlerStore.guardHandler(noinline check: suspend (Update, HandlerContext) -> Boolean, builder: HandlerStore.() -> Unit) {
    GuardHandlerBuilder(check, this).also(builder).build()
}


class GuardHandlerBuilder(
    val check: suspend (Update, HandlerContext) -> Boolean,
    val original: HandlerStore
) : HandlerStore {
    private val handlers = arrayListOf<Handler>()

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher

    override fun addHandler(handler: Handler) {
        handlers.add(handler)
    }

    fun build() {
        if (handlers.isEmpty()) {
            return
        }

        val last = handlers.last().ensureHasIdentity()

        handlers[handlers.lastIndex] = last

        val ao = GuardHandlerStart(check, last.identity!!)

        original.addHandler(ao)
        handlers.forEach { original.addHandler(it) }
    }
}

class GuardHandlerStart(private val check: suspend (Update, HandlerContext) -> Boolean, endIdentity: HandlerIdentity): Handler {
    private val endIdentity = Decision.AfterNextTo(endIdentity)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        return if (check(update, handlerContext)) {
            Decision.Next
        } else {
            endIdentity
        }
    }
}