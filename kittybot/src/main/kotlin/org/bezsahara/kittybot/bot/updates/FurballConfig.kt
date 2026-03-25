package org.bezsahara.kittybot.bot.updates

data class FurballConfig(
    val hopSafetyTimes: Int,
    val attrsLimit: Int,
    val onRecursionProblem: (() -> Unit)? = null,
) {
    companion object {
        val Default = FurballConfig(3, 1000)
    }
}