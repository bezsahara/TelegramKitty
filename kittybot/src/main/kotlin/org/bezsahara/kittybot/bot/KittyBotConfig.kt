package org.bezsahara.kittybot.bot


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.builder.*
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.TypeAwareMap
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.json.jsonInstance
import org.bezsahara.kittybot.bot.updates.FurballConfig
import org.bezsahara.kittybot.bot.updates.furballs.*
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.PollingRecovery
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.bot.updates.updaters.CustomUpdater
import org.bezsahara.kittybot.bot.updates.updaters.MultiUpdater
import org.bezsahara.kittybot.bot.updates.updaters.SingleUpdater
import org.bezsahara.kittybot.bot.updates.updaters.Updater
import org.bezsahara.kittybot.other.FDC
import org.bezsahara.kittybot.telegram.classes.core.update.Update


data class KittyBotResult(
    val identityScope: IdentityScope,
    val handlerList: List<Handler>,
    val updateOrigin: UpdateOrigin,
    val pollingTimeout: Long,
    val preActions: List<suspend KittyBot.() -> Unit>,
    val botApiServerConfig: BotApiServerConfig,
    val lastIdRecovery: RecoverLastId?,
    val furballConfig: FurballConfig,
    val apiClientBuilder: ClientBuilder,
    val errorHandler: HandlerErrorHandler,
    val botContext: TypeAwareMap,
    val allowedUpdates: List<String>?,
    val supervisorJob: CompletableJob,
    val updaterMode: UpdaterMode,
    val visitor: UpdateVisitor?,
)

class KittyBotConfig<T : UpdateReceiver>(
    result: KittyBotResult,
) {
    val updateOrigin: UpdateOrigin = result.updateOrigin
    val errorHandler: HandlerErrorHandler = result.errorHandler
    val botContext: TypeAwareMap = result.botContext
    val allowedUpdates: List<String>? = result.allowedUpdates
    val supervisorJob = result.supervisorJob
    internal val scope = CoroutineScope(Dispatchers.IO + supervisorJob)


    init {
        botContext.kittyBotConfig = this
    }

    val json get() = jsonInstance

    @JvmField
    val updatesChannel =
        Channel<Update>(1024)

    private val apiClientBuilder: ClientBuilder = result.apiClientBuilder

    @JvmField
    val kittyBot: KittyBot = apiClientBuilder.build(result.botApiServerConfig, json)//


    internal val updateReceiver = when (updateOrigin) {
        UpdateOrigin.Polling -> PollingReceiver(
            kittyBot,
            result.pollingTimeout,
            result.lastIdRecovery,
            allowedUpdates
        )

        UpdateOrigin.Webhook -> null
    }

    internal val furball: Furball = if (result.visitor != null) FurballVisitor(kittyBot, result.visitor)
    else if (result.furballConfig.useFurballContVariant) FDC(
        kittyBot,
        updatesChannel,
        result
    ) else FurballDispatchers(kittyBot, updatesChannel, result)

    internal val updater: Updater = when (result.updaterMode) {
        is UpdaterMode.SingleThread -> SingleUpdater(
            updatesChannel, scope, furball
        )

        is UpdaterMode.MultiThread -> MultiUpdater(
            updatesChannel,
            scope,
            result.updaterMode.multiIdentity,
            result.updaterMode.parallelism,
            result.furballConfig,
            furball
        )

        is UpdaterMode.Custom -> CustomUpdater(
            result.updaterMode.customUpdater,
            updatesChannel,
            supervisorJob,
            furball
        )
    }

    private var closed = false

    @Synchronized
    fun close() {
        if (closed) return
        updateReceiver?.close()
        apiClientBuilder.close()
        closed = true
    }

    init {
        val preActions = result.preActions
        if (preActions.isNotEmpty()) {
            runBlocking(Dispatchers.IO) {
                preActions.forEach {
                    it.invoke(kittyBot)
                }
            }
        }
        supervisorJob.invokeOnCompletion { close() }
    }

}


// PollingRecovery is a strategy for the polling to continue to live even when caught an error.
//  Telegram sometimes enjoys sending back error results for whatever reason.
fun KittyBotConfig<PollingReceiver>.startPolling(
    wait: Boolean = true,
    pollingRecovery: PollingRecovery = PollingRecovery.Default(),
): Job {
    if (updateReceiver !is PollingReceiver) {
        hiss("To start polling, you need to set updateOrigin to UpdateOrigin.Polling")
    }
    val pollingAsync = CoroutineScope(Dispatchers.IO + supervisorJob).async {
        updateReceiver.receiveUpdates(updatesChannel, pollingRecovery)
    }
    pollingAsync.invokeOnCompletion { cause ->
        if (cause != null && cause !is CancellationException) {
            supervisorJob.cancel(
                CancellationException("Polling receiver failed", cause)
            )
        }
    }
    updater.start()
    if (wait) {
        runBlocking {
            pollingAsync.await()
        }
    }
    return pollingAsync
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
