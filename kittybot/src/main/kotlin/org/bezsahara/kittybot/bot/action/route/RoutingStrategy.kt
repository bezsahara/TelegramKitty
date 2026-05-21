package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.action.other.replaceLast
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind

sealed class RoutingStrategy<T>(val original: HandlerStore) {
//    protected val sections by lazy { sectionsMap.toList().filter { !it.second.isEmpty() } }

    protected fun computeSections(): List<Pair<T, RoutingPart>> {
        return sectionsMap.toList().filter { !it.second.isEmpty() }
    }

    protected val sectionsMap = linkedMapOf<T, RoutingPart>()

    protected fun computeRoutingUpdKinds(sections: List<Pair<T, RoutingPart>>): Set<UpdKind>? {
        val set: MutableSet<UpdKind> = mutableSetOf()

        for (section in sections) {
            for (handler in section.second.handlers) {
                val allowed = handler.allowedKinds
                if (allowed == null) {
                    return null
                } else set.addAll(allowed)
            }
        }

        return set
    }

    @PublishedApi
    internal fun addOrGetSection(key: T, original: HandlerStore): RoutingPart {
        val routingPart = sectionsMap.getOrPut(key) { RoutingPart(original) }
        require(original === routingPart.original)
        return routingPart
    }


    protected var common: Handler? = null

    @PublishedApi
    internal var default: RoutingPart? = null
        get() {
            if (field == null || field!!.isEmpty()) {
                return null
            }
            return field
        }

    protected fun computeExits(sections: List<Pair<T, RoutingPart>>): Pair<Decision?, Decision> {
        val exitHandler = sections.last().second.handlers.replaceLast { it.ensureHasIdentity() }
        val exitHandlerIdentityD = if (default == null) {
            null
        } else {
            Decision.NextTo(default!!.handlers.first().identity!!, 0, true)
        }
        val actualExit = if (default == null) {
            Decision.AfterNextTo(exitHandler.identity!!, true)
        } else {
            Decision.AfterNextTo(default!!.handlers.replaceLast { it.ensureHasIdentity() }.identity!!, true)
        }

        return exitHandlerIdentityD to actualExit
    }

    /**
     * Defines a handler that runs before any section.
     */
    fun common(handler: Handler) {
        require(common == null) { "You already defined common before!" }
        common = handler
    }

    fun common(allowedKinds: Set<UpdKind>? = null, identity: HandlerIdentity? = null, handler: Handler) {
        common(handler.asDelegate(identity, allowedKinds))
    }

    protected fun reduceAllowedKinds(list: List<Handler>): Set<UpdKind>? {
        val set = hashSetOf<UpdKind>()
        list.forEach {
            val ac = it.allowedKinds ?: return null
            set.addAll(ac)
        }
        return set
    }

    inline fun default(block: TransparentHandlerStore.() -> Unit) {
        var r = default
        if (r == null) {
            r = RoutingPart(original)
            default = r
        }
        r.block()
    }
}