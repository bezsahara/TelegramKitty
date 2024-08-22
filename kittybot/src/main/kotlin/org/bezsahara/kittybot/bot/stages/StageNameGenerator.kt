package org.bezsahara.kittybot.bot.stages

object StageNameGenerator {
    private var id = 0

    @JvmStatic
    @Synchronized
    fun createStageName(): Stage {
        id++
        return Stage("/generated$id")
    }
}