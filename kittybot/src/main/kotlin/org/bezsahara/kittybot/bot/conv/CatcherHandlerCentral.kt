package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicLong


private enum class CatchAttempt {
    NoMatch,
    Matched,
    AlreadySettled,
}

private class CatchRegistration<T>(
    val order: Long,
    val updateOrdinal: Int,
    private val catcherHandler: CatcherHandler<T>,
    val deferred: CompletableDeferred<T>,
) {
    suspend fun tryCatch(update: Update, handlerContext: HandlerContext): CatchAttempt {
        val caught = try {
            catcherHandler.catchOrNull(update, handlerContext)
        } catch (t: Throwable) {
            deferred.completeExceptionally(t)
            // TODO review
//            throw t
            return CatchAttempt.AlreadySettled
        } ?: return CatchAttempt.NoMatch

        return if (deferred.complete(caught)) {
            CatchAttempt.Matched
        } else {
            CatchAttempt.AlreadySettled
        }
    }
}

abstract class CatcherHandlerCentral {

    // Each bucket is ordered by registration time so oldest waiter is tested first.
    private val registry = Array(telegramUpdateKinds.size) {
        ConcurrentSkipListMap<Long, CatchRegistration<*>>()
    }
    private val nextOrder = AtomicLong()

    private fun isEmpty(): Boolean {
        for (i in registry.indices) {
            if (registry[i].size != 0) return false
        }
        return true
    }

    fun <T> register(catcherHandler: CatcherHandler<T>, ownerJob: Job? = null): CompletableDeferred<T> {
        val deferred = CompletableDeferred<T>()
        val registration = CatchRegistration(
            order = nextOrder.getAndIncrement(),
            updateOrdinal = catcherHandler.updateKind.ordinal,
            catcherHandler = catcherHandler,
            deferred = deferred
        )
        addToRegistry(registration)
        val ownerHandle: DisposableHandle? = ownerJob?.invokeOnCompletion { cause ->
            deferred.cancel(cause.toWaiterCancellation())
        }
        deferred.invokeOnCompletion {
            ownerHandle?.dispose()
            removeFromRegistry(registration)
        }
        return deferred
    }

    protected suspend fun handleWaiters(
        update: Update,
        handlerContext: HandlerContext
    ): Decision {
        val bucket = registry[update.ordinal]
        val lastVisibleOrder = nextOrder.get() - 1
        if (lastVisibleOrder < 0) return Decision.Next

        for (registration in bucket.headMap(lastVisibleOrder, true).values) {
            if (!registration.deferred.isActive) {
                removeFromRegistry(registration)
                continue
            }

            when (registration.tryCatch(update, handlerContext)) {
                CatchAttempt.NoMatch -> Unit
                CatchAttempt.AlreadySettled -> removeFromRegistry(registration)
                CatchAttempt.Matched -> {
                    removeFromRegistry(registration)
                    return Decision.Consumed
                }
            }
        }

        return Decision.Next
    }

    private fun addToRegistry(catcher: CatchRegistration<*>) {
        registry[catcher.updateOrdinal][catcher.order] = catcher
    }

    private fun removeFromRegistry(catcher: CatchRegistration<*>) {
        registry[catcher.updateOrdinal].remove(catcher.order, catcher)
    }

    private fun Throwable?.toWaiterCancellation(): CancellationException? {
        return when (this) {
            null -> null
            is CancellationException -> this
            else -> CancellationException("Waiter owner completed").also { it.initCause(this) }
        }
    }
}
