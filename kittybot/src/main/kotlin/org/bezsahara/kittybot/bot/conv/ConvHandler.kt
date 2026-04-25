package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentityDelegate
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface ConvHandlerBuilder<T: ConvScope> {
    abstract fun ConversationBuilder.build(block: suspend T.() -> Unit): ConvHandler<T>
}

abstract class ConvHandler<T: ConvScope> : CatcherHandlerCentral(), Handler {
    final override val identity: HandlerIdentity by HandlerIdentityDelegate()

    internal abstract val scope: CoroutineScope
}

abstract class ConvScope {
    abstract val bot: KittyBot
    abstract val update: Update
    abstract val handlerContext: HandlerContext
    internal abstract val convInfo: ConvInfo
    internal abstract val scope: CoroutineScope
    internal abstract val chc: CatcherHandlerCentral

    fun endConversation(message: String = "Conversation ended"): Nothing {
        val cause = CancellationException(message)
        scope.cancel(cause)
        throw cause
    }

    suspend fun <T> receive(h: CatcherHandler<T>): Deferred<T> {
        return chc.register(h, currentCoroutineContext().job)
    }

    fun <T> receive(h: CatcherHandler<T>, job: Job? = null): Deferred<T> {
        return chc.register(h, job)
    }
}
