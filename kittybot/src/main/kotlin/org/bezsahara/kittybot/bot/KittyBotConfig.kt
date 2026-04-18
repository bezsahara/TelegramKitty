package org.bezsahara.kittybot.bot


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.builder.*
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.TypeAwareMap
import org.bezsahara.kittybot.bot.dispatchers.createTypeAwareKey
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.json.jsonInstance
import org.bezsahara.kittybot.bot.updates.*
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.classes.core.update.Update


class KittyBotConfig<T : UpdateReceiver>(
    felineDispatcher: FelineDispatcher,
    updaterMode: UpdaterMode,
    val updateOrigin: UpdateOrigin,
    pollingTimeout: Long,
    preActions: List<FelineBuilder.PreAction>,
    botApiServerConfig: BotApiServerConfig,
    lastIdRecovery: RecoverLastId?,
    val errorHandler: HandlerErrorHandler,
    private val apiClientBuilder: ClientBuilder,
    val allowedUpdates: List<String>?,
    furballConfig: FurballConfig,
    val botContext: TypeAwareMap
) {
    init {
        botContext.kittyBotConfig = this
    }
    val json get() = jsonInstance

    @JvmField
    val updatesChannel =
        Channel<Update>(1024)

    private val tApiClient = apiClientBuilder.build(botApiServerConfig, json)//

    internal val updateReceiver = when (updateOrigin) {
        UpdateOrigin.Polling -> PollingReceiver(tApiClient, pollingTimeout, lastIdRecovery, allowedUpdates)
        UpdateOrigin.Webhook -> null
    }

    val supervisorJob = felineDispatcher.felineBuilder.supervisorJob
    internal val scope = CoroutineScope(Dispatchers.IO + supervisorJob)

    internal val updater: Furball = when (updaterMode) {
        is UpdaterMode.SingleThread -> SingleUpdater(
            tApiClient,
            felineDispatcher,
            updatesChannel,
            scope,
            errorHandler,
            furballConfig
        )

        is UpdaterMode.MultiThread -> MultiUpdater(
            tApiClient,
            felineDispatcher,
            updatesChannel,
            scope,
            updaterMode.multiIdentity,
            updaterMode.parallelism,
            errorHandler,
            furballConfig
        )

        is UpdaterMode.Custom -> CustomUpdater(
            tApiClient,
            updaterMode.customUpdater,
            felineDispatcher,
            updatesChannel,
            supervisorJob,
            errorHandler,
            furballConfig
        )
    }

    @JvmField
    val kittyBot: KittyBot = updater.bot

    private var closed = false

    @Synchronized
    fun close() {
        if (closed) return
        updateReceiver?.close()
        apiClientBuilder.close()
        closed = true
    }

    init {
        runBlocking(Dispatchers.IO) {
            preActions.forEach {
                it.execute(kittyBot)
            }
        }
        supervisorJob.invokeOnCompletion { close() }
    }

    companion object {
        @JvmField
        internal val BOT_SUPERVISOR_JOB = createTypeAwareKey<CompletableJob>("BotSupervisorJob")
    }
}

fun KittyBotConfig<PollingReceiver>.startPolling(wait: Boolean = true): Job {
    if (updateReceiver !is PollingReceiver) {
        hiss("To start polling, you need to set updateOrigin to UpdateOrigin.Polling")
    }
    val pollingJob = CoroutineScope(Dispatchers.IO + supervisorJob).launch {
        updateReceiver.receiveUpdates(updatesChannel)
    }
    updater.start()
    if (wait) {
        runBlocking {
            pollingJob.join()
        }
    }
    return pollingJob
}

fun KittyBotConfig<PollingReceiver>.stopPolling() {
    supervisorJob.cancel()
}

fun KittyBotConfig<WebhookReceiver>.stop() {
    supervisorJob.cancel()
}

fun KittyBotConfig<WebhookReceiver>.start() {
    updater.start()
}

/**
 * Send updates from webhook via a channel.
 */
suspend inline fun KittyBotConfig<WebhookReceiver>.onUpdate(data: String) {
    updatesChannel.send(
        json.decodeFromString(Update.serializer(), data)
    )
}
