package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.utils.forList
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

fun interface MultiIdentity {
    fun identify(update: Update): Any?

    companion object {
        val OfMessageChatIdentity = MultiIdentity {
            if (it.javaClass === MessageUpdate::class.java) {
                (it as MessageUpdate).message.chat.id
            } else null
        }

        val OfAnyUserChatIdentity = MultiIdentity { it.chatIdOrNull() }

        val OfAnyUserIdentity = MultiIdentity { it.userIdOrNull() }
    }
}

// It tries to process updates sequentially for a given identity
// While processing identities in parallel
@OptIn(DelicateCoroutinesApi::class)
internal class MultiUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    private val channel: Channel<Update>,
    private val scope: CoroutineScope,
    private val identity: MultiIdentity,
    private val parallelism: Int,
    errorHandler: HandlerErrorHandler,
    furballConfig: FurballConfig
) : Furball(
    bot,
    botDispatchers,
    errorHandler,
    furballConfig,
    channel
) {
    private val shards = if (furballConfig.multiUpdaterUseMap)
        ShardsMap.HashMap(furballConfig.multiUpdaterMapLimit) else ShardsMap.ArrayMap.fromParallelism(parallelism)
    private val ready = Channel<Any>(1024)

    private suspend fun run() = coroutineScope {
        while (isActive) {
            val update = channel.receive()
            val id = identity.identify(update)
            shards.registerStart()
            if (id != null) {
                val shard = shards[id]
                shard.queue.add(update)
                if (shard.scheduled.compareAndSet(false, true)) {
                    ready.send(shard)
                }
            } else {
                ready.send(update)
            }
        }
    }

    private suspend fun worker() = coroutineScope {
        while (isActive) {
            val shard = ready.receive()

            if (shard.javaClass !== Shard::class.java) {
                applyHandlers(shard as Update)
                shards.registerEnd()
                continue
            }

            val q = (shard as Shard).queue
            while (true) {
                val update = q.poll() ?: break
                applyHandlers(update)
                shards.registerEnd()
            }

            shard.scheduled.set(false)

            if (!q.isEmpty() && shard.scheduled.compareAndSet(false, true)) {
                ready.send(shard)
            }
        }
    }

    override fun start() {
        repeat(parallelism) {
            scope.launch {
                worker()
            }
        }
        scope.launch() {
            run()
        }
    }
}

internal class Shard {
    val queue = ConcurrentLinkedQueue<Update>()
    val scheduled = AtomicBoolean(false)
}

internal sealed class ShardsMap {
    abstract operator fun get(key: Any): Shard
    abstract fun registerStart()
    abstract fun registerEnd()

    class HashMap(private val initialCleaningBound: Int) : ShardsMap() {
        private val map = java.util.HashMap<Any, ShardEntry>()
        private val active = AtomicInteger()

        // `0` means "no previous clean". This avoids the first clean treating fresh entries as stale.
        private var lastClean = 0L
        // Adaptive upper bound used by pressure checks. It can grow and shrink, but never below the initial floor.
        private var currentCleaningBound = initialCleaningBound
        // Raised bounds decay only after enough idle starts so short bursts do not immediately collapse the bound back.
        private var idleStartsSinceGrowth = 0

        init {
            require(initialCleaningBound > 0) { "initialCleaningBound must be positive" }
        }

        override fun registerStart() {
            val bound = currentCleaningBound
            val size = map.size
            if (size > bound) {
                idleStartsSinceGrowth = 0
                // Regular pressure cleans only run when the updater is idle.
                // Once the map is far above the bound we force a blocking clean and grow more aggressively.
                if (size > forcedBound(bound)) {
                    waitUntilInactive()
                    cleanMap(CleanTrigger.ForcedPressure)
                } else if (active.get() == 0) {
                    cleanMap(CleanTrigger.Pressure)
                }
            } else if (active.get() == 0 && bound > initialCleaningBound) {
                // A raised bound decays only from idle starts, never from the hot path while work is in flight.
                idleStartsSinceGrowth++
                if (idleStartsSinceGrowth >= MAINTENANCE_IDLE_STARTS) {
                    idleStartsSinceGrowth = 0
                    cleanMap(CleanTrigger.Maintenance)
                }
            }

            active.incrementAndGet()
        }

        // runs on several threads
        override fun registerEnd() {
            val left = active.decrementAndGet()
            check(left >= 0) { "MultiUpdater mutex map registerEnd was called more times than registerStart" }
        }

        private fun waitUntilInactive() {
            val start = System.currentTimeMillis()

            while (true) {
                var counter = 0
                while (active.get() != 0 && counter < 1_000_000) {
                    Thread.onSpinWait()
                    counter++
                }
                val now = System.currentTimeMillis()
                if (active.get() == 0) return
                if (now - start > FORCE_CLEAN_WAIT_MS) {
                    throw KittyError("Map size limit is too small! Program cannot handle cleaning it!")
                }
            }
        }

        private fun cleanMap(trigger: CleanTrigger) {
            val previousClean = lastClean
            lastClean = System.currentTimeMillis()

            // Phase 1: drop shards untouched since the previous clean window.
            val iterator = map.entries.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().value.lastAccessed <= previousClean) {
                    iterator.remove()
                }
            }

            val sizeAfterStaleClean = map.size
            // Phase 2: adapt the bound based on how much live data survived the stale-entry pass.
            if (trigger !== CleanTrigger.Maintenance && sizeAfterStaleClean > currentCleaningBound) {
                growBound(sizeAfterStaleClean, trigger)
            } else if (trigger === CleanTrigger.Maintenance) {
                shrinkBound(sizeAfterStaleClean)
            }

            // Phase 3: if the live set is still larger than the bound, evict the oldest survivors down to the bound.
            if (map.size <= currentCleaningBound) return

            val removeCount = map.size - currentCleaningBound

            val oldestKeys = map.entries.toMutableList()
            oldestKeys.sortBy { it.value.lastAccessed }

            for (keyIdx in 0 until min(oldestKeys.size, removeCount)) {
                val key = oldestKeys[keyIdx].key
                map.remove(key)
            }
        }

        private fun growBound(sizeAfterStaleClean: Int, trigger: CleanTrigger) {
            // Normal pressure grows by 1.5x. Forced pressure grows by 2x.
            // The live-set term adds headroom when the survivor set is already larger than those multipliers.
            val targetFromGrowth = when (trigger) {
                CleanTrigger.ForcedPressure -> scale(currentCleaningBound, 2, 1)
                CleanTrigger.Pressure -> scale(currentCleaningBound, 3, 2)
                CleanTrigger.Maintenance -> currentCleaningBound
            }
            val targetFromLiveSet = scale(sizeAfterStaleClean, 5, 4)
            currentCleaningBound = maxOf(initialCleaningBound, currentCleaningBound, targetFromGrowth, targetFromLiveSet)
            idleStartsSinceGrowth = 0
        }

        private fun shrinkBound(sizeAfterStaleClean: Int) {
            if (currentCleaningBound <= initialCleaningBound) return
            // If two-thirds of the raised bound is still genuinely in use, keep the larger bound.
            if (sizeAfterStaleClean > scale(currentCleaningBound, 2, 3)) return

            // Decay by 1/2, but keep enough room for the current live set plus 50% headroom.
            val targetFromDecay = scale(currentCleaningBound, 1, 2)
            val targetFromUsage = scale(sizeAfterStaleClean, 3, 2)
            currentCleaningBound = maxOf(initialCleaningBound, targetFromDecay, targetFromUsage)
            if (currentCleaningBound == initialCleaningBound) {
                idleStartsSinceGrowth = 0
            }
        }

        override fun get(key: Any): Shard {
            val res = map.computeIfAbsent(key, mutexFun)
            res.lastAccessed = System.currentTimeMillis()
            return res.shard
        }

        private class ShardEntry {
            val shard = Shard()
            var lastAccessed = 0L
        }

        private enum class CleanTrigger {
            // A regular idle clean because the map is above the current bound.
            Pressure,
            // A stronger clean when the map is far above the bound and we are willing to block to recover.
            ForcedPressure,
            // Periodic idle maintenance used only to decay a previously raised bound.
            Maintenance
        }

        private companion object {
            private const val MAINTENANCE_IDLE_STARTS = 32
            private const val FORCE_CLEAN_WAIT_MS = 30_000L

            private val mutexFun = java.util.function.Function<Any, ShardEntry> { ShardEntry() }

            private fun forcedBound(bound: Int): Int = scale(bound, 3, 2)

            private fun scale(value: Int, numerator: Int, denominator: Int): Int {
                return ((value.toLong() * numerator + denominator - 1) / denominator)
                    .coerceAtMost(Int.MAX_VALUE.toLong())
                    .toInt()
            }
        }
    }

    class ArrayMap private constructor(size: Int) : ShardsMap() {
        private val array = Array(size) { Shard() }
        private val lastIndex = size - 1

        init {
            require(size > 0 && size and lastIndex == 0) { "ArrayMap size must be a power of two" }
        }

        override fun get(key: Any): Shard {
            var x = key.hashCode()
            x = x xor (x ushr 16)
            x *= 0x21f0aaad
            x = x xor (x ushr 15)
            x *= 0xd35a2d97.toInt()
            x = x xor (x ushr 15)
            return array[x and lastIndex]
        }

        override fun registerStart() {}

        override fun registerEnd() {}

        companion object {
            private const val MIN_STRIPES = 64
            private const val STRIPES_PER_PARALLELISM = 20
            private const val MAX_STRIPES = 1 shl 30

            fun fromParallelism(parallelism: Int): ArrayMap {
                require(parallelism > 0) { "parallelism must be positive" }

                val requested = parallelism.toLong() * STRIPES_PER_PARALLELISM
                require(requested <= MAX_STRIPES) { "parallelism is too high for ArrayMap" }

                return ArrayMap(nextPowerOfTwo(maxOf(MIN_STRIPES, requested.toInt())))
            }

            private fun nextPowerOfTwo(value: Int): Int {
                val highest = Integer.highestOneBit(value)
                return if (value == highest) value else highest shl 1
            }
        }
    }
}
