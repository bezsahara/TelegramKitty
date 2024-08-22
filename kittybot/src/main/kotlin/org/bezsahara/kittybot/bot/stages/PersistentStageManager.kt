package org.bezsahara.kittybot.bot.stages

import org.bezsahara.kittybot.bot.errors.hiss
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Similar to [InMemoryStageManager] but also saves stages to a file, so it can recover them when program restarts.
 */
class PersistentStageManager(
    private val saveTo: File
) : StageManager {
    private val map = ConcurrentHashMap<Long, Stage>()

    init {
        if (!saveTo.exists()) {
            saveTo.createNewFile()
        }
        if (!(saveTo.canWrite() && saveTo.canRead())) {
            hiss("File cannot be written to or read.")
        }

        //Restoring the map
        saveTo.bufferedReader(Charsets.UTF_8).use { file ->
            file.forEachLine { line ->
                if (line.isNotBlank()) {
                    val (id, stage) = line.split(":", limit = 2)
                    map[id.toLong()] = Stage(stage)
                }
            }
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            saveTo.bufferedWriter(Charsets.UTF_8).use { file ->
                map.forEach { (id, stage) ->
                    file.append(id.toString())
                    file.append(':')
                    file.append(stage.value)
                    file.append('\n')
                }
            }
        })
    }

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