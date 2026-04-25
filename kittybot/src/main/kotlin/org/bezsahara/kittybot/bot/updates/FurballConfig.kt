package org.bezsahara.kittybot.bot.updates

data class FurballConfig(
    val hopSafetyTimes: Int = 3,
    val attrsLimit: Int = 1000,
    val onRecursionProblem: ((Int) -> Unit)? = null,
    val ignoreIdentityDuplicated: Boolean = false,
    val multiUpdaterUseMap: Boolean = false,
    val multiUpdaterMapLimit: Int = 50_000
) {
    companion object {
        val Default = FurballConfig()
    }
}
