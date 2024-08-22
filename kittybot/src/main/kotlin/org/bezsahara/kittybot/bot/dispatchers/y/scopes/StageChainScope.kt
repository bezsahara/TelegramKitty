package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.stages.Stage
import org.bezsahara.kittybot.bot.stages.StageInfo
import org.bezsahara.kittybot.bot.stages.StageManager
import org.bezsahara.kittybot.bot.stages.withArguments
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message

class StageChainScope(
    bot: KittyBot,
    message: Message,
    val stageInfo: StageInfo,
    private val stageManager: StageManager,
    private val stageChainList: Pair<List<Stage>, Int>?
) : MessageScope(message, bot) {
    private var exitAllowed = true
    private val args = mutableListOf<String>()

    /** The next stage will NOT be set at the end of this scope, and the user will be on the same stage.
     *  It is useful if a user entered wrong data, and you are asking them to send it again to the same handler.
     */
    fun cannotProceed() {
        exitAllowed = false
    }

    /** If called stage will be automatically set at the end of this scope.
     *  You can also add arguments for the next handler.
     */
    fun canProceed(withArguments: List<String>) {
        exitAllowed = true
        args.addAll(withArguments)
    }

    fun canProceed() {
        exitAllowed = true
    }

    /** Set any next stage for the user, automatic set stage won't be called if this function is used */
    fun setNextStage(nextStage: Stage) {
        exitAllowed = false
        stageManager.setStage(message.chat.id, nextStage)
    }

    /** Moves user back to the beginning of the chain or flow, idk how to call it. */
    fun resetTheFlow() {
        val flowList = stageChainList?.first
        if (flowList.isNullOrEmpty()) {
            hiss("You can't call it here because the handler was not configured as part of a chain or chain is empty")
        }
        exitAllowed = false
        stageManager.setStage(message.chat.id, flowList[0])
    }

    /**
     * Clears user stage and exits the chain.
     */
    fun exitTheChain() {
        exitAllowed = false
        stageManager.clearStage(message.chat.id)
    }

    // Return false if supposedNextStage needs to be set to null and this function won't be called, aka optimization.
    // TODO Make sure user is included
    internal fun exit(supposedNextStage: Pair<List<Stage>, Int>): Boolean {
        if (exitAllowed) {
            val tryNextStage = supposedNextStage.run {
                first.getOrNull(second + 1)
            } ?: return false
            stageManager.setStage(
                message.chat.id,
                if (args.isEmpty()) {
                    tryNextStage
                } else {
                    tryNextStage.withArguments(args)
                }
            )
            return true
        }
        return true
    }
}