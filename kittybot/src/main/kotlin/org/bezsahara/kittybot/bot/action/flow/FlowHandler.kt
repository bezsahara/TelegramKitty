package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.action.route.KeyGeneratorInt
import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface FlowIdentityFinder {
    fun findOrNull(update: Update, handlerContext: HandlerContext): String?

    companion object {
        val OfMessageUpdateChatId = FlowIdentityFinder { update, _ -> update.message?.chat?.id?.toString() }
    }
}

class FlowIdentityData<T>(
    val identity: String,
    val payload: FlowPayload<T>,
) {
    val args: T? get() = payload.args
}

internal class FlowRoutingKeyGenerator<T>(
    private val flowIdentityFinder: FlowIdentityFinder,
    private val flowIdentityStorage: FlowIdentityStorage<T>,
    private val fidAttribute: AttrKey<FlowIdentityData<T>>,
    private val sectionCount: Int,
) : KeyGeneratorInt {
    override fun generate(update: Update, handlerContext: HandlerContext): Int {
        val identity = flowIdentityFinder.findOrNull(update, handlerContext) ?: return Int.MIN_VALUE
        var stage = flowIdentityStorage[identity]

        if (stage == null) {
            stage = FlowPayload(0, null)
        }

        handlerContext[fidAttribute] = FlowIdentityData(identity, stage)

        val stageId = stage.id

        if (stageId < 0) {
            error("Flow stage id cannot be negative!")
        }
        if (stageId >= sectionCount) {
            error("Trying to jump to a section that does not exist! Check, maybe you called nextSection on the last section!")
        }

        return stageId
    }
}
