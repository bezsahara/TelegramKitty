@file:Suppress("NOTHING_TO_INLINE")

package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.attrKeyOf
import org.bezsahara.kittybot.bot.dispatchers.real
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update

inline fun TransparentHandlerStore.allowDynamicDecisions(block: DynamicDecisions.() -> Unit) {
    DynamicDecisions(this).also(block)
}

class DynamicDecisions(val original: HandlerStore) : TransparentHandlerStore {
    val decisionKey = original.attrKeyOf<Decision>("DecisionKey")

    inline fun HandlerContext.returnDecisionOf(decision: Decision) {
        set(decisionKey, decision)
    }

    override fun addHandler(handler: Handler) {
        addHandler(DDInterceptor(handler, decisionKey))
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}


class DDInterceptor(
    original: Handler,
    private val decAttr: AttrKey<Decision>
) : Handler {
    override val identity: HandlerIdentity? = original.identity
    override val allowedKinds: Set<UpdKind>? = original.allowedKinds

    private val realHandler = original.real()

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val res = realHandler.handleUpdate(update, bot, handlerContext)
        if (res === Decision.Consumed) {
            val d = handlerContext.remove(decAttr) ?: return Decision.Consumed
            return d
        }
        return res
    }
}