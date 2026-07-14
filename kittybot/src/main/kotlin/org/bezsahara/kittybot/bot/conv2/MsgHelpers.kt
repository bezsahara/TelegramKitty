package org.bezsahara.kittybot.bot.conv2

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update

// helpers

suspend inline fun <T: Any> ConversationManager.Scope.receive(updKind: UpdKind, crossinline block: (Update) -> T?): Deferred<T> {
    return receive(object : UpdateReceiver<T> {
        override val kind: UpdKind
            get() = updKind

        override fun receive(
            update: Update,
            context: HandlerContext,
        ): T? {
            return block(update)
        }
    }, currentCoroutineContext().job)
}

inline fun <T: Any> anyUpdateReceiver(updKind: UpdKind, crossinline block: (Update) -> T?): UpdateReceiver<T> {
    return object : UpdateReceiver<T> {
        override val kind: UpdKind
            get() = updKind

        override fun receive(update: Update, context: HandlerContext): T? {
            return block(update)
        }
    }
}


suspend inline fun ConversationManager.Scope.receiveText(chatId: Long, noinline filter: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(anyUpdateReceiver(MessageUpdate) {
        val message = (it as MessageUpdate).message
        if (message.chat.id != chatId) return@anyUpdateReceiver null
        val res = message.text ?: return@anyUpdateReceiver null
        if (filter != null) {
            if (!filter.invoke(res)) return@anyUpdateReceiver null
        }
        res
    })
}
