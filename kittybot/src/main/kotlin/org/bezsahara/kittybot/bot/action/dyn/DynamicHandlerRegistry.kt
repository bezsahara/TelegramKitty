package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.errors.KittyException
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.updates.IntIntHashMap
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds
import java.util.Arrays
import java.util.IdentityHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.ArrayDeque

fun TransparentHandlerStore.createDynamicHandlerStore(
    useIdentity: Boolean = false,
    allowedKinds: Set<UpdKind>? = null,
): DynamicHandlerRegistry {
    return DynamicHandlerRegistry(useIdentity, felineDispatcher, allowedKinds).also {
        addHandler(it)
    }
}

inline fun TransparentHandlerStore.createDynamicHandlerStore(
    useIdentity: Boolean = false,
    allowedKinds: Set<UpdKind>? = null,
    block: DynamicHandlerRegistry.() -> Unit,
): DynamicHandlerRegistry {
    return createDynamicHandlerStore(useIdentity, allowedKinds).apply(block)
}


class DynamicHandlerRegistry(
    useIdentity: Boolean,
    override val felineDispatcher: FelineDispatcher,
    override val allowedKinds: Set<UpdKind>?,
) : Handler, TransparentHandlerStore, RejectDelegate {
    @Volatile
    private var registryState = RegistryState.Empty

    // Any is ArrayDeque<HandlerInfo> OR HandlerInfo
    private val index: MutableMap<Handler, Any> =
        if (useIdentity) IdentityHashMap()
        else HashMap()

    @Volatile
    var errorHandler = felineDispatcher.felineBuilder.errorHandlerInternal

    @Volatile
    var hopSafetyTimes = felineDispatcher.felineBuilder.furballConfig.hopSafetyTimes

    @Synchronized
    fun removeHandlers(several: List<Handler>) {
        when (several.size) {
            0 -> return
            1 -> {
                removeHandler(several[0])
                return
            }
        }

        val removedList = Array(several.size) {
            val handler = several[it]
            val el = index.remove(handler) ?: error("Handler $handler not found!")

            when {
                el.javaClass === ArrayDeque::class.java -> {
                    val removed = (el as ArrayDeque<HandlerInfo>).removeLast()
                    if (!el.isEmpty()) {
                        index[handler] = el
                    }
                    removed
                }

                else -> (el as HandlerInfo)
            }
        }

        registryState = registryState.remove(removedList)
    }

    @Synchronized
    fun addHandlers(several: List<Handler>) {
        when (several.size) {
            0 -> return
            1 -> return addHandler(several[0])
        }

        val container = Array(several.size) { i ->
            val handler = several[i]
            val hi = HandlerInfo(handler)
            val value = index[handler]

            when {
                value == null -> {
                    index[handler] = hi
                }

                value.javaClass === ArrayDeque::class.java -> {
                    (value as ArrayDeque<HandlerInfo>).addLast(hi)
                }

                else -> {
                    val nr = ArrayDeque<HandlerInfo>(2)
                    nr.addLast(value as HandlerInfo)
                    nr.addLast(hi)
                    index[handler] = nr
                }
            }
            hi
        }

        registryState = registryState.addAll(container)
    }

    @Synchronized
    override fun addHandler(handler: Handler) {
        require(handler !is DynamicHandlerRegistry) { "Handler must not be a DynamicHandlerRegistry" }

        val hi = HandlerInfo(handler)
        val value = index[handler]

        when {
            value == null -> {
                index[handler] = hi
            }

            value.javaClass === ArrayDeque::class.java -> {
                (value as ArrayDeque<HandlerInfo>).addLast(hi)
            }

            else -> {
                val nr = ArrayDeque<HandlerInfo>(2)
                nr.addLast(value as HandlerInfo)
                nr.addLast(hi)
                index[handler] = nr
            }
        }

        registryState = registryState.add(hi)
    }

    @Synchronized
    fun removeHandler(handler: Handler): Boolean {
        val value = index.remove(handler) ?: return false

        val hi: HandlerInfo

        when {
            value.javaClass === ArrayDeque::class.java -> {
                hi = (value as ArrayDeque<HandlerInfo>).removeLast()
                if (!value.isEmpty()) {
                    index[handler] = value
                }
            }

            else -> {
                hi = (value as HandlerInfo)
            }
        }

        registryState = registryState.remove(hi)
        return true
    }

    @Synchronized
    fun removeLast(): Boolean {
        val state = registryState
        val size = state.array.size
        if (size == 0) return false
        registryState = state.remove(size - 1)
        return true
    }

    val size: Int get() = registryState.array.size

    override val identity: HandlerIdentity = HandlerIdentity.createNew()

    internal data class RegistryKey(
        val saved: RegistryState,
        val indexPos: Int,
    ) {
        companion object {
            @JvmField
            val exit = RegistryKey(RegistryState.Empty, 0)
        }
    }

    internal val regAttrKey = felineDispatcher.attrKeyOf<RegistryKey>("dynamic_router")

    private val thisDecision = Decision.NextTo(identity)

    internal fun createJumpDecision(context: HandlerContext, decision: Decision, updType: Int): Decision? {
        val saved = registryState
        var res = saved.identityMap[decision.result]
        if (res == -1) return null

        res += decision.offset

        if (res !in 0 until saved.array.size)
            throw KittyException("Index in dynamic registry jump decision is out of bounds")

        if (decision.adjust) {
            val index = saved.indexMap[updType]
            if (index == null) {
                context[regAttrKey] = RegistryKey.exit
                return thisDecision
            }
            res = if (res == 0) index[index.size - 1] else index[res - 1]
            if (res >= index.size) {
                context[regAttrKey] = RegistryKey.exit
                return thisDecision
            }
        }
        context[regAttrKey] = RegistryKey(saved, res)
        return thisDecision
    }

    // lower level functions if u can maintain HandlerInfo identity you can use them
    fun add(handlerInfo: HandlerInfo) {
        val state = registryState
        val array = state.array
        require(array.isEmpty() || array[array.size - 1].id <= handlerInfo.id) { "Id of handler info must be greater or equal to the last one" }
        registryState = state.add(handlerInfo)
    }

    fun addAll(handlerInfos: Array<HandlerInfo>) {
        val state = registryState
        val array = state.array
        var latest = if (array.isEmpty()) Long.MIN_VALUE else array[array.size - 1].id

        handlerInfos.forEach {
            val id = it.id
            require(id >= latest) { "Id of handler info must be greater or equal to the last one" }
            latest = id
        }

        registryState = state.addAll(handlerInfos)
    }

    fun remove(handlerInfo: HandlerInfo) {
        registryState = registryState.remove(handlerInfo)
    }

    fun removeAll(handlerInfos: Array<HandlerInfo>) {
        registryState = registryState.remove(handlerInfos)
    }

    fun remove(index: Int) {
        registryState = registryState.remove(index)
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val regKey = handlerContext[regAttrKey]

        val state: RegistryState = if (regKey != null) {
            if (regKey === RegistryKey.exit) return Decision.Next
            regKey.saved
        } else registryState

        val array = state.array
        val size = array.size
        if (array.isEmpty()) return Decision.Next

        val index = state.indexMap[update.ordinal]

        val startIndex = if (index == null) {
            regKey?.indexPos ?: return Decision.Next
        } else index[size]

        if (startIndex >= size) return Decision.Next

        return decision(index, array, update, bot, handlerContext, size, state, startIndex)
    }

    private suspend fun decision(
        index: IntArray?,
        array: Array<HandlerInfo>,
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
        size: Int,
        state: RegistryState,
        startKey: Int,
    ): Decision {
        var i = startKey
        var hopSafety = 0
        val hopSafetyLimit = hopSafetyTimes * array.size
        while (true) {
            val handler = array[i]
            val res = try {
                handler.handler.handleUpdate(update, bot, handlerContext)
            } catch (e: Throwable) {
                errorHandler.handleException(e, bot, update, handlerContext, handler.handler)
            }

            if (hopSafety > hopSafetyLimit) {
                throw KittyException("It seems there is a recursion problem in dynamic registry!")
            }

            when (res.result) {
                Decision.NEXT -> {
                    i = index?.get(i) ?: return Decision.Next
                    if (i >= size) return Decision.Next
                }

                Decision.CONSUMED -> return Decision.Consumed
                else -> {
                    i = state.identityMap[res.result]
                    hopSafety++

                    if (i == -1) {
                        return res
                    }

                    if (res.offset != 0) {
                        i += res.offset
                        if (i >= size) return Decision.Next
                        if (i < 0) throw KittyException("Index is lower than zero after offset of ${res.offset}")
                    }

                    if (res.adjust) {
                        i = when {
                            index == null -> return Decision.Next
                            i == 0 -> index[size]
                            else -> index[i - 1]
                        }
                        if (i >= size) return Decision.Next
                    }
                }
            }
        }
    }
}

internal fun findDynamicHandlers(list: List<Handler>): Array<DynamicHandlerRegistry> {
    return list.filterIsInstance<DynamicHandlerRegistry>().toTypedArray()
}

internal class RegistryState(val array: Array<HandlerInfo>) {
    val identityMap = IntIntHashMap(missingValue = -1)
    val indexMap = arrayOfNulls<IntArray>(telegramUpdateKinds.size)

    fun add(handlerInfo: HandlerInfo): RegistryState {
        return RegistryState(array + handlerInfo)
    }

    fun addAll(handlerInfos: Array<HandlerInfo>): RegistryState {
        return RegistryState(array + handlerInfos)
    }

    fun remove(handlerInfos: Array<HandlerInfo>): RegistryState {
        if (handlerInfos.isEmpty()) return this
        val storge = IntArray(handlerInfos.size)
        for (i in handlerInfos.indices) {
            val it = handlerInfos[i]
            val r = Arrays.binarySearch(array, it, Compare)
            if (r < 0) error("Not found: $it")
            storge[i] = r
        }
        Arrays.sort(storge)
        return RegistryState(array.removeIndexes(storge))
    }

    fun remove(handlerInfo: HandlerInfo): RegistryState {
        val res = Arrays.binarySearch(array, handlerInfo, Compare)
        return remove(res)
    }

    fun remove(index: Int): RegistryState {
        val lastIndex = array.size - 1
        require(index in 0..lastIndex) { "Index must be within range" }
        if (lastIndex == 0) {
            return Empty
        }
        val arr = when (index) {
            0 -> {
                Arrays.copyOfRange(array, 1, lastIndex + 1)
            }

            lastIndex -> {
                Arrays.copyOfRange(array, 0, lastIndex)
            }

            else -> {
                val new = arrayOfNulls<HandlerInfo>(lastIndex)
                System.arraycopy(array, 0, new, 0, index)
                System.arraycopy(array, index + 1, new, index, lastIndex - index)
                new as Array<HandlerInfo>
            }
        }
        return RegistryState(arr)
    }

    init {
        val map = IntArray(telegramUpdateKinds.size) { -1 }
        array.forEachIndexed { index, info ->
            info.identity?.let { id -> identityMap[id.value] = index }

            info.allowedKinds.forEach { updateKind ->
                var indexedList = indexMap[updateKind]
                if (indexedList == null) {
                    indexedList = IntArray(array.size + 1) {
                        array.size
                    }
                    indexMap[updateKind] = indexedList
                }
                val previous = map[updateKind]
                map[updateKind] = index

                if (previous == -1) {
                    indexedList[indexedList.size - 1] = index
                    for (p in 0 until index) {
                        indexedList[p] = index
                    }
                } else {
                    for (p in previous until index) {
                        indexedList[p] = index
                    }
                }
            }
        }
    }

    private object Compare : Comparator<HandlerInfo> {
        override fun compare(
            o1: HandlerInfo,
            o2: HandlerInfo,
        ): Int {
            return java.lang.Long.compare(o1.id, o2.id)
        }
    }

    companion object {
        val Empty = RegistryState(emptyArray())
    }
}

inline fun <reified T> Array<T>.removeIndexes(
    indexesToRemove: IntArray,
): Array<T> {
    // Count valid unique removals
    val result = arrayOfNulls<T>(size - indexesToRemove.size)

    var srcStart = 0
    var dst = 0
    var i = 0

    while (i < indexesToRemove.size) {
        val removeStart = indexesToRemove[i]

        // Skip duplicates
        var removeEnd = removeStart
        while (i + 1 < indexesToRemove.size && indexesToRemove[i + 1] <= removeEnd + 1) {
            i++
            removeEnd = indexesToRemove[i]
        }

        // Copy segment before removed range
        val len = removeStart - srcStart
        if (len > 0) {
            System.arraycopy(this, srcStart, result, dst, len)
            dst += len
        }

        srcStart = removeEnd + 1
        i++
    }

    // Copy tail after last removed range
    if (srcStart < size) {
        System.arraycopy(this, srcStart, result, dst, size - srcStart)
    }

    @Suppress("UNCHECKED_CAST")
    return result as Array<T>
}

class HandlerInfo private constructor(
    val handler: Handler,
    val allowedKinds: IntArray,
    val identity: HandlerIdentity?,
) {
    val id = counter.getAndIncrement()

    constructor(original: Handler) : this(
        original.real(),
        original.allowedKinds?.let { allowedKinds ->
            val iterator = allowedKinds.iterator()
            IntArray(allowedKinds.size) {
                iterator.hasNext()
                iterator.next().ordinal
            }
        } ?: allAllowed,
        original.identity
    )

    private companion object {
        private val counter = AtomicLong(0)
        private val allAllowed = IntArray(telegramUpdateKinds.size) { it }
    }
}
