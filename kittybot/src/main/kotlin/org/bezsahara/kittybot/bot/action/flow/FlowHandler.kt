package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface FlowIdentityFinder {
    fun findOrNull(update: Update): String?
}

class FlowIdentityData(
    val identity: String,
    val payload: FlowPayload,
) {
    val args: Any? get() = payload.args
}

class FlowHandlerBegin(
    sections: Array<HandlerIdentity>,
    val flowIdentityFinder: FlowIdentityFinder,
    val flowIdentityStorage: FlowIdentityStorage,
    exitHandlerIdentity: HandlerIdentity,
    val fidAttribute: AttrKey<FlowIdentityData>,
) : Handler {

    private val exitDecision = Decision.AfterNextTo(exitHandlerIdentity)

    private val sections = Array(sections.size) { Decision.NextTo(sections[it]) }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val identity = flowIdentityFinder.findOrNull(update) ?: return exitDecision
        var stage = flowIdentityStorage[identity]

        if (stage == null) {
            stage = FlowPayload(0, null)
        }

        handlerContext[fidAttribute] = FlowIdentityData(identity, stage)

        val stageIdMinusOne = stage.id - 1

        if (stageIdMinusOne < 0) return Decision.Next

        if (stageIdMinusOne >= sections.size) error("Trying to jump to a section that does not exist! Check, maybe you called nextSection on the last section!")
        return sections[stageIdMinusOne]
    }
}

class FlowHandlerSectionEnd(
    exitHandlerIdentity: HandlerIdentity,
) : Handler {
    private val exitDecision = Decision.AfterNextTo(exitHandlerIdentity)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        return exitDecision
    }
}