package org.bezsahara.kittybot.bot.action.mgroup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.superVisorJob
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.message.Message
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Function


/**
 * Adds a handler that gathers messages belonging to the same Telegram media group.
 *
 * Grouping is keyed by `(chatId, mediaGroupId)`. The first message for a new key is checked with
 * [check]. If accepted, subsequent messages with the same key are appended to the same batch until
 * no new message arrives for [periodLimitMillis]. After that inactivity window the collected
 * messages are passed to [block].
 *
 * [check] is only evaluated when a new media-group batch is started.
 */
fun HandlerStore.mediaGroupHandler(
    check: MediaGroupCheck,
    periodLimitMillis: Long = 800,
    block: suspend MediaGroupScope.() -> Unit,
) {
    addHandler(MediaGroupHandler(check, felineDispatcher, periodLimitMillis, block))
}

/**
 * Handler that accumulates `MessageUpdate`s sharing the same `(chatId, mediaGroupId)` and emits
 * them as a single [MediaGroupScope].
 *
 * A batch stays open while new messages for the same key keep arriving. When the channel stays
 * idle for [periodLimitMillis], the batch is closed and [block] is invoked asynchronously in the
 * handler's internal coroutine scope.
 */
class MediaGroupHandler(
    val check: MediaGroupCheck,
    felineDispatcher: FelineDispatcher,
    val periodLimitMillis: Long,
    val block: suspend MediaGroupScope.() -> Unit,
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>>
        get() = setOf(MessageUpdate)

    private val scope = CoroutineScope(
        SupervisorJob(felineDispatcher.felineBuilder.botContext.superVisorJob())
    )

    private val map = ConcurrentHashMap<MediaKey, MediaRecord>()

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val message = (update as MessageUpdate).message
        val mediaGroupId = message.mediaGroupId ?: return Decision.Next
        val chatId = message.chat.id

        // First try to get anything from the map
        // If not a null, means that stuff still happening
        val mediaKey = MediaKey(chatId, mediaGroupId)
        val md = map[mediaKey]

        if (md != null) {
            md.channel.send(message)
            return Decision.Consumed
        }

        // check if appropriate request
        if (!check.accept(message, handlerContext)) return Decision.Next

        // Means it is our media group request
        // Now, more than one thread can come to this point
        // It does not matter which starts first, they all will have same media record
        val mediaRecord = map.computeIfAbsent(mediaKey, Function { MediaRecord(mediaGroupId) })

        // Now just send all messages for all threads that got through to here
        // Including the first one
        mediaRecord.channel.send(message)

        // Important part, allow only one thread to establish the collector
        if (mediaRecord.started.compareAndSet(false, true)) {
            scope.launch {
                val list = ArrayList<Message>(20)

                val channel = mediaRecord.channel
                while (true) {
                    val mediaRecord = withTimeoutOrNull(periodLimitMillis) {
                        channel.receive()
                    }
                    if (mediaRecord == null) {
                        break
                    } else {
                        list.add(mediaRecord)
                    }
                }
                map.remove(mediaKey, mediaRecord)

                MediaGroupScope(
                    bot, handlerContext, update, list
                ).block()
            }
        }
        return Decision.Consumed
    }

}

/**
 * Predicate used to decide whether a newly discovered media group should start collecting.
 *
 * It is called only for the first message that attempts to create a batch for a given
 * `(chatId, mediaGroupId)`. Once the batch exists, later messages with the same key are appended
 * without re-running this check.
 */
fun interface MediaGroupCheck {
    fun accept(message: Message, handlerContext: HandlerContext): Boolean

    companion object {
        val OfAnyGroupId = MediaGroupCheck { _, _ -> true }
    }
}

data class MediaKey(val chatId: Long, val mediaGroupId: String)

class MediaGroupScope(
    override val bot: KittyBot,
    override val handlerContext: HandlerContext,
    override val update: MessageUpdate,
    val mediaGroup: List<Message>,
) : HandlerScope<MessageUpdate> {
    val message get() = update.message
}

data class MediaRecord(
    val mediaGroupId: String,
) {
    val channel = Channel<Message?>(20)
    val started = AtomicBoolean(false)
}
