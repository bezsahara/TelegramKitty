package org.bezsahara.kittybot.bot.action.each

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update

// Ignored Decision.Consume for each handler, effectively testing them all
// If at least one of the handlers in the block returned Consumed, once execution reaches the end of the block, following handlers are not tested
// If no handler in the block returns Consumed then handlers following outside the block are tested
inline fun TransparentHandlerStore.testEach(block: ChangingHandlerStore.() -> Unit) {
    val tes = TestEachStrategy(this)
    tes.block()
    tes.build()
}

class TestEachStrategy(val delegate: HandlerStore) : ChangingHandlerStore {
    private val changeKey = delegate.felineDispatcher.identityScope.attrKeyOf<Unit>("change")

    private var updateTypes: HashSet<UpdKind>? = null
    private var utn = true

    override fun addHandler(handler: Handler) {
        if (utn) {
            val kinds = handler.allowedKinds
            // If at least one accepts all kinds, the purpose of this logic goes away
            if (kinds == null) {
                updateTypes = null
                utn = false
            } else {
                var ut = updateTypes
                if (ut == null) {
                    ut = HashSet()
                    updateTypes = ut
                }
                ut.addAll(kinds)
            }
        }
        delegate.addHandler(IgnoreConsumeHandler(handler.real(), changeKey, handler.identity, handler.allowedKinds))
    }

    fun build() {
        delegate.addHandler(updateTypes) { _, _, handlerContext ->
            if (handlerContext[changeKey] != null) {
                Decision.Consumed
            } else {
                Decision.Next
            }
        }
    }

    override val felineDispatcher: FelineDispatcher
        get() = delegate.felineDispatcher
}

class IgnoreConsumeHandler internal constructor(
    private val original: Handler,
    private val changeKey: AttrKey<Unit>,
    override val identity: HandlerIdentity?,
    override val allowedKinds: Set<UpdKind>?
) : Handler {
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val originalDecision = original.handleUpdate(update, bot, handlerContext)
        return if (originalDecision.isConsumed()) {
            handlerContext[changeKey] = Unit
            Decision.Next
        } else originalDecision
    }
}

