package org.bezsahara.kittybot.bot.updates

data class FurballConfig(
    val hopSafetyTimes: Int,
    val attrsLimit: Int,
    val onRecursionProblem: ((Int) -> Unit)? = null,
    val ignoreIdentityDuplicated: Boolean = false,
) {
    companion object {
        val Default = FurballConfig(3, 1000)
    }
}