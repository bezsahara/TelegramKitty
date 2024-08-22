package org.bezsahara.kittybot.bot.stages.builder

import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.stages.Stage

class StageChain internal constructor(
    private val baseStage: Stage,
) {
    var felineDispatcher: FelineDispatcher? = null
    internal val stages: MutableList<Stage> = mutableListOf(baseStage)
    internal var currentStage: Stage = baseStage

    private var step = 0
    private var currentIndex = 0

    private val chain = "chain"

    internal fun getListAndIndex(): Pair<List<Stage>, Int> {
        return stages to currentIndex
    }

    internal fun addStage(stage: Stage? = null) {
        if (stage != null) {
            if (stage in stages) {
                hiss("Adding same named stages into one chain is useless")
            }
            stages.add(stage)
            currentIndex++
        } else {
            while (true) {
                val newStage = Stage("${baseStage.value}/$chain$step")
                if (newStage in stages) {
                    step++
                } else {
                    stages.add(newStage)
                    currentStage = newStage
                    currentIndex++
                    break
                }
            }
        }
    }
}