package org.bezsahara.kittybot.bot.action.mgroup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.GroupedMediaUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.message.Message
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.exp

private const val EXPECTED_MEDIA_GROUP_SIZE = 10

/**
 * Installs a handler that emits [GroupedMediaUpdate] synthetic updates for Telegram media groups.
 *
 * The handler collects consecutive message updates for the same chat and `mediaGroupId` and
 * flushes them as one grouped update after [periodLimitMillis] of inactivity, when a different
 * media group starts, or when a non-media-group message arrives for that chat.
 *
 * This helper may only be used with updater modes that preserve sequential processing for a given
 * chat.
 */
fun FelineDispatcher.setupMediaGroupHandler(periodLimitMillis: Long = 800, ignoreSequentialSafety: Boolean = false) {
    require(felineBuilder.updaterMode.isSequential() || ignoreSequentialSafety) {
        "Updater mode must be sequential for a given chat!"
    }
    addHandlerFirst(MediaGroupTransformerSeq(this, periodLimitMillis))
}

/**
 * Media-group transformer specialized for updater modes where updates already arrive sequentially
 * for a given chat.
 *
 * This class only synchronizes access between the update path and the delayed flush coroutine. It
 * does not try to make concurrent same-chat `handleUpdate` calls safe on its own.
 */
class MediaGroupTransformerSeq(
    felineDispatcher: FelineDispatcher,
    val periodLimitMillis: Long
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>> = setOf(MessageUpdate)

    private val scope = CoroutineScope(
        SupervisorJob(felineDispatcher.felineBuilder.supervisorJob)
    )

    private val pendingByChat = ConcurrentHashMap<Long, PendingMediaGroup>()

    init {
        require(periodLimitMillis >= 0) { "periodLimitMillis must be greater than or equal to 0" }
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val messageUpdate = update as MessageUpdate
        val message = messageUpdate.message
        val chatId = message.chat.id

        val mediaGroupId = message.mediaGroupId

        if (mediaGroupId == null) {
            val pendingMediaGroup = pendingByChat.remove(chatId) ?: return Decision.Next
            pendingMediaGroup.flushJob?.cancel()

            val groupedUpdate = pendingMediaGroup.tryClose(0)
            if (groupedUpdate != null) {
                handlerContext.channel.send(groupedUpdate)
                handlerContext.channel.send(update)
                return Decision.Consumed
            }

            return Decision.Next
        } else  {
            val pendingMediaGroup = pendingByChat[chatId]

            if (pendingMediaGroup != null) {
                if (pendingMediaGroup.mediaGroupId == mediaGroupId) {
                    val nextVersion = pendingMediaGroup.tryAppend(message)
                    if (nextVersion != 0L) {
                        pendingMediaGroup.flushJob?.cancel()
                        pendingMediaGroup.flushJob = scheduleFlush(
                            chatId = chatId,
                            pendingMediaGroup = pendingMediaGroup,
                            expectedVersion = nextVersion,
                            handlerContext = handlerContext,
                        )
                        return Decision.Consumed
                    }

                    pendingByChat.remove(chatId, pendingMediaGroup)
                } else {
                    pendingByChat.remove(chatId, pendingMediaGroup)
                    pendingMediaGroup.flushJob?.cancel()

                    val groupedUpdate = pendingMediaGroup.tryClose(0)
                    if (groupedUpdate != null) {
                        handlerContext.channel.send(groupedUpdate)
                    }
                }
            }
        }

        val pendingMediaGroup = PendingMediaGroup(
            mediaGroupId = mediaGroupId,
            firstMessage = message,
        )
        pendingByChat[chatId] = pendingMediaGroup
        pendingMediaGroup.flushJob = scheduleFlush(
            chatId = chatId,
            pendingMediaGroup = pendingMediaGroup,
            expectedVersion = pendingMediaGroup.currentVersion,
            handlerContext = handlerContext,
        )

        return Decision.Consumed
    }

    private fun scheduleFlush(
        chatId: Long,
        pendingMediaGroup: PendingMediaGroup,
        expectedVersion: Long,
        handlerContext: HandlerContext,
    ): Job = scope.launch {
        delay(periodLimitMillis)

        val current = pendingByChat[chatId]
        if (current !== pendingMediaGroup) {
            return@launch
        }

        val groupedUpdate = pendingMediaGroup.tryClose(expectedVersion) ?: return@launch
        pendingByChat.remove(chatId, pendingMediaGroup)
        handlerContext.channel.send(groupedUpdate)
    }
}


private class PendingMediaGroup(
    val mediaGroupId: String,
    firstMessage: Message,
) {
    private val messages = ArrayList<Message>(EXPECTED_MEDIA_GROUP_SIZE).apply { add(firstMessage) }

    private var version: Long = 1

    @get:Synchronized
    @set:Synchronized
    var flushJob: Job? = null

    val currentVersion: Long
        @Synchronized
        get() = version

    @Synchronized
    fun tryAppend(message: Message): Long {
        if (version == 0L) return 0
        messages.add(message)
        version += 1
        return version
    }

    @Synchronized
    fun tryClose(expectedVersion: Long): GroupedMediaUpdate? {
        if (version == 0L) return null
        if (expectedVersion != 0L && version != expectedVersion) {
            return null
        }

        version = 0
        return GroupedMediaUpdate(messages)
    }
}
