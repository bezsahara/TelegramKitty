package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.replaceLast
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

inline fun HandlerStore.routing(keyGeneratorAny: KeyGeneratorAny, builder: RoutingStrategyAny.() -> Unit) {
    val rsa = RoutingStrategyAny(keyGeneratorAny, this)
    rsa.builder()
    rsa.build()
}

class RoutingStrategyAny(
    val keyGeneratorAny: KeyGeneratorAny,
    val original: HandlerStore
) {
    @PublishedApi
    internal val sections = arrayListOf<Pair<Any, RoutingPart>>()

    inline fun section(key: Any, block: RoutingPart.() -> Unit) {
        val r = RoutingPart(original)
        r.block()
        if (r.handlers.isEmpty()) return
        sections.add(key to r)
    }

    fun build() {
        if (sections.isEmpty()) return

        val map = HashMap<Any, HandlerIdentity>(sections.size * 2)
        sections.forEach { (key, part) ->
            val id = part.handlers[0].identity!!
            val prev = map.put(key, id)
            if (prev != null) {
                error("Duplicate routing key detected: `$key`")
            }
        }

        val exitHandler = sections.last().second.handlers.replaceLast { it.ensureHasIdentity() }
        original.addHandler(RoutingMainAny(keyGeneratorAny, map, exitHandler.identity!!))

        sections.forEach { (_, part) ->
            part.handlers.forEach { original.addHandler(it) }
        }
    }
}


class RoutingMainAny(
    private val keyGeneratorAny: KeyGeneratorAny,
    private val map: HashMap<Any, HandlerIdentity>,
    exitId: HandlerIdentity
) : Handler {
    private val exitDecision = Decision.AfterNextTo(exitId)
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = keyGeneratorAny.generate(update, handlerContext) ?: return exitDecision
        val id = map[key] ?: error("In RoutingMainAny specified key of `$key` was not found! Check keys u specified")
        return Decision.NextTo(id)
    }
}

class RoutingPart(val original: HandlerStore) : HandlerStore {
    val handlers = arrayListOf<Handler>()

    private var first = true
    override fun addHandler(handler: Handler) {
        if (first) {
            first = false
            handlers.add(handler.ensureHasIdentity())
            return
        }
        handlers.add(handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}