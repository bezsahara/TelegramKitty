package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

fun interface MultiIdentity {
    fun identify(update: Update): Any?

    companion object {
        val OfMessageChatIdentity = MultiIdentity {
            (it as? MessageUpdate)?.message?.chat?.id
        }

        val OfAnyUserChatIdentity = MultiIdentity { it.chatIdOrNull() }

        val OfAnyUserIdentity = MultiIdentity { it.userIdOrNull() }
    }
}

// It tries to process updates sequentially for a given identity
// While processing identities in parallel
internal class MultiUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    private val channel: Channel<Update>,
    private val scope: CoroutineScope,
    private val identity: MultiIdentity,
    parallelism: Int,
    errorHandler: HandlerErrorHandler,
    furballConfig: FurballConfig
) : Furball(
    bot,
    botDispatchers,
    errorHandler,
    furballConfig,
    channel
) {
    private val semaphore = Semaphore(parallelism)
    private val mutexMap: MutexMap = if (furballConfig.multiUpdaterUseMap)
        MutexMap.HashMap(50_000) else MutexMap.ArrayMap.fromParallelism(parallelism)

    private suspend fun run() = coroutineScope {
        while (isActive) {
            val update = channel.receive()
            val id = identity.identify(update)
            mutexMap.registerStart()
            if (id != null) {
                val mutex = mutexMap[id]
                mutex.lock(null)
                launch {
                    semaphore.acquire()
                    try {
                        applyHandlers(update)
                    } finally {
                        mutex.unlock(null)
                        semaphore.release()
                        mutexMap.registerEnd()
                    }
                }
            } else {
                launch {
                    semaphore.acquire()
                    try {
                        applyHandlers(update)
                    } finally {
                        semaphore.release()
                        mutexMap.registerEnd()
                    }
                }
            }
        }
    }

    override fun start() {
        scope.launch {
            run()
        }
    }
}


sealed class MutexMap {
    abstract operator fun get(key: Any): Mutex
    abstract fun registerStart()
    abstract fun registerEnd()

    class HashMap(private var cleaningBound: Int) : MutexMap() {
        private val map = java.util.HashMap<Any, MutexEntry>()
        private val active = AtomicInteger()
        private val cleaningRequested = AtomicBoolean()

        private var lastClean = System.currentTimeMillis()

        // Runs on one thread
        override fun registerStart() {
            if (map.size > cleaningBound) {
                cleaningRequested.set(true)
            }

            if (cleaningRequested.get() && active.get() == 0) {
                cleanMap()
                cleaningRequested.set(false)
            }

            active.incrementAndGet()
        }

        // runs on several threads
        override fun registerEnd() {
            val left = active.decrementAndGet()
            check(left >= 0) { "MultiUpdater mutex map registerEnd was called more times than registerStart" }
        }

        private fun cleanMap() {
            if (map.isEmpty()) return

            val previousClean = lastClean
            lastClean = System.currentTimeMillis()

            val iterator = map.entries.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().value.lastAccessed <= previousClean) {
                    iterator.remove()
                }
            }

            if (map.size <= cleaningBound) return

            val targetSize = cleaningBound / 2
            val removeCount = map.size - targetSize
            val oldestKeys = map.entries
                .asSequence()
                .sortedBy { it.value.lastAccessed }
                .take(removeCount)
                .map { it.key }
                .toList()

            for (key in oldestKeys) {
                map.remove(key)
            }
        }

        override fun get(key: Any): Mutex {
            val res = map.computeIfAbsent(key, mutexFun)
            res.lastAccessed = System.currentTimeMillis()
            return res.mutex
        }

        private class MutexEntry {
            val mutex = Mutex(false)
            var lastAccessed = 0L
        }

        private companion object {
            private val mutexFun = java.util.function.Function<Any, MutexEntry> { MutexEntry() }
        }
    }

    class ArrayMap private constructor(size: Int) : MutexMap() {
        private val array = Array(size) { Mutex(false) }
        private val lastIndex = size - 1

        init {
            require(size > 0 && size and lastIndex == 0) { "ArrayMap size must be a power of two" }
        }

        override fun get(key: Any): Mutex {
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
            private const val MIN_STRIPES = 16
            private const val STRIPES_PER_PARALLELISM = 4
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
