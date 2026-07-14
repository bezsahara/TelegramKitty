package org.bezsahara.kittybot.bot.conv2

import kotlinx.coroutines.*
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * More lightweight version of conversations.
 * You manage conversation instances yourself
 */
fun HandlerStore.conversations(allowedKinds: Set<UpdKind>? = null): ConversationManager {
    return ConversationManager(this, allowedKinds).also { addHandler(it) }
}

interface UpdateReceiver<T: Any> {
    val kind: UpdKind

    fun receive(update: Update, context: HandlerContext): T?
}

internal data class URInfo(
    val deferred: CompletableDeferred<Any>,
    val updateReceiver: UpdateReceiver<Any>
) {
    @JvmField
    var cleared: Boolean = false
}

class ConversationManager(
    origin: HandlerStore,
    override val allowedKinds: Set<UpdKind>? = null
) : Handler {

    private val receivers = (allowedKinds ?: telegramUpdateKinds).let { kinds ->
        val array = arrayOfNulls<ConcurrentLinkedQueue<URInfo>>(telegramUpdateKinds.size)
        kinds.forEach { kind ->
            array[kind.ordinal] = ConcurrentLinkedQueue<URInfo>()
        }
        array
    }

    private val scope = CoroutineScope(origin.felineDispatcher.felineBuilder.supervisorJob + Dispatchers.IO)

    inner class Scope(
        val coroutineScope: CoroutineScope
    ) : CoroutineScope by coroutineScope {
        fun endConversation(cancellationException: CancellationException = CancellationException()): Nothing {
            coroutineScope.cancel(cancellationException)
            throw cancellationException
        }

        suspend inline fun <T: Any> receive(r: UpdateReceiver<T>): Deferred<T> = receive(r, currentCoroutineContext().job)

        suspend inline fun <T: Any> receive(
            kind: UpdKind,
            crossinline r: (Update) -> T?
        ): Deferred<T> = receive(object : UpdateReceiver<T> {
            override val kind: UpdKind
                get() = kind

            override fun receive(update: Update, context: HandlerContext): T? {
                return r(update)
            }
        }, currentCoroutineContext().job)


        fun <T: Any> receive(r: UpdateReceiver<T>, parent: Job? = null): Deferred<T> {
            val queue = receivers[r.kind.ordinal] ?: error("This conversation was not configured to allow ${r.kind} updates")
            val cd = CompletableDeferred<T>(parent)
            val urInfo = URInfo(cd as CompletableDeferred<Any>, r as UpdateReceiver<Any>)
            cd.invokeOnCompletion {
                if (!urInfo.cleared) {
                    queue.remove(urInfo)
                }
            }
            queue.offer(urInfo)
            return cd
        }
    }

    fun newConversation(block: suspend Scope.() -> Unit): Job {
        return scope.launch(block = object : suspend (CoroutineScope) -> Unit {
            override suspend fun invoke(p1: CoroutineScope) {
                Scope(p1).block()
            }
        })
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val queue = receivers[update.ordinal] ?: return Decision.Next
        if (queue.isEmpty()) return Decision.Next

        val iter = queue.iterator()

        while (iter.hasNext()) {
            val urInfo = iter.next()
            val deferred = urInfo.deferred
            if (deferred.isCompleted) continue
            val res = try {
                urInfo.updateReceiver.receive(update, handlerContext) ?: continue
            } catch (e: Throwable) {
                iter.remove()
                urInfo.cleared = true
                deferred.completeExceptionally(e)
                continue
            }
            iter.remove()
            urInfo.cleared = true
            if (deferred.complete(res)) {
                return Decision.Consumed
            }
        }

        return Decision.Next
    }
}