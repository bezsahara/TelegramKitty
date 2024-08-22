package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.stages.InMemoryStageManager
import org.bezsahara.kittybot.bot.stages.StageManager
import org.bezsahara.kittybot.bot.stages.StageManagerProvider


class FelineDispatcher internal constructor() {
    @JvmField
    internal val handlerList = mutableListOf<Handler>()
    @JvmField
    internal val filterHandlerList = mutableListOf<FilterHandler>()
    @JvmField
    internal var errorHandler: ErrorHandler? = null
    private val filterBuilder = Filters(filterHandlerList)

    class Filters(
        private val filterHandlerList: MutableList<FilterHandler>
    ) {
        fun addFilterAtIndex(filterHandler: FilterHandler, index: Int = 0) {
            filterHandlerList.add(index, filterHandler)
        }

        fun addFilter(filterHandler: FilterHandler) {
            filterHandlerList.add(filterHandler)
        }
    }

    /**
     * Sets up filters. Filters will be executed before the handlers.
     */
    fun filters(block: Filters.() -> Unit) {
        filterBuilder.apply(block)
    }

    /**
     * Sets a [StageManager] for use by stage handlers.
     * @param stageManager if set to null, a default impl will be used - InMemoryStageManager
     */
    fun useStageManager(stageManager: StageManager? = null) {
        StageManagerProvider.stageManager = stageManager ?: InMemoryStageManager()
    }

    /**
     * Stage manager can be used outside the scope of stage handlers.
     */
    val stageManager: StageManager
        get() = StageManagerProvider.stageManager ?: hiss("StageManager is not set! Use setStageManager")

    fun addHandler(handler: Handler) {
        handlerList.add(handler)
    }

    fun setErrorHandler(errorHandler: ErrorHandler) {
        this.errorHandler = errorHandler
    }
}