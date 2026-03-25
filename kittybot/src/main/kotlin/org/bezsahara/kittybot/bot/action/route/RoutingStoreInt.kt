package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.replaceLast
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

inline fun HandlerStore.routingInt(keyGeneratorInt: KeyGeneratorInt, builder: RoutingStrategyInt.() -> Unit) {
    val rsa = RoutingStrategyInt(keyGeneratorInt, this)
    rsa.builder()
    rsa.build()
}

class RoutingStrategyInt(
    val keyGeneratorInt: KeyGeneratorInt,
    val original: HandlerStore
) {
    val sections = arrayListOf<Pair<Int, RoutingPart>>()

    inline fun section(key: Int, block: RoutingPart.() -> Unit) {
        val r = RoutingPart(original)
        r.block()
        if (r.handlers.isEmpty()) return
        sections.add(key to r)
    }

    fun build() {
        if (sections.isEmpty()) return

        val map = HashMap<Int, HandlerIdentity>(sections.size * 2)
        sections.forEach { (key, part) ->
            map[key] = part.handlers[0].identity!!
        }
        if (map.size != sections.size) {
            error("Duplicate routing keys detected in RoutingStrategyInt")
        }

        val exitHandler = sections.last().second.handlers.replaceLast { it.ensureHasIdentity() }

        val lookup = RoutingMainInt.buildLookup(map.keys, map)

        original.addHandler(RoutingMainInt(keyGeneratorInt, lookup, exitHandler.identity!!))

        sections.forEach { (_, part) ->
            part.handlers.forEach { original.addHandler(it) }
        }
    }
}

class RoutingMainInt(
    private val keyGeneratorInt: KeyGeneratorInt,
    private val lookup: Lookup,
    exitId: HandlerIdentity
) : Handler {

    abstract class Lookup {
        abstract operator fun get(key: Int): HandlerIdentity?
    }

    private val exitDecision = Decision.AfterNextTo(exitId)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = keyGeneratorInt.generate(update, handlerContext)
        if (key == Int.MIN_VALUE) return exitDecision
        val id = lookup[key]
            ?: error("In RoutingMainInt specified key of `$key` was not found! Check keys u specified")
        return Decision.NextTo(id)
    }

    companion object {
        // Heuristic:
        // - must fit into a sane max range (memory)
        // - and range must be at most GAP_FACTOR * number_of_keys (tolerate some gaps)
        private const val ARRAY_MAX_RANGE: Long = 1_000_000L
        private const val GAP_FACTOR: Long = 32L

        /**
         * Returns a function that, given the final map, builds the optimal Lookup.
         * (So we only compute min/max/density once from keys, but fill using the map.)
         */
        fun buildLookup(keys: Set<Int>, map: HashMap<Int, HandlerIdentity>): Lookup {
            if (keys.isEmpty()) return MapLookup(map)

            val min = keys.minOrNull()!!
            val max = keys.maxOrNull()!!
            val range = (max.toLong() - min.toLong() + 1L)
            val n = keys.size.toLong()


            return if (range in 1..ARRAY_MAX_RANGE &&
                range <= n * GAP_FACTOR) {
                ArrayLookup.from(map, min, max)
            } else {
                MapLookup(map)
            }
        }
    }

    private class MapLookup(private val map: HashMap<Int, HandlerIdentity>) : Lookup() {
        override fun get(key: Int): HandlerIdentity? = map[key]
    }

    private class ArrayLookup(
        private val min: Int,
        private val arr: Array<HandlerIdentity?>
    ) : Lookup() {
        override fun get(key: Int): HandlerIdentity? {
            val idxL = key.toLong() - min.toLong()
            if (idxL < 0L || idxL >= arr.size.toLong()) return null
            return arr[idxL.toInt()]
        }

        companion object {
            fun from(map: HashMap<Int, HandlerIdentity>, min: Int, max: Int): ArrayLookup {
                val sizeL = (max.toLong() - min.toLong() + 1L)
                if (sizeL <= 0L || sizeL > Int.MAX_VALUE.toLong()) {
                    return ArrayLookup(min, emptyArray())
                }

                val arr = arrayOfNulls<HandlerIdentity>(sizeL.toInt())
                map.forEach { (k, id) ->
                    val idx = (k.toLong() - min.toLong()).toInt()
                    arr[idx] = id
                }
                return ArrayLookup(min, arr)
            }
        }
    }
}
