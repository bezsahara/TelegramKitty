package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotConfig
import org.bezsahara.kittybot.bot.builder.KittyBotPolling
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.updates.updaters.MultiIdentity
import org.bezsahara.kittybot.bot.updates.updaters.ShardsMap
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.client.CustomRequest
import org.bezsahara.kittybot.telegram.client.CustomResponse
import org.bezsahara.kittybot.telegram.values.ChatType
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import java.net.URI

class MultiUpdaterTest {

    @Test
    fun arrayMapFromParallelismUsesPowerOfTwoStripeCounts() {
        val cases = mapOf(
            1 to 64,
            2 to 64,
            4 to 128,
            8 to 256,
            10 to 256,
            16 to 512,
            33 to 1024,
        )

        cases.forEach { (parallelism, expectedStripes) ->
            val map = ShardsMap.ArrayMap.fromParallelism(parallelism)

            assertEquals(expectedStripes, map.stripeCount(), "parallelism=$parallelism")
            assertTrue(map.stripeCount().isPowerOfTwo(), "parallelism=$parallelism")
        }
    }

    @Test
    fun arrayMapFromParallelismRejectsNonPositiveParallelism() {
        assertFailsWith<IllegalArgumentException> {
            ShardsMap.ArrayMap.fromParallelism(0)
        }
        assertFailsWith<IllegalArgumentException> {
            ShardsMap.ArrayMap.fromParallelism(-1)
        }
    }

    @Test
    fun arrayMapReturnsSameStripeForEqualKeys() {
        val map = ShardsMap.ArrayMap.fromParallelism(10)
        val first = String(charArrayOf('c', 'h', 'a', 't'))
        val second = String(charArrayOf('c', 'h', 'a', 't'))

        assertEquals(first, second)
        assertSame(map[first], map[second])
    }

    @Test
    fun hashMapRegisterEndCanRunFromManyThreads() {
        val map = ShardsMap.HashMap(initialCleaningBound = 1_000)
        val workerCount = 64
        val start = CountDownLatch(1)
        val done = CountDownLatch(workerCount)
        val errors = Collections.synchronizedList(mutableListOf<Throwable>())

        repeat(workerCount) {
            map.registerStart()
        }

        val threads = List(workerCount) { index ->
            thread(start = true, name = "mutex-map-register-end-$index") {
                try {
                    start.await()
                    map.registerEnd()
                } catch (e: Throwable) {
                    errors += e
                } finally {
                    done.countDown()
                }
            }
        }

        start.countDown()

        assertTrue(done.await(5, TimeUnit.SECONDS), "worker threads did not finish")
        threads.forEach { it.join(1_000) }
        assertTrue(errors.isEmpty(), errors.joinToString(separator = "\n") { it.stackTraceToString() })
        assertEquals(0, map.activeCount())
    }

    @Test
    fun hashMapDoesNotCleanUntilWorkerThreadsDrainActiveUpdates() {
        val cleaningBound = 8
        val insertedKeys = 10
        val map = ShardsMap.HashMap(initialCleaningBound = cleaningBound)
        val releaseWorker = CountDownLatch(1)
        val workerDone = CountDownLatch(1)
        val workerError = AtomicReference<Throwable?>()

        map.registerStart()
        repeat(insertedKeys) { map["chat-$it"] }
        assertEquals(insertedKeys, map.backingSize())

        val worker = thread(start = true, name = "mutex-map-active-worker") {
            try {
                releaseWorker.await()
                map.registerEnd()
            } catch (e: Throwable) {
                workerError.set(e)
            } finally {
                workerDone.countDown()
            }
        }

        map.registerStart()
        try {
            assertEquals(insertedKeys, map.backingSize())
        } finally {
            map.registerEnd()
        }

        assertEquals(insertedKeys, map.backingSize())
        assertEquals(1, map.activeCount())

        releaseWorker.countDown()
        assertTrue(workerDone.await(5, TimeUnit.SECONDS), "worker thread did not finish")
        worker.join(1_000)
        workerError.get()?.let { throw AssertionError("worker thread failed", it) }
        assertEquals(0, map.activeCount())

        map.registerStart()
        try {
            assertTrue(map.backingSize() <= map.currentCleaningBound())
            assertTrue(map.currentCleaningBound() >= cleaningBound)
        } finally {
            map.registerEnd()
        }
    }

    @Test
    fun hashMapRegisterEndDetectsUnderflow() {
        val map = ShardsMap.HashMap(initialCleaningBound = 4)

        assertFailsWith<IllegalStateException> {
            map.registerEnd()
        }
    }

    @Test
    fun hashMapDoesNotGrowWithoutPressure() {
        val initialCleaningBound = 10
        val map = ShardsMap.HashMap(initialCleaningBound = initialCleaningBound)

        repeat(64) {
            map.registerStart()
            map.registerEnd()
        }

        assertEquals(initialCleaningBound, map.currentCleaningBound())
        assertEquals(0, map.backingSize())

        map.registerStart()
        repeat(initialCleaningBound - 1) { map["chat-$it"] }
        map.registerEnd()

        repeat(16) {
            map.registerStart()
            map.registerEnd()
        }

        assertEquals(initialCleaningBound, map.currentCleaningBound())
        assertEquals(initialCleaningBound - 1, map.backingSize())
    }

    @Test
    fun hashMapPressureGrowthUsesOneAndHalfMultiplier() {
        val initialCleaningBound = 10
        val map = ShardsMap.HashMap(initialCleaningBound = initialCleaningBound)

        map.registerStart()
        repeat(11) { map["chat-$it"] }
        map.registerEnd()

        map.registerStart()
        try {
            assertEquals(15, map.currentCleaningBound())
            assertEquals(11, map.backingSize())
        } finally {
            map.registerEnd()
        }
    }

    @Test
    fun hashMapForcedPressureGrowthUsesTwoTimesMultiplier() {
        val initialCleaningBound = 10
        val map = ShardsMap.HashMap(initialCleaningBound = initialCleaningBound)

        map.registerStart()
        repeat(16) { map["chat-$it"] }
        map.registerEnd()

        map.registerStart()
        try {
            assertEquals(20, map.currentCleaningBound())
            assertEquals(16, map.backingSize())
        } finally {
            map.registerEnd()
        }
    }

    @Test
    fun hashMapDoesNotGrowWhenOverboundEntriesAreAlreadyStale() {
        val initialCleaningBound = 10
        val map = ShardsMap.HashMap(initialCleaningBound = initialCleaningBound)

        map.registerStart()
        repeat(11) { map["chat-$it"] }
        map.registerEnd()
        map.setLastClean(Long.MAX_VALUE)

        map.registerStart()
        try {
            assertEquals(initialCleaningBound, map.currentCleaningBound())
            assertEquals(0, map.backingSize())
        } finally {
            map.registerEnd()
        }
    }

    @Test
    fun hashMapMaintenanceShrinkIsGradualAndStopsAtInitialFloor() {
        val initialCleaningBound = 10
        val map = ShardsMap.HashMap(initialCleaningBound = initialCleaningBound)

        map.registerStart()
        repeat(16) { map["chat-$it"] }
        map.registerEnd()
        map.registerStart()
        map.registerEnd()
        assertEquals(20, map.currentCleaningBound())

        map.registerStart()
        repeat(16) { map["chat-$it"] }
        repeat(15) { map["chat-${16 + it}"] }
        map.registerEnd()
        map.registerStart()
        map.registerEnd()
        assertEquals(40, map.currentCleaningBound())

        repeat(31) {
            map.registerStart()
            map.registerEnd()
        }
        assertEquals(40, map.currentCleaningBound())

        map.registerStart()
        map.registerEnd()
        assertEquals(20, map.currentCleaningBound())

        repeat(32) {
            map.registerStart()
            map.registerEnd()
        }
        assertEquals(initialCleaningBound, map.currentCleaningBound())
        assertEquals(0, map.backingSize())
    }

    @Test
    fun multiUpdaterKeepsSameIdentityUpdatesInArrivalOrder() = runBlocking {
        val started = Collections.synchronizedList(mutableListOf<Long>())
        val releaseFirst = CompletableDeferred<Unit>()
        val finished = CountDownLatch(3)
        val active = AtomicInteger()
        val maxActive = AtomicInteger()

        val handler = object : Handler {
            override val allowedKinds: Set<UpdKind> = setOf(MessageUpdate)

            override suspend fun handleUpdate(
                update: Update,
                bot: KittyBot,
                handlerContext: HandlerContext,
            ): Decision {
                val messageUpdate = update as MessageUpdate
                started += messageUpdate.message.messageId

                val running = active.incrementAndGet()
                while (true) {
                    val previous = maxActive.get()
                    if (running <= previous || maxActive.compareAndSet(previous, running)) break
                }

                try {
                    if (messageUpdate.message.messageId == 1L) {
                        releaseFirst.await()
                    } else {
                        delay(25)
                    }
                } finally {
                    active.decrementAndGet()
                    finished.countDown()
                }

                return Decision.Consumed
            }
        }

        withMultiUpdater(handler) { config ->
            config.updater.start()

            config.updatesChannel.send(messageUpdate(chatId = 42L, messageId = 1L))
            config.updatesChannel.send(messageUpdate(chatId = 42L, messageId = 2L))
            config.updatesChannel.send(messageUpdate(chatId = 42L, messageId = 3L))

            delay(150)
            assertEquals(listOf(1L), started.toList())
            assertEquals(1, maxActive.get())

            releaseFirst.complete(Unit)

            assertTrue(finished.await(5, TimeUnit.SECONDS), "updates did not finish")
            assertEquals(listOf(1L, 2L, 3L), started.toList())
            assertEquals(1, maxActive.get())
        }
    }

    private fun ShardsMap.ArrayMap.stripeCount(): Int {
        val field = javaClass.getDeclaredField("array")
        field.isAccessible = true
        return (field.get(this) as Array<*>).size
    }

    private fun ShardsMap.HashMap.backingSize(): Int {
        val field = javaClass.getDeclaredField("map")
        field.isAccessible = true
        return (field.get(this) as Map<*, *>).size
    }

    private fun ShardsMap.HashMap.activeCount(): Int {
        val field = javaClass.getDeclaredField("active")
        field.isAccessible = true
        return (field.get(this) as AtomicInteger).get()
    }

    private fun ShardsMap.HashMap.currentCleaningBound(): Int {
        val field = javaClass.getDeclaredField("currentCleaningBound")
        field.isAccessible = true
        return field.getInt(this)
    }

    private fun ShardsMap.HashMap.setLastClean(value: Long) {
        val field = javaClass.getDeclaredField("lastClean")
        field.isAccessible = true
        field.setLong(this, value)
    }

    private suspend fun withMultiUpdater(
        vararg handlers: Handler,
        block: suspend (KittyBotConfig<*>) -> Unit,
    ) {
        val config = KittyBotPolling {
            token = "test-token"
            updaterMode = UpdaterMode.MultiThread(MultiIdentity.OfAnyUserChatIdentity, 2)
            furballConfig = FurballConfig.Default.copy(multiUpdaterUseMap = true)
            useCustomClient(NoOpCustomClient)
            setErrorHandler(HandlerErrorHandler { e, _, _, _, _ -> throw e })
            dispatchers {
                handlers.forEach(::addHandler)
            }
        }

        try {
            block(config)
        } finally {
            config.supervisorJob.cancel()
            config.close()
        }
    }

    private fun messageUpdate(chatId: Long, messageId: Long): MessageUpdate {
        return MessageUpdate(
            updateId = messageId,
            message = Message(
                messageId = messageId,
                date = 0L,
                chat = Chat(id = chatId, type = ChatType.PRIVATE),
                from = User(id = chatId + 1, isBot = false, firstName = "Test"),
                text = "hello",
            ),
        )
    }

    private object NoOpCustomClient : CustomClient {
        override suspend fun CoroutineScope.createMPRequest(
            urlAbs: URI,
            contentType: String,
        ): CustomRequest {
            error("No multipart requests are expected in MultiUpdater tests")
        }

        override suspend fun sendJSONRequest(
            urlAbs: URI,
            json: ByteArray,
            isGetUpdates: Boolean,
        ): CustomResponse {
            error("No API requests are expected in MultiUpdater tests")
        }

        override fun close() {}
    }

    private fun Int.isPowerOfTwo(): Boolean = this > 0 && (this and (this - 1)) == 0
}
