package org.bezsahara.kittybot.bot.updates

import kotlin.math.max

data class FurballConfig(
    val hopSafetyTimes: Int = 3,
    val attrsLimit: Int = 1000,
    val onRecursionProblem: ((Int) -> Unit)? = null,
    val ignoreIdentityDuplicated: Boolean = false,
    val multiUpdaterUseMap: Boolean = false,
    val multiUpdaterMapLimit: Int = 50_000,
    val httpTimeout: (pollingTimeout: Long) -> Long = { max(it + 5, 10) },
) {
    companion object {
        val Default = FurballConfig()
    }
}
