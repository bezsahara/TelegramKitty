package org.bezsahara.kittybot.bot.updates.api

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.utils.TReturns
import java.util.concurrent.ConcurrentHashMap

class ApiRateControllerV2(private val coroutineScope: CoroutineScope) {
    private val semaphore = Semaphore(30)
    private val chatMap = ConcurrentHashMap<String, Mutex>()

    suspend fun beginStructure(chatId: ChatId): CompletableDeferred<Unit> {
        val mutex = chatMap.getOrPut(chatId.value) { Mutex() }
        mutex.lock()
        semaphore.acquire()
        val def = CompletableDeferred<Unit>()

        coroutineScope.launch {
            def.await()
            delay(1000)
            mutex.unlock()
            semaphore.release()
        }
        return def
    }

    fun endStructure(def: CompletableDeferred<Unit>) {
        def.complete(Unit)
    }

    suspend inline fun execute(chatId: ChatId, block: () -> TReturns) {
        val b = beginStructure(chatId)
        block()
        endStructure(b)
    }
}