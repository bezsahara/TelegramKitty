package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.EmptyHandler
import org.bezsahara.kittybot.bot.action.other.createBoolUpdateKindArray
import org.bezsahara.kittybot.bot.action.other.replaceLast
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.ensureHasIdentity
import org.bezsahara.kittybot.bot.dispatchers.real
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import kotlin.collections.forEachIndexed



class RoutingStrategyAny<T>(
    val keyGeneratorAny: KeyGeneratorAny<T>,
    original: HandlerStore,
    val ofKinds: Set<UpdateKind<*>>?
) : RoutingStrategy<T>(original) {

    inline fun section(key: T, block: HandlerStore.() -> Unit) {
        val r = RoutingPart(original)
        r.block()
        if (r.isEmpty()) return
        addSection(key to r)
    }

    fun build() {
        if (sections.isEmpty()) return

        val map = HashMap<T, HandlerIdentity>(sections.size * 2)
        sections.forEach { (key, part) ->
            val id = part.handlers[0].identity!!
            val prev = map.put(key, id)
            if (prev != null) {
                error("Duplicate routing key detected: `$key`")
            }
        }

        val (exitHandlerIdentityD, actualExit) = computeExits()

        original.addHandler(
            if (common != null) {
                RoutingMainAnyWithCommon(keyGeneratorAny, map, exitDecisionWithDefault = exitHandlerIdentityD, exitDecision = actualExit, common!!, ofKinds)
            } else {
                RoutingMainAny(keyGeneratorAny, map, exitDecisionWithDefault = exitHandlerIdentityD, exitDecision = actualExit, ofKinds)
            }
        )

        sections.forEachIndexed { index, (_, part) ->
            part.handlers.forEach { original.addHandler(it) }
            if (default != null || sections.lastIndex != index) {
                original.addHandler(EmptyHandler(actualExit))
            }
        }

        default?.handlers?.forEach { original.addHandler(it) }
    }
}



class RoutingMainAny(
    private val keyGeneratorAny: KeyGeneratorAny<*>,
    private val map: HashMap<*, HandlerIdentity>,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    override val allowedKinds: Set<UpdateKind<*>>?
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
    private val map: HashMap<*, HandlerIdentity>,
    private val exitDecisionWithDefault: Decision?,
    private val exitDecision: Decision,
    commonHandler: Handler,
    override val allowedKinds: Set<UpdateKind<*>>?
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