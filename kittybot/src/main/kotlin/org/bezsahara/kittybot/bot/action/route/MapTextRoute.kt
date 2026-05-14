package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

inline fun HandlerStore.textMapHandler(block: MapTextRouteBuilder.() -> Unit) {
    val builder = MapTextRouteBuilder()
    builder.block()
    addHandler(builder.build())
}

class MapTextRouteBuilder {
    private val map = mutableMapOf<String, suspend MessageScope.() -> Unit>()

    fun text(vararg match: String, block: suspend MessageScope.() -> Unit) {
        match.forEach {
            map[it] = block
        }
    }

    fun build(): MapTextRouteHandler {
        return MapTextRouteHandler(map)
    }
}

class MapTextRouteHandler(
    val map: Map<String, suspend MessageScope.() -> Unit>
) : Handler {
    override val allowedKinds: Set<UpdateKind<*>> = setOf(MessageUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val text = (update as MessageUpdate).message.text ?: return Decision.Next
        val func = map[text] ?: return Decision.Next
        return apply(MessageScope(update, bot, handlerContext), func)
    }

    private suspend fun apply(scope: MessageScope, func: suspend MessageScope.() -> Unit): Decision {
        func(scope)
        return Decision.Consumed
    }
}