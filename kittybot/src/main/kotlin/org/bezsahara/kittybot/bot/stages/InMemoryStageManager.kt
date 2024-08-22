package org.bezsahara.kittybot.bot.stages

import java.util.concurrent.ConcurrentHashMap

/**
 * Manages stages with [ConcurrentHashMap]. Stages are not saved after the application shut down.
 */
class InMemoryStageManager : StageManager {
    private val map = ConcurrentHashMap<Long, Stage>()
    override fun getStage(chatId: Long): Stage? {
        return map[chatId]
    }

    override fun setStage(chatId: Long, stage: Stage) {
        map[chatId] = stage
    }

    override fun clearStage(chatId: Long) {
        map.remove(chatId)
    }
}