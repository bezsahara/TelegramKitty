package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import java.util.concurrent.ConcurrentHashMap

fun <T> HandlerStore.callbackQueryRoute(asConcurrent: Boolean = false, selector: (CallbackQueryUpdate, HandlerContext) -> T?): CallbackQueryRoute<T> {
    return CallbackQueryRoute.create(this, asConcurrent, selector)
}

class CallbackQueryRoute<T> internal constructor(
    asConcurrent: Boolean,
    handlerStore: HandlerStore,
    selector: (CallbackQueryUpdate, HandlerContext) -> T?,
) {
    private val map: MutableMap<T, suspend CallQDataScope.() -> Unit> =
        if (asConcurrent) ConcurrentHashMap() else HashMap()
    private val defRef: TRef<(suspend CallQDataScope.() -> Unit)?> =
        if (asConcurrent) TRefVolatile(null) else TRefRegular(null)

    init {
        handlerStore.addHandler(CallQHandler(map, selector, defRef))
    }

    fun callbackQuery(key: T, block: suspend CallQDataScope.() -> Unit) {
        map[key] = block
    }

    fun default(block: suspend CallQDataScope.() -> Unit) {
        defRef.value = block
    }

    companion object {
        fun <T> create(
            handlerStore: HandlerStore,
            asConcurrent: Boolean = false,
            selector: (CallbackQueryUpdate, HandlerContext) -> T?,
        ): CallbackQueryRoute<T> {
            return CallbackQueryRoute(asConcurrent, handlerStore, selector)
        }
    }
}

class CallQHandler<T>(
    private val map: MutableMap<T, suspend CallQDataScope.() -> Unit>,
    private val selector: (CallbackQueryUpdate, HandlerContext) -> T?,
    private val defRef: TRef<(suspend CallQDataScope.() -> Unit)?>,
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>>
        get() = setOf(CallbackQueryUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = selector.invoke(update as CallbackQueryUpdate, handlerContext) ?: return Decision.Next
        val block = map[key]
        val scope = CallQDataScope(bot, handlerContext, update)
        if (block == null) {
            defRef.value?.invoke(scope) ?: throw KittyError("CallbackQueryRoute received key of $key. But it was not defined")
        } else {
            block.invoke(scope)
        }
        return Decision.Consumed
    }
}


class CallQDataScope(
    override val bot: KittyBot,
    override val handlerContext: HandlerContext,
    override val update: CallbackQueryUpdate,
) : HandlerScope<CallbackQueryUpdate> {
    val callbackQuery get() = update.callbackQuery
    val data get() = update.callbackQuery.data
}

sealed interface TRef<T> {
    var value: T
}

class TRefVolatile<T>(
    @Volatile override var value: T,
) : TRef<T>

class TRefRegular<T>(
    override var value: T,
) : TRef<T>