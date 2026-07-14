package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.toImmutableMap
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.TextScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update

inline fun HandlerStore.textMapHandler(block: MapTextRouteBuilder.() -> Unit) {
    val builder = MapTextRouteBuilder()
    builder.block()
    addHandler(builder.build())
}

class MapTextRouteBuilder {
    private val map = mutableMapOf<String, suspend TextScope.() -> Unit>()

    fun text(vararg match: String, block: suspend TextScope.() -> Unit) {
        match.forEach {
            map[it] = block
        }
    }
    fun build(): MapTextRouteHandler {
        return MapTextRouteHandler(map.toImmutableMap())
    }
}

class MapTextRouteHandler(
    val map: Map<String, suspend TextScope.() -> Unit>
) : Handler {
    override val allowedKinds: Set<UpdKind> = setOf(MessageUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val text = (update as MessageUpdate).message.text ?: return Decision.Next
        val func = map[text] ?: return Decision.Next
        return apply(TextScope(update, bot, handlerContext, text), func)
    }

    private suspend fun apply(scope: TextScope, func: suspend TextScope.() -> Unit): Decision {
        func(scope)
        return Decision.Consumed
    }
}
