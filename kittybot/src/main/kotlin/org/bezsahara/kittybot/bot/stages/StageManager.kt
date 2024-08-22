package org.bezsahara.kittybot.bot.stages

/**
 * Manages assignment of stages to users. You can implement it yourself or use [InMemoryStageManager] impl.
 */
interface StageManager {
    fun getStage(chatId: Long): Stage?

    fun setStage(chatId: Long, stage: Stage)

    fun clearStage(chatId: Long)
}