package org.bezsahara.kittybot.bot.stages.builder

import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.EitherStageTextHandler
import org.bezsahara.kittybot.bot.dispatchers.y.StageChainHandler
import org.bezsahara.kittybot.bot.dispatchers.y.StageHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.StageChainScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.StageScope
import org.bezsahara.kittybot.bot.stages.Stage
import org.bezsahara.kittybot.bot.stages.StageNameGenerator
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message

/**
 * Handles a stage with an optional message check and success callback.
 * Adds a `StageChainHandler` to manage the incoming messages for the specified stage.
 *
 * @param stage The stage to handle.
 * @param check An optional function to check messages. It returns true if the message is valid for this handler.
 * @param onSuccess A suspend function to be executed when the message is successfully handled.
 */
fun FelineDispatcher.stageHandler(
    stage: Stage,
    check: ((Message) -> Boolean)? = null,
    onSuccess: suspend StageScope.() -> Unit
) {
    addHandler(
        StageHandler(
            stage,
            check,
            onSuccess
        )
    )
}

/**
 * Starts handling a stage with an optional message check and success callback.
 * Adds a `StageChainHandler` and returns a `StageChain` to manage the flow of stages.
 *
 * @param stage The initial stage to handle.
 * @param check An optional function to check messages. It returns true if the message is valid for this handler.
 * @param onSuccess A suspend function to be executed when the message is successfully handled.
 * @return The `StageChain` to manage the flow of stages.
 */
fun FelineDispatcher.startStageHandler(
    stage: Stage,
    check: ((Message) -> Boolean)? = null,
    onSuccess: suspend StageChainScope.() -> Unit
): StageChain {
    val ch = StageChain(stage)
    addHandler(
        StageChainHandler(
            stage,
            check,
            ch.getListAndIndex(),
            onSuccess
        )
    )
    return ch
}

/**
 * Starts handling a stage with a message check and success callback.
 * Generates a new `Stage`, adds an `EitherStageTextHandler`, and returns a `StageChain`.
 *
 * @param check A function to check messages. It returns true if the message is valid for this handler.
 * @param onSuccess A suspend function to be executed when the message is successfully handled.
 * @return The `StageChain` to manage the flow of stages.
 */
fun FelineDispatcher.startStageHandler(
    check: (Message) -> Boolean,
    onSuccess: suspend StageChainScope.() -> Unit
): StageChain {
    val stage = StageNameGenerator.createStageName()
    val ch = StageChain(stage)
    ch.felineDispatcher = this
    addHandler(
        EitherStageTextHandler(
            stage,
            check,
            ch.getListAndIndex(),
            onSuccess
        )
    )
    return ch
}

/**
 * Adds a handler for the next stage in the chain with an optional message check and success callback.
 * Updates the current stage and returns the updated `StageChain`.
 *
 * @param stage An optional next stage. If null, the next stage is added automatically.
 * @param check An optional function to check messages. It returns true if the message is valid for this handler.
 * @param onSuccess A suspend function to be executed when the message is successfully handled.
 * @return The updated `StageChain`.
 */
fun StageChain.thenStageHandler(
    stage: Stage? = null,
    check: ((Message) -> Boolean)? = null,
    onSuccess: suspend StageChainScope.() -> Unit
): StageChain {
    if (stage != null) {
        addStage(stage)
    } else {
        addStage()
    }
    felineDispatcher!!.addHandler(
        StageChainHandler(
            currentStage,
            check,
            getListAndIndex(),
            onSuccess
        )
    )
    return this
}

