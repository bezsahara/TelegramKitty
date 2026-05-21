package org.bezsahara.kittybot.bot.updates.updaters

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.updates.furballs.Furball
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface CustomUpdaterSetup {
    // this function is called once, return immediately.
    // Attach supervisorJob to your CoroutineContext.
    fun configure(channel: Channel<Update>, furball: Furball, supervisorJob: CompletableJob)
}

internal class CustomUpdater(
    private val customUpdater: CustomUpdaterSetup,
    val channel: Channel<Update>,
    private val supervisorJob: CompletableJob,
    private val furball: Furball
) : Updater {
    override fun start() {
        customUpdater.configure(channel, furball, supervisorJob)
    }
}