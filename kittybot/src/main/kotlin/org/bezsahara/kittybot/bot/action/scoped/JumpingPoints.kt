package org.bezsahara.kittybot.bot.action.scoped

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

fun <T> TransparentHandlerStore.centralJumpPoint(
    updateKinds: Set<UpdateKind<*>>? = null,
    resolve: (update: Update, handlerContext: HandlerContext) -> T,
): JumpingPoints<T> {
    val jp = JumpingPoints<T>(updateKinds, this)
    jp.central(resolve)
    return jp
}

class JumpingPoints<T>(
    val ofKinds: Set<UpdateKind<*>>?,
    private val original: HandlerStore,
) {
    private val resolution = hashMapOf<T, Decision>()

    fun central(resolve: (update: Update, handlerContext: HandlerContext) -> T) {
        original.addHandler(ofKinds) { update, _, handlerContext ->
            val key = resolve(update, handlerContext)
            resolution[key] ?: error("Couldn't find handler for $key")
        }
    }

    fun jumpPoint(name: T) {
        val jp = JumpPoint()
        resolution[name] = Decision.AfterNextTo(jp.identity)
        original.addHandler(jp)
    }
}

class JumpPoint() : Handler {
    override val allowedKinds: Set<UpdateKind<*>> get() = emptySet()

    override val identity: HandlerIdentity = HandlerIdentity.createNew()

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        return Decision.Next
    }
}