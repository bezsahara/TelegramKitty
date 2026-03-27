package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import java.util.concurrent.ConcurrentHashMap

fun interface MultiIdentity {
    fun identify(update: Update): Any?

    companion object {
        val OfMessageChatIdentity = MultiIdentity {
            (it as? MessageUpdate)?.message?.chat?.id
        }

        val OfUserChatIdentity = MultiIdentity { it.chatIdOrNull() }
    }
}

// It tries to process updates sequentially for a given identity
// While processing identities in parallel
internal class MultiUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    private val channel: ReceiveChannel<Update>,
    private val scope: CoroutineScope,
    private val identity: MultiIdentity,
    parallelism: Int,
    errorHandler: HandlerErrorHandler,
    furballConfig: FurballConfig
) : Furball(
    bot,
    botDispatchers,
    errorHandler,
    furballConfig
) {
    private val semaphore = Semaphore(parallelism)
    private val buckets = ConcurrentHashMap<Any, Mutex>()

    private val mutexFun = java.util.function.Function<Any, Mutex> { Mutex(false) }

    private suspend fun run() = coroutineScope {
        for (update in channel) {
            val id = identity.identify(update)
            semaphore.acquire()
            launch {
                if (id == null) {
                    try {
                        applyHandlers(update)
                    } finally {
                        semaphore.release()
                    }
                } else {
                    val mutex = buckets.computeIfAbsent(id, mutexFun)
                    mutex.lock(null)
                    try {
                        applyHandlers(update)
                    } finally {
                        mutex.unlock(null)
                        semaphore.release()
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