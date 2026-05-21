@file:Suppress("DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_WARNING", "DataClassPrivateConstructor")

package org.bezsahara.kittybot.bot.updates.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.utils.TReturns
import org.bezsahara.kittybot.telegram.utils.errorOrNull
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

// Purpose of this class is to make sure you are within api rate limits
// However, it might be incorrect impl. Telegram does not specify exact rate limiting mechanism
// Make sure to create just one instance! for one bot
// https://core.telegram.org/bots/faq#my-bot-is-hitting-limits-how-do-i-avoid-this
class ApiRateController(
    // Safer than exactly 30/sec because Telegram says "about 30".
    globalSpacing: Duration = 35.milliseconds,

    // 1 request/sec per private chat.
    privateChatSpacing: Duration = 1.seconds,

    // 20 requests/min per group = 1 request every 3 seconds.
    groupChatSpacing: Duration = 3.seconds,

    evictAfter: Duration = 10.minutes,

    @Volatile
    private var mapThreshold: Int = 10_000,
) {
    @PublishedApi
    internal val privateChatSpacingNanos = privateChatSpacing.inWholeNanoseconds
    @PublishedApi
    internal val groupChatSpacingNanos = groupChatSpacing.inWholeNanoseconds
    private val evictAfterNanos = evictAfter.inWholeNanoseconds

    init {
        require(globalSpacing.inWholeNanoseconds > 0)
        require(privateChatSpacingNanos > 0)
        require(groupChatSpacingNanos > 0)
        require(evictAfterNanos > 0)
        require(mapThreshold > 0)
    }

    class ChatState {
        val lock = Mutex()

        // Next legal start time for this chat.
        var nextChatStartNanos: Long = 0L

        @Volatile
        var lastUsedNanos: Long = System.nanoTime()

        val activeUsers = AtomicInteger(0)
    }

    private class ReservingGate(
        private val spacingNanos: Long,
    ) {
        private var nextAtNanos: Long = 0L

        @Synchronized
        fun reserveAtLeast(earliestNanos: Long = 0L): Long {
            val now = System.nanoTime()
            val startAt = maxOf(now, nextAtNanos, earliestNanos)
            nextAtNanos = startAt + spacingNanos
            return startAt
        }
    }

    private val globalGate = ReservingGate(globalSpacing.inWholeNanoseconds)

    @PublishedApi
    internal val privateChats = ConcurrentHashMap<ChatId, ChatState>()
    @PublishedApi
    internal val groupChats = ConcurrentHashMap<ChatId, ChatState>()

    private val cleaning = AtomicBoolean(false)

    private fun cleanMapMaybe() {
        val totalSize = privateChats.size + groupChats.size
        if (totalSize <= mapThreshold) return

        if (!cleaning.compareAndSet(false, true)) return

        try {
            val now = System.nanoTime()

            cleanMap(privateChats, now)
            cleanMap(groupChats, now)

            val newTotalSize = privateChats.size + groupChats.size

            if (
                mapThreshold != Int.MAX_VALUE &&
                newTotalSize > (mapThreshold * 2L / 3L)
            ) {
                mapThreshold = (mapThreshold * 3L / 2L)
                    .coerceAtMost(Int.MAX_VALUE.toLong())
                    .toInt()
            }
        } finally {
            cleaning.set(false)
        }
    }

    private fun cleanMap(
        map: ConcurrentHashMap<ChatId, ChatState>,
        now: Long,
    ) {
        for (chatId in map.keys) {
            map.computeIfPresent(chatId) { _, state ->
                val inactive = state.activeUsers.get() == 0
                val expired = now - state.lastUsedNanos > evictAfterNanos

                if (inactive && expired) null else state
            }
        }
    }

    fun acquireState(
        map: ConcurrentHashMap<ChatId, ChatState>,
        chatId: ChatId,
    ): ChatState {
        cleanMapMaybe()

        return map.compute(chatId, BiFunction { _, existing ->
            val state = existing ?: ChatState()
            state.activeUsers.incrementAndGet()
            state.lastUsedNanos = System.nanoTime()
            state
        })!!
    }

    fun releaseState(state: ChatState) {
        state.lastUsedNanos = System.nanoTime()
        state.activeUsers.decrementAndGet()
    }

    fun computeWhenToStart(
        state: ChatState,
        chatSpacingNanos: Long,
    ): Long {
        val startAt = globalGate.reserveAtLeast(state.nextChatStartNanos)
        state.nextChatStartNanos = startAt + chatSpacingNanos
        state.lastUsedNanos = System.nanoTime()
        return startAt
    }

    // If you use this as part of broadcasting mechanism
    // add additional delay for it so that messages for it will not
    // reserve future slots
    suspend inline fun <T : TReturns> submitPrivate(
        chatId: ChatId,
        action: suspend () -> T,
    ): T {
        val state = acquireState(privateChats, chatId)

        try {
            return state.lock.withLock {
                val startAt = computeWhenToStart(state, privateChatSpacingNanos)
                val wait = startAt - System.nanoTime()
                if (wait > 0) {
                    delay(wait.nanoseconds)
                }
                action()
            }
        } finally {
            releaseState(state)
        }
    }

    suspend inline fun <T : TReturns> submitGroup(
        chatId: ChatId,
        action: suspend () -> T,
    ): T {
        val state = acquireState(groupChats, chatId)

        try {
            return state.lock.withLock {
                val startAt = computeWhenToStart(state, groupChatSpacingNanos)
                val wait = startAt - System.nanoTime()
                if (wait > 0) {
                    delay(wait.nanoseconds)
                }
                action()
            }
        } finally {
            releaseState(state)
        }
    }

    companion object {
        var defaultConstructor = { ApiRateController() }
    }
}

private val instances = ConcurrentHashMap<FelineDispatcher, ApiRateController>()

val HandlerStore.apiRateController: ApiRateController
    get() {
        return instances.computeIfAbsent(felineDispatcher, Function { ApiRateController.defaultConstructor() })
    }

suspend inline fun <T : TReturns> HandlerStore.submitPrivate(chatId: ChatId, block: () -> T): T {
    return apiRateController.submitPrivate(chatId, block)
}

suspend inline fun <T : TReturns> HandlerStore.submitGroup(chatId: ChatId, block: () -> T): T {
    return apiRateController.submitGroup(chatId, block)
}

// Can be used if u expected to get something like ResponseParameters(migrateToChatId=null, retryAfter=9)
// It will catch it and automatically retry
suspend inline fun <T : TReturns> sendOrRetry(cancelLimitSeconds: Int = 60, block: suspend () -> T): T {
    while (true) {
        val r = block.invoke()
        r.errorOrNull()?.also { error ->
            val retryAfter = error.parameters?.retryAfter
            if (retryAfter != null && retryAfter <= cancelLimitSeconds) {
                delay(retryAfter * 1000)
                continue
            } else {
                return r
            }
        }
        return r
    }
}

