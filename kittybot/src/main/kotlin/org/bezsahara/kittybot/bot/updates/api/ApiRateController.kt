@file:Suppress("DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_WARNING", "DataClassPrivateConstructor")

package org.bezsahara.kittybot.bot.updates.api

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.utils.TReturns
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlin.math.roundToInt

// Make sure to create just one instance! for one bot
class ApiRateController(
    private val evictDeltaMillis: Long = 1000 * 60 * 10, // remove chats that are not active for 10 mins
    @Volatile private var mapThreshold: Int = 10_000 // start cleaning at 10 000
) {
    data class ChatCheck private constructor(
        @Volatile var lastTime: Long,
        val mutex: Mutex
    ) {
        constructor(lastTime: Long): this(lastTime, Mutex())
        fun copy(lastTime: Long) = ChatCheck(lastTime, mutex)
    }

    private val map = ConcurrentHashMap<ChatId, ChatCheck>()

    private var cleaning = AtomicBoolean(false)

    private fun cleanMapMaybe() {
        if (map.size > mapThreshold && cleaning.compareAndSet(false, true)) {
            val currTime = System.currentTimeMillis()
            map.forEachEntry(20_000, Consumer {
                if (currTime - it.value.lastTime > evictDeltaMillis) {
                    map.remove(it.key)
                }
            })
            if (mapThreshold != Int.MAX_VALUE && mapThreshold - map.size < (mapThreshold / 3)) {
                mapThreshold = (mapThreshold.toDouble() * 1.5).roundToInt()
            }
            cleaning.set(false)
        }
    }

    private val semaphore = Semaphore(30)

    private val releaseFun = object : suspend (CoroutineScope) -> Unit {
        override suspend fun invoke(p1: CoroutineScope) {
            delay(1000)
            semaphore.release()
        }
    }

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    suspend fun <T: TReturns> submit(chatId: ChatId, action: suspend () -> T): T {
        cleanMapMaybe()

        val chatControl = map.computeIfAbsent(chatId) { ChatCheck(System.currentTimeMillis()) }

        return try {
            chatControl.mutex.lock()
            semaphore.acquire()
            action()
        } finally {
            scope.launch(block = releaseFun)
            scope.launch {
                chatControl.lastTime = System.currentTimeMillis()
                delay(1000)
                chatControl.mutex.unlock()
            }
        }
    }

    companion object {
        val instance by lazy { ApiRateController() }
    }
}
