package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.createBoolUpdateKindArray
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.RejectDelegate
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.attrKeyOf
import org.bezsahara.kittybot.bot.dispatchers.real
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.updates.HandlerException
import org.bezsahara.kittybot.bot.updates.IntIntHashMap
import org.bezsahara.kittybot.telegram.classes.core.update.Update

/**
 * Mutable handler block that behaves like a scoped sub-dispatcher inside the main handler list.
 *
 * Main rules:
 * - A jump to any handler identity stored inside this registry can be initiated from anywhere in
 *   the bot. [DynIdentityFinder] resolves such identities to this registry and routes execution
 *   into it.
 * - Once inside the registry, jumps to identities that are also inside the registry stay local
 *   and are resolved against the registry's own handler array.
 * - A jump to an identity that is not present in the registry is returned unchanged so the outer
 *   [org.bezsahara.kittybot.bot.updates.Furball] dispatcher can resolve it against the global
 *   handler list.
 * - Offsets are interpreted in the registry's local index space. If a local jump plus offset
 *   moves past the last local handler, the registry finishes and returns [Decision.Next] to the
 *   outer dispatcher instead of trying to "spill" into the global list.
 *
 * Thread-safety:
 * - [addHandler] and [removeHandler] publish a fresh immutable snapshot under a small lock.
 * - [handleUpdate] reads a single published snapshot and stays lock-free on the hot path.
 * - If execution enters the registry through a dynamic identity jump, that snapshot is carried in
 *   [DynRouterInfo] so in-flight dispatch is not invalidated by concurrent mutations.
 */
class DynamicHandlersRegistry(original: FelineDispatcher) : TransparentHandlerStore, Handler, RejectDelegate {
    override val identity: HandlerIdentity = HandlerIdentity.createNew()

    private val mutationLock = Any()
    private var dynIdentityFinder: DynIdentityFinder? = null

    @Volatile
    private var state = RegistryState.EMPTY

    var errorHandler = original.felineBuilder.errorHandlerInternal

    private val dynRouterInfo = original.attrKeyOf<DynRouterInfo>("DynRouterInfo")

    /**
     * Adds a handler to the end of the dynamic block.
     *
     * The update is atomic from readers' point of view: dispatch either sees the old snapshot or
     * the new snapshot, never a partially rebuilt identity map.
     */
    override fun addHandler(handler: Handler) {
        require(handler.real() !is DynamicHandlersRegistry)
        val handlerInfo = HandlerInfo.create(handler)
        val finderToNotify: DynIdentityFinder?
        synchronized(mutationLock) {
            state = state.withAdded(handlerInfo)
            finderToNotify = dynIdentityFinder
        }
        finderToNotify?.rebuildIndex()
    }

    /**
     * Removes the first matching handler snapshot entry from this registry.
     */
    fun removeHandler(handler: Handler) {
        val handlerInfo = HandlerInfo.create(handler)
        val finderToNotify: DynIdentityFinder?
        synchronized(mutationLock) {
            val newState = state.withRemoved(handlerInfo)
            if (newState === state) return
            state = newState
            finderToNotify = dynIdentityFinder
        }
        finderToNotify?.rebuildIndex()
    }

    internal fun jumpIfDynIdentity(
        handlerIdentity: HandlerIdentity,
        offset: Int,
        adjust: Boolean,
        context: HandlerContext
    ): HandlerIdentity {
        val currentState = state
        val target = currentState.hiMap[handlerIdentity.value]
        if (target == -1) return HandlerIdentity.emptyID
        context[dynRouterInfo] = DynRouterInfo(handlerIdentity, offset, adjust, currentState.handlers, currentState.hiMap)
        return identity
    }

    internal fun attachIdentityFinder(finder: DynIdentityFinder) {
        synchronized(mutationLock) {
            dynIdentityFinder = finder
        }
    }

    internal fun currentHandlerCount(): Int {
        return state.handlers.size
    }

    internal fun fillIdentityOwners(target: IntIntHashMap, ownerIndex: Int) {
        val currentState = state
        currentState.handlers.forEach { handler ->
            handler.identity?.let { identity ->
                target[identity.value] = ownerIndex
            }
        }
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val dynRouter = handlerContext[dynRouterInfo]
        val currentState = state
        val handlers = dynRouter?.handlers ?: currentState.handlers
        val hiMap = dynRouter?.hiMap ?: currentState.hiMap

        var adjust = true

        var i = if (dynRouter != null) {
            var r = hiMap[dynRouter.jumpTo.value]
            if (r == -1) throw KittyError("HandlerIdentity of ${dynRouter.jumpTo} could not be found in DynamicHandlersRegistry")
            r += dynRouter.initialOffset
            if (r < 0) {
                throw HandlerException("pos is less than 0 after offset of ${dynRouter.initialOffset}!")
            }
            if (r >= handlers.size) {
                return Decision.Next
            }
            adjust = dynRouter.adjust
            r
        } else 0

        while (i < handlers.size) {
            val handlerInfo = handlers[i]
            if (adjust) {
                if (!handlerInfo.accepts(update.ordinal)) {
                    i++
                    continue
                }
            } else {
                adjust = true
            }
            val res = try {
                handlerInfo.handler.handleUpdate(update, bot, handlerContext)
            } catch (e: Throwable) {
                errorHandler.handleException(e, bot, update, handlerContext, handlerInfo.handler)
            }

            when (res.result) {
                Decision.NEXT -> { i++ }
                Decision.CONSUMED -> return Decision.Consumed
                else -> {
                    var here = hiMap[res.result]
                    if (here == -1) {
                        return res
                    }
                    here += res.offset
                    if (here < 0) {
                        throw HandlerException("pos is less than 0 after offset of ${res.offset}!")
                    }
                    if (here >= handlers.size) {
                        return Decision.Next
                    }
                    adjust = res.adjust
                    i = here
                }
            }
        }

        return Decision.Next
    }

    override val felineDispatcher: FelineDispatcher = original.felineDispatcher

    /**
     * Immutable registry snapshot published to readers.
     *
     * Rebuilding the identity map on each mutation keeps dispatch simple and lock-free. The
     * library expects far more reads than writes here, so the extra copy work is paid on mutation
     * instead of on the hot path.
     */
    private class RegistryState private constructor(
        val handlers: Array<HandlerInfo>,
        val hiMap: IntIntHashMap,
    ) {
        fun withAdded(handlerInfo: HandlerInfo): RegistryState {
            val newHandlers = Array(handlers.size + 1) { index ->
                if (index < handlers.size) handlers[index] else handlerInfo
            }
            return create(newHandlers)
        }

        fun withRemoved(handlerInfo: HandlerInfo): RegistryState {
            val removeIndex = handlers.indexOf(handlerInfo)
            if (removeIndex == -1) return this

            val newHandlers = Array(handlers.size - 1) { index ->
                if (index < removeIndex) handlers[index] else handlers[index + 1]
            }
            return create(newHandlers)
        }

        companion object {
            val EMPTY = create(emptyArray<HandlerInfo>())

            fun create(handlers: Array<HandlerInfo>): RegistryState {
                val hiMap = IntIntHashMap(expectedSize = handlers.size, missingValue = -1)
                handlers.forEachIndexed { index, handler ->
                    handler.identity?.let { identity ->
                        hiMap[identity.value] = index
                    }
                }
                return RegistryState(handlers, hiMap)
            }
        }
    }
}

/**
 * Precomputed handler metadata stored inside [DynamicHandlersRegistry].
 *
 * The accepted update kinds are flattened into a boolean lookup array so the dispatch loop avoids
 * set lookups on each update.
 */
data class HandlerInfo(
    val handler: Handler,
    val identity: HandlerIdentity?,
    val allowedKinds: BooleanArray?
) {

    companion object {
        fun create(handler: Handler): HandlerInfo {
            return HandlerInfo(
                handler.real(),
                handler.identity,
                handler.allowedKinds?.let { createBoolUpdateKindArray(it) }
            )
        }
    }

    fun accepts(ordinal: Int): Boolean {
        return allowedKinds?.get(ordinal) ?: true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HandlerInfo

        if (handler != other.handler) return false
        if (identity != other.identity) return false
        if (!allowedKinds.contentEquals(other.allowedKinds)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = handler.hashCode()
        result = 31 * result + (identity?.hashCode() ?: 0)
        result = 31 * result + (allowedKinds?.contentHashCode() ?: 0)
        return result
    }
}
