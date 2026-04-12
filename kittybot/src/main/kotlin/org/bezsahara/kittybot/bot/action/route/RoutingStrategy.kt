package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.action.other.replaceLast
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.asDelegate
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

sealed class RoutingStrategy<T>(val original: HandlerStore) {
    protected val sections = arrayListOf<Pair<T, RoutingPart>>()

    @PublishedApi
    internal fun addSection(section: Pair<T, RoutingPart>) {
        sections.add(section)
    }

    protected var common: Handler? = null

    @PublishedApi
    internal var default: RoutingPart? = null

    protected fun computeExits(): Pair<Decision?, Decision> {
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

    fun common(allowedKinds: Set<UpdateKind<*>>? = null, identity: HandlerIdentity? = null, handler: Handler) {
        common(handler.asDelegate(identity, allowedKinds))
    }

    inline fun default(block: HandlerStore.() -> Unit) {
        require(default == null) { "You already defined default before!" }
        val r = RoutingPart(original)
        r.block()
        if (r.isEmpty()) return
        default = r
    }
}