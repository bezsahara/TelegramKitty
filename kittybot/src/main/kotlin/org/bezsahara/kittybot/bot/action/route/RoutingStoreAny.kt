package org.bezsahara.kittybot.bot.action.route

import io.ktor.util.Hash
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.EmptyHandler
import org.bezsahara.kittybot.bot.action.other.createBoolUpdateKindArray
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update


class RoutingStrategyAny<T>(
    val keyGeneratorAny: KeyGeneratorAny<T>,
    original: HandlerStore,
) : RoutingStrategy<T>(original) {
    @Deprecated("Use main constructor")
    constructor(
        keyGeneratorAny: KeyGeneratorAny<T>,
        original: HandlerStore,
        ofKinds: Set<UpdKind>?,
    ) : this(keyGeneratorAny, original)

    inline fun section(key: T, block: TransparentHandlerStore.() -> Unit) {
        val r = addOrGetSection(key)
        r.block()
    }

    var map: MutableMap<T, HandlerIdentity>? = null

    fun build() {
        val sections = computeSections()
        if (sections.isEmpty()) return

        val map = map ?: hashMapOf()
        sections.forEach { (key, part) ->
            if (!part.isEmpty()) {
                val id = part.handlers[0].identity!!
                val prev = map.put(key, id)
                if (prev != null) {
                    error("Duplicate routing key detected: `$key`")
                }
            }
        }

        val (exitHandlerIdentityD, actualExit) = computeExits(sections)

        val finalMap = if (map.isJavaHashMap() && map.size < 1024) {
            java.util.Map.copyOf(map)
        } else map
        original.addHandler(
            if (common != null) {
                RoutingMainAnyWithCommon(
                    keyGeneratorAny,
                    finalMap,
                    exitDecisionWithDefault = exitHandlerIdentityD,
                    exitDecision = actualExit,
                    common!!,
                    computeRoutingUpdKinds(sections)
                )
            } else {
                RoutingMainAny(
                    keyGeneratorAny,
                    finalMap,
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
        }

        default?.handlers?.forEach { original.addHandler(it) }

        free()
    }
}

private fun Map<*, *>.isJavaHashMap() = javaClass === java.util.HashMap::class.java || javaClass === java.util.LinkedHashMap::class.java

class RoutingMainAny(
    private val keyGeneratorAny: KeyGeneratorAny<*>,
    private val map: Map<*, HandlerIdentity>,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    override val allowedKinds: Set<UpdKind>?,
) : Handler {
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val key = keyGeneratorAny.generate(update, handlerContext) ?: return exitDecision
        val id = map[key] ?: if (exitDecisionWithDefault == null) {
            error("In RoutingMainAny specified key of `$key` was not found! And default is not specified!")
        } else {
            return exitDecisionWithDefault
        }
        return Decision.NextTo(id)
    }
}

class RoutingMainAnyWithCommon(
    private val keyGeneratorAny: KeyGeneratorAny<*>,
    private val map: Map<*, HandlerIdentity>,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    commonHandler: Handler,
    override val allowedKinds: Set<UpdKind>?,
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
        val key = keyGeneratorAny.generate(update, handlerContext) ?: return exitDecision
        val id = map[key] ?: if (exitDecisionWithDefault == null) {
            error("In RoutingMainAny specified key of `$key` was not found! And default is not specified!")
        } else {
            return exitDecisionWithDefault
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