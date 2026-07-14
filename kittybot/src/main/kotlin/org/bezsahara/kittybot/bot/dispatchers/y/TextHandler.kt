package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.TextScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.other.HandlerNS
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlin.coroutines.Continuation

abstract class TextHandler(
    onSuccess: suspend TextScope.() -> Unit
) : HandlerNS<TextScope>(onSuccess as ((TextScope, Continuation<Any?>) -> Any?)?) {
    final override val allowedKinds: Set<UpdKind> get() = setOf(MessageUpdate)
}

fun HandlerStore.text(regex: Regex, onSuccess: suspend TextScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun allowUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): TextScope? {
            val text = (update as MessageUpdate).message.text ?: return null
            if (!regex.matches(text)) return null
            return TextScope(
                update,
                bot,
                handlerContext,
                text
            )
        }
    })
}

fun HandlerStore.text(textExact: String, onSuccess: suspend TextScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun allowUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): TextScope? {
            val text = (update as MessageUpdate).message.text ?: return null
            if (!textExact.equals(text)) return null
            return TextScope(
                update,
                bot,
                handlerContext,
                text
            )
        }
    })
}

fun HandlerStore.text(onSuccess: suspend TextScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun allowUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): TextScope? {
            val text = (update as MessageUpdate).message.text ?: return null
            return TextScope(
                update,
                bot,
                handlerContext,
                text
            )
        }
    })
}

inline fun HandlerStore.text(crossinline check: (String) -> Boolean, noinline onSuccess: suspend TextScope.() -> Unit) {
    addHandler(object : TextHandler(onSuccess) {
        override fun allowUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): TextScope? {
            val text = (update as MessageUpdate).message.text ?: return null
            if (!check(text)) return null
            return TextScope(
                update,
                bot,
                handlerContext,
                text
            )
        }
    })
}