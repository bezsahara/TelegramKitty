package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.IdentityScope


class FelineDispatcher internal constructor() : HandlerStore {
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
            handlerList.add(0, filterHandler)
        }
    }

    /**
     * Sets up filters. Filters will be executed before the handlers.
     */
    fun filters(block: Filters.() -> Unit) {
        filterBuilder.apply(block)
    }

    override fun addHandler(handler: Handler) {
        handlerList.add(handler)
    }

    fun addHandlerFirst(handler: Handler) {
        handlerList.add(0, handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = this
}

interface HandlerStore {
    fun addHandler(handler: Handler)

    val felineDispatcher: FelineDispatcher
}

inline fun <reified T> HandlerStore.attrKeyOf(name: String? = null): AttrKey<T> {
    return felineDispatcher.identityScope.attrKeyOf<T>(name)
}