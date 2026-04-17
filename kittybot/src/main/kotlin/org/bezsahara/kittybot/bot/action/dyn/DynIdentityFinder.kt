package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.updates.IntIntHashMap

/**
 * Resolves dynamic handler identities to the owning [DynamicHandlersRegistry].
 */
class DynIdentityFinder(
    dynHandlers: List<DynamicHandlersRegistry>
) {
    private val dynHandlers = dynHandlers.toTypedArray()
    private val rebuildLock = Any()

    @Volatile
    private var registryByIdentity = IntIntHashMap(missingValue = -1)

    init {
        this.dynHandlers.forEach { registry ->
            registry.attachIdentityFinder(this)
        }
        rebuildIndex()
    }

    fun jumpIfDynIdentity(handlerIdentity: HandlerIdentity, offset: Int, adjust: Boolean, context: HandlerContext): HandlerIdentity {
        val ownerIndex = registryByIdentity[handlerIdentity.value]
        if (ownerIndex == -1) return HandlerIdentity.emptyID
        return dynHandlers[ownerIndex].jumpIfDynIdentity(handlerIdentity, offset, adjust, context)
    }

    internal fun rebuildIndex() {
        synchronized(rebuildLock) {
            var expectedSize = 0
            dynHandlers.forEach { registry ->
                expectedSize += registry.currentHandlerCount()
            }

            val newIndex = IntIntHashMap(expectedSize = expectedSize, missingValue = -1)
            dynHandlers.forEachIndexed { index, registry ->
                registry.fillIdentityOwners(newIndex, index)
            }
            registryByIdentity = newIndex
        }
    }

    companion object {
        fun fromList(handlers: List<Handler>): DynIdentityFinder {
            return DynIdentityFinder(handlers.filterIsInstance<DynamicHandlersRegistry>())
        }
    }
}
