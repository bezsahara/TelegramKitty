package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore

/**
 * Builder used to register conversation entry handlers on top of an existing dispatcher store.
 *
 * All conversations created from the same bot share one [ConversationRuntime], so they reuse
 * the same background scope and conversation ownership state.
 */
class ConversationBuilder(
    private val original: TransparentHandlerStore
) {
    private val runtime: ConversationRuntime
    @PublishedApi
    internal val convInfo: ConvInfo
    init {
        val felineBuilder = original.felineDispatcher.felineBuilder
        val sj = felineBuilder.supervisorJob
        runtime = felineBuilder.botContext.getOrPut(ConversationRuntime.BOT_CONTEXT_KEY) {
            ConversationRuntime(CoroutineScope(Dispatchers.IO + sj))
        }
        convInfo = ConvInfo(runtime)
    }

    /**
     * Registers a conversation entry point.
     *
     * The [convHandler] decides when a new conversation should start and how incoming updates
     * are routed to active waiters for that conversation type.
     */
    fun <T: ConvScope> on(convHandler: ConvHandlerBuilder<T>, block: suspend T.() -> Unit) {
        original.addHandler(convHandler.run { build(block) })
    }
}


/**
 * Opens a conversation registration block on this dispatcher store.
 */
inline fun TransparentHandlerStore.buildConversation(block: ConversationBuilder.() -> Unit) {
    val cb = ConversationBuilder(this)
    block(cb)
}
