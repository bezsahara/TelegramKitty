package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update


inline fun HandlerStore.guardHandler(noinline check: (Update, HandlerContext) -> Boolean, builder: TransparentHandlerStore.() -> Unit) {
    GuardHandlerBuilder(check, this).also(builder).build()
}

class GuardHandlerBuilder(
    val check: (Update, HandlerContext) -> Boolean,
    val original: HandlerStore
) : TransparentHandlerStore {
    private val handlers = FreeRef(arrayListOf<Handler>())

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher

    private var allowedKinds: HashSet<UpdKind>? = hashSetOf()

    override fun addHandler(handler: Handler) {
        val ak = handler.allowedKinds
        if (ak == null) {
            allowedKinds = null
        } else {
            allowedKinds?.addAll(ak)
        }
        handlers.get().add(handler)
    }

    fun build() {
        val handlers = handlers.get()
        if (handlers.isEmpty()) {
            return
        }

        val last = handlers.last().ensureHasIdentity()

        handlers[handlers.lastIndex] = last

        val ao = GuardHandlerStart(check, last.identity!!, allowedKinds)

        original.addHandler(ao)
        handlers.forEach { original.addHandler(it) }
        this.handlers.free()
    }
}

class GuardHandlerStart(
    private val check: (Update, HandlerContext) -> Boolean,
    endIdentity: HandlerIdentity,
    override val allowedKinds: HashSet<UpdKind>?
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