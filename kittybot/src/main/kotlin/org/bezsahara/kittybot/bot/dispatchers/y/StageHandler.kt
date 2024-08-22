@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.StageScope
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.stages.Stage
import org.bezsahara.kittybot.bot.stages.StageInfo
import org.bezsahara.kittybot.bot.stages.StageManagerProvider
import org.bezsahara.kittybot.bot.stages.extractPath
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.updates.Update


class StageHandler(
    val stage: Stage,
    private val check: ((Message) -> Boolean)?,
    val onSuccess: suspend StageScope.() -> Unit
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
        StageScope(
            update.message,
            bot,
            StageInfo.fromStage(stageManager.getStage(update.message.chat.id)!!),
            stageManager
        ).onSuccess()
    }
}