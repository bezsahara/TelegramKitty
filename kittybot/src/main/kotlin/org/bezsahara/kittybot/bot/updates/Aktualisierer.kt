package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.CoroutineScope
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.telegram.classes.updates.Update

abstract class Aktualisierer(
    val bot: KittyBot,
    val coroutineScope: CoroutineScope,
    private val botDispatchers: FelineDispatcher,
    val updateReceiver: UpdateReceiver?
) {
    private val filterHandlerList = botDispatchers.filterHandlerList.toTypedArray()
    private val handlerList = botDispatchers.handlerList.toTypedArray()
    private val fhlSize = botDispatchers.filterHandlerList.size
    private val hlSize = botDispatchers.handlerList.size

    suspend fun applyHandlers(update: Update) {
        for (filterIndex in 0..<fhlSize) {
            val filter = filterHandlerList[filterIndex]
            if (filter.applicable(update)) {
                if (filter.process(update, bot)) {
                    return
                }
            }
        }

        for (handlerIndex in 0..<hlSize) {
            val handler = handlerList[handlerIndex]
            if (handler.checkUpdate(update)) {
                try {
                    handler.handleUpdate(update, bot)
                } catch (e: Exception) {
                    botDispatchers.errorHandler?.handleError(e, bot, update) ?: throw e
                }
                return
            }
        }
    }

    abstract fun start()

    abstract fun stop()
}