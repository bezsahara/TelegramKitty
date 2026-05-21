package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.withStartOf
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.CallScope
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.utils.key.InlineKeyboardBuilder
import org.bezsahara.kittybot.telegram.values.KeyboardButtonStyle
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


inline fun InlineKeyboardBuilder.dynamicScope(d: DynamicCallbackQData, block: DynamicCallbackQData.Scope.() -> Unit) {
    d.Scope(this).block()
}

class DynamicCallbackQData(
    val callQIdentity: CallQIdentity
) : Handler {
    override val allowedKinds: Set<UpdKind>
        get() = setOf(CallbackQueryUpdate)

    private val counter = AtomicInteger(0)
    private val map = ConcurrentHashMap<Int, suspend CallScope.() -> Boolean>()
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val callbackQuery = (update as CallbackQueryUpdate).callbackQuery
        val data = callbackQuery.data ?: return Decision.Next
        if (!callQIdentity.owns(data)) return Decision.Next
        val int = callQIdentity.get(data).toInt(36)
        val block = map[int] ?: throw KittyError("DynamicCallbackQData did not find $int in its map!")
        if (block.invoke(CallScope(bot, update, handlerContext))) {
            map.remove(int)
        }
        return Decision.Consumed
    }

    // Returns callbackQuery data
    fun add(block: suspend CallScope.() -> Boolean): String {
        val num = counter.getAndAdd(1)
        map[num] = block
        return callQIdentity.create(num.toString(36))
    }

    inner class Scope(private val builder: InlineKeyboardBuilder) {
        fun callback(text: String, style: KeyboardButtonStyle? = null, iconCustomEmojiId: String? = null, block: suspend CallScope.() -> Boolean) {
            builder.callback(text, add(block), iconCustomEmojiId, style)
        }
    }
}

interface CallQIdentity {
    fun create(str: String): String

    fun owns(string: String): Boolean

    fun get(string: String): String

    class Prefix(val prefix: String) : CallQIdentity {
        override fun create(str: String): String {
            return prefix + str
        }

        override fun owns(string: String): Boolean {
            return string.withStartOf(prefix)
        }

        override fun get(string: String): String {
            return string.substring(prefix.length)
        }
    }

    class CharPrefix(val prefix: Char) : CallQIdentity {
        override fun create(str: String): String {
            return prefix + str
        }

        override fun owns(string: String): Boolean {
            return string.length > 0 && string[0] == prefix
        }

        override fun get(string: String): String {
            return string.substring(1)
        }
    }
}