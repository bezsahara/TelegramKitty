@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.StageChainScope
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.stages.Stage
import org.bezsahara.kittybot.bot.stages.StageInfo
import org.bezsahara.kittybot.bot.stages.StageManagerProvider
import org.bezsahara.kittybot.bot.stages.extractPath
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.updates.Update

class StageChainHandler(
    private val stage: Stage,
    private val check: ((Message) -> Boolean)? = null,
    private var chainStageList: Pair<List<Stage>, Int>? = null,
    private val onSuccess: suspend StageChainScope.() -> Unit
) : Handler {
    private val stageManager = StageManagerProvider.stageManager ?: hiss("You did not select StageManager")

    override fun checkUpdate(update: Update): Boolean {
        if (update !is MessageUpdate) {
            return false
        }
        val chatId = update.message.chat.id
        return if (stageManager.getStage(chatId)?.let { extractPath(it) == stage } == true) {
            check?.invoke(update.message) ?: return true
        } else {
            false
        }
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        update as MessageUpdate
        StageChainScope(
            bot,
            update.message,
            StageInfo.fromStage(stageManager.getStage(update.message.chat.id)!!),
            stageManager,
            chainStageList
        ).apply {
            onSuccess()
            chainStageList?.let {
                if (!exit(it)) {
                    chainStageList = null
                }
            }
        }
    }
}

// used as a chain origin of stage handlers
class EitherStageTextHandler(
    private val stage: Stage,
    private val check: (Message) -> Boolean,
    private var chainStageList: Pair<List<Stage>, Int>? = null,
    private val onSuccess: suspend StageChainScope.() -> Unit
) : Handler {
    val stageManager = StageManagerProvider.stageManager
        ?: hiss("You did not select StageManager. Use setStageManager(InMemoryStageManager()) in dispatchers scope")

    override fun checkUpdate(update: Update): Boolean {
        if (update !is MessageUpdate) {
            return false
        }

        val chatId = update.message.chat.id
        // Here we make sure that we won't interrupt other stages
        val uStage = stageManager.getStage(chatId)
        return if (uStage != null) {
            extractPath(uStage) == stage
        } else {
            check(update.message)
        }
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        StageChainScope(
            bot,
            update.message!!,
            StageInfo.none,
            stageManager,
            chainStageList
        ).apply {
            onSuccess()
            chainStageList?.let {
                if (!exit(it)) {
                    chainStageList = null
                }
            }
        }
    }
}