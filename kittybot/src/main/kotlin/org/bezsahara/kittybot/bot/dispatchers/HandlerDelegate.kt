package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

// Can be used to change Handler arguments without putting handler into another handler
// It overrides Handler preferences completely!
class HandlerDelegate(
    override val identity: HandlerIdentity?,
    override val allowedKinds: Set<UpdateKind<*>>?,
    originalHandler: Handler,
) : Handler {
    init {
        require(originalHandler !is RejectDelegate) { "Handler is RejectDelegate. HandlerDelegate cannot accept it" }
    }

    val originalHandler: Handler =
        if (originalHandler is HandlerDelegate) originalHandler.originalHandler else originalHandler

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        error("This is HandlerDelegate. If you see this error, please report this!")
    }

    override fun toString(): String {
        return "HandlerDelegate[identity=$identity, allowedKinds=$allowedKinds](${originalHandler.toStr()})"
    }
}

fun Handler.ensureHasIdentity(): Handler {
    if (identity != null) return this
    return HandlerDelegate(HandlerIdentity.createNew(), allowedKinds, this)
}

fun Handler.asDelegate(identity: HandlerIdentity?, allowedKinds: Set<UpdateKind<*>>?): HandlerDelegate {
    return HandlerDelegate(identity, allowedKinds, this)
}

fun Handler.copyAsDelegate(
    identity: HandlerIdentity? = this.identity,
    allowedKinds: Set<UpdateKind<*>>? = this.allowedKinds,
): HandlerDelegate {
    return HandlerDelegate(identity, allowedKinds, this)
}

fun Handler.real(): Handler {
    return if (this is HandlerDelegate) originalHandler else this
}

// Useful if u want to create handler as lambda but also want to specify identity/allowedTypes
// In kotlin 2.3.10, compiler sometimes emits wrong bytecode for this function in certain situations.
// So an error might be expected.
fun HandlerStore.addHandler(
    allowedTypes: Set<UpdateKind<*>>? = null,
    identity: HandlerIdentity? = null,
    handler: Handler,
) {
    addHandler(HandlerDelegate(identity, allowedTypes, handler))
}


interface RejectDelegate


fun Handler.toStr(): String =
    if (this is HandlerDelegate) toString() else "Handler(identity=$identity, allowedKinds=$allowedKinds)"