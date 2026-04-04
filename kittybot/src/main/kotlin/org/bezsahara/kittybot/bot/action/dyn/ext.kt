package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.dispatchers.HandlerStore

fun HandlerStore.createDynamicHandlerRegistry(): DynamicHandlersRegistry {
    return DynamicHandlersRegistry(felineDispatcher).also { addHandler(it) }
}
