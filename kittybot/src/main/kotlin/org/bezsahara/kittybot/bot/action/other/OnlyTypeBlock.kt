package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.asDelegate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind

class OnlyTypeBlock(
    private val acceptTypes: Set<UpdKind>,
    private val original: HandlerStore
) : HandlerStore {
    override fun addHandler(handler: Handler) {
        original.addHandler(handler.asDelegate(handler.identity, acceptTypes))
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}

inline fun HandlerStore.scopeOfType(vararg types: UpdKind, block: HandlerStore.() -> Unit) {
    val otb = OnlyTypeBlock(types.toSet(), this)
    otb.block()
}