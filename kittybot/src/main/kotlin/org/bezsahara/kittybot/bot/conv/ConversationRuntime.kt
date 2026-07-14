package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CoroutineScope
import org.bezsahara.kittybot.bot.dispatchers.typeKeyOf
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import java.util.concurrent.ConcurrentHashMap

internal class ConversationRuntime(
    val scope: CoroutineScope
) {
    private class ConversationClaim

    private val activeConversations = ConcurrentHashMap<ChatId, ConversationClaim>()

    fun wasStarted(chatId: ChatId): Boolean {
        return activeConversations.containsKey(chatId)
    }

    fun tryStart(peerId: ChatId): Any? {
        val claim = ConversationClaim()
        return if (activeConversations.putIfAbsent(peerId, claim) == null) {
            claim
        } else {
            null
        }
    }

    fun finish(peerId: ChatId, claim: Any) {
        activeConversations.remove(peerId, claim)
    }

    companion object {
        @JvmField
        val BOT_CONTEXT_KEY = typeKeyOf<ConversationRuntime>("ConversationRuntime")
    }
}
