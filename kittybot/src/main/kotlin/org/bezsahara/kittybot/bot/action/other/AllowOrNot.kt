package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind


inline fun HandlerStore.guardHandler(noinline check: (Update, HandlerContext) -> Boolean, builder: TransparentHandlerStore.() -> Unit) {
    GuardHandlerBuilder(check, this).also(builder).build()
}

class GuardHandlerBuilder(
    val check: (Update, HandlerContext) -> Boolean,
    val original: HandlerStore
) : TransparentHandlerStore {
    private val handlers = arrayListOf<Handler>()

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher

    private var allowedKinds: HashSet<UpdateKind<*>>? = hashSetOf()

    override fun addHandler(handler: Handler) {
        val ak = handler.allowedKinds
        if (ak == null) {
            allowedKinds = null
        } else {
            allowedKinds?.addAll(ak)
        }
        handlers.add(handler)
    }

    fun build() {
        if (handlers.isEmpty()) {
            return
        }

        val last = handlers.last().ensureHasIdentity()

        handlers[handlers.lastIndex] = last

        val ao = GuardHandlerStart(check, last.identity!!, allowedKinds)

        original.addHandler(ao)
        handlers.forEach { original.addHandler(it) }
    }
}

class GuardHandlerStart(
    private val check: (Update, HandlerContext) -> Boolean,
    endIdentity: HandlerIdentity,
    override val allowedKinds: HashSet<UpdateKind<*>>?
): Handler {
    private val endIdentity = Decision.AfterNextTo(endIdentity, true)

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