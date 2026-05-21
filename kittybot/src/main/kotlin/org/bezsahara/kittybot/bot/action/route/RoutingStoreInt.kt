package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.EmptyHandler
import org.bezsahara.kittybot.bot.action.other.createBoolUpdateKindArray
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.updates.IntIntHashMap
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update


class RoutingStrategyInt(
    var keyGeneratorInt: KeyGeneratorInt,
    original: HandlerStore,
) : RoutingStrategy<Int>(original) {
    @Deprecated("Use main constructor")
    constructor(
        keyGeneratorAny: KeyGeneratorInt,
        original: HandlerStore,
        ofKinds: Set<UpdKind>?,
    ) : this(keyGeneratorAny, original)

    inline fun section(key: Int, block: TransparentHandlerStore.() -> Unit) {
        val r = addOrGetSection(key, original)
        r.block()
    }

    fun build() {
        val sections = computeSections()
        if (sections.isEmpty()) return

        val map = HashMap<Int, HandlerIdentity>(sections.size * 2)
        sections.forEach { (key, part) ->
            map[key] = part.handlers[0].identity!!
        }

        if (map.size != sections.size) {
            error("Duplicate routing keys detected in RoutingStrategyInt")
        }

        val (exitHandlerIdentityD, actualExit) = computeExits(sections)

        val lookup = RoutingMainInt.buildLookup(map.keys, map)

        original.addHandler(
            if (common != null) {
                RoutingMainIntWithCommon(
                    keyGeneratorInt,
                    lookup,
                    exitDecisionWithDefault = exitHandlerIdentityD,
                    exitDecision = actualExit,
                    common!!,
                    computeRoutingUpdKinds(sections)
                )
            } else {
                RoutingMainInt(
                    keyGeneratorInt,
                    lookup,
                    exitDecisionWithDefault = exitHandlerIdentityD,
                    exitDecision = actualExit,
                    computeRoutingUpdKinds(sections)
                )
            }
        )

        sections.forEachIndexed { index, (_, part) ->
            part.handlers.forEach { original.addHandler(it) }
            if (default != null || sections.lastIndex != index) {
                original.addHandler(EmptyHandler(actualExit, reduceAllowedKinds(part.handlers)))
            }
            part.free()
        }

        default?.handlers?.forEach { original.addHandler(it) }
    }
}

class RoutingMainInt(
    private val keyGeneratorInt: KeyGeneratorInt,
    private val lookup: Lookup,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    override val allowedKinds: Set<UpdKind>?
) : Handler {

    abstract class Lookup {
        abstract operator fun get(key: Int): HandlerIdentity
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = keyGeneratorInt.generate(update, handlerContext)
        if (key == Int.MIN_VALUE) return exitDecision
        val id = lookup[key]
        if (id == HandlerIdentity.emptyID) {
            if (exitDecisionWithDefault == null) {
                error("In RoutingMainInt specified key of `$key` was not found! And default is not specified!")
            } else {
                return exitDecisionWithDefault
            }
        }
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

    private class MapLookup(map: HashMap<Int, HandlerIdentity>) : Lookup() {
        private val map = IntIntHashMap(missingValue = Int.MIN_VALUE).also {
            map.forEach { (i, identity) ->
                it[i] = identity.value
            }
        }

        override fun get(key: Int): HandlerIdentity = HandlerIdentity(map[key])
    }

    private class ArrayLookup(
        private val min: Int,
        private val arr: IntArray
    ) : Lookup() {
        override fun get(key: Int): HandlerIdentity {
            val idxL = key - min
            if (idxL < 0 || idxL >= arr.size) return HandlerIdentity.emptyID
            return HandlerIdentity(arr[idxL])
        }

        companion object {
            fun from(map: HashMap<Int, HandlerIdentity>, min: Int, max: Int): ArrayLookup {
                val sizeL = (max.toLong() - min.toLong() + 1L)
                if (sizeL <= 0L || sizeL > Int.MAX_VALUE.toLong()) {
                    return ArrayLookup(min, intArrayOf())
                }

                val arr = IntArray(sizeL.toInt()) { HandlerIdentity.emptyID.value }
//                val arr = Array(sizeL.toInt()) { HandlerIdentity.emptyID }
                map.forEach { (k, id) ->
                    val idx = (k.toLong() - min.toLong()).toInt()
                    arr[idx] = id.value
                }
                return ArrayLookup(min, arr)
            }
        }
    }
}

class RoutingMainIntWithCommon(
    private val keyGeneratorInt: KeyGeneratorInt,
    private val lookup: RoutingMainInt.Lookup,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    commonHandler: Handler,
    override val allowedKinds: Set<UpdKind>?
) : Handler {

    private val ach = commonHandler.real()
    private val accepted = commonHandler.allowedKinds?.let {
        if (allowedKinds == it) return@let null
        createBoolUpdateKindArray(it)
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = keyGeneratorInt.generate(update, handlerContext)
        if (key == Int.MIN_VALUE) return exitDecision
        val id = lookup[key]
        if (id == HandlerIdentity.emptyID) {
            if (exitDecisionWithDefault == null) {
                error("In RoutingMainInt specified key of `$key` was not found! And default is not specified!")
            } else {
                return exitDecisionWithDefault
            }
        }
        if (accepted == null || accepted[update.ordinal]) {
            val comD = ach.handleUpdate(update, bot, handlerContext)
            if (comD !== Decision.Next) {
                return comD
            }
        }
        return Decision.NextTo(id)
    }
}
