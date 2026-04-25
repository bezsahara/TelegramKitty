package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.IdentityScope
import org.bezsahara.kittybot.bot.builder.FelineBuilder

@KittyDsl
class FelineDispatcher internal constructor(val felineBuilder: FelineBuilder<*>) : TransparentHandlerStore {
    @JvmField
    internal val handlerList = arrayListOf<Handler>()
    private val filterBuilder = Filters()

    private var identityScopePrivate = IdentityScope()

    var identityScope: IdentityScope
        get() = identityScopePrivate
        set(value) {
            if (identityScopePrivate.highest() != 0) {
                error("You can't set IdentityScope because it was already used!")
            }
            identityScopePrivate = value
        }

    // Filters are just handlers that are added in the beginning
    inner class Filters {
        fun addFilter(filterHandler: Handler) {
            checkClosed()
            handlerList.add(0, filterHandler)
        }
    }

    /**
     * Sets up filters. Filters will be executed before the handlers.
     */
    fun filters(block: Filters.() -> Unit) {
        checkClosed()
        filterBuilder.apply(block)
    }

    override fun addHandler(handler: Handler) {
        checkClosed()
        handlerList.add(handler)
    }

    fun addHandlerFirst(handler: Handler) {
        checkClosed()
        handlerList.add(0, handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = this

    private var closed = false

    private fun checkClosed() {
        if (closed) { error("Feline was already closed!") }
    }

    fun close() {
        closed = true
    }
}



@KittyDsl
interface HandlerStore {
    fun addHandler(handler: Handler)

    val felineDispatcher: FelineDispatcher
}

// Does not change behavior of added handlers
@KittyDsl
interface TransparentHandlerStore : HandlerStore

// Can change behaivour of added handlers
@KittyDsl
interface ChangingHandlerStore : HandlerStore

inline fun <reified T> HandlerStore.attrKeyOf(name: String? = null): AttrKey<T> {
    return felineDispatcher.identityScope.attrKeyOf<T>(name)
}