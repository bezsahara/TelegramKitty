package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.dispatchers.*


class CollectorStoreTransparent(val original: HandlerStore) : TransparentHandlerStore, ChangingHandlerStore {
    val collection = mutableListOf<Handler>()
    override fun addHandler(handler: Handler) {
        collection.add(handler)
        original.addHandler(handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}

// Just gets you the list of handlers that were added.
// Can be useful for dynamic handler store if u need to use stuff like routing or other dsl builder
inline fun TransparentHandlerStore.collectAndAdd(block: TransparentHandlerStore.() -> Unit): List<Handler> {
    val builder = CollectorStoreTransparent(this)
    (builder).block()
    return builder.collection
}

inline fun ChangingHandlerStore.collectAndAdd(block: ChangingHandlerStore.() -> Unit): List<Handler> {
    val builder = CollectorStoreTransparent(this)
    (builder).block()
    return builder.collection
}

inline fun TransparentHandlerStore.collectSingle(block: TransparentHandlerStore.() -> Unit): Handler {
    val builder = CollectorStoreTransparent(this)
    builder.block()
    return builder.collection.single()
}

inline fun ChangingHandlerStore.collectSingle(block: ChangingHandlerStore.() -> Unit): Handler {
    val builder = CollectorStoreTransparent(this)
    builder.block()
    return builder.collection.single()
}