package org.bezsahara.kittybot.bot


import io.netty.util.internal.PlatformDependent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.builder.ClientBuilder
import org.bezsahara.kittybot.bot.builder.FelineBuilder
import org.bezsahara.kittybot.bot.builder.RecoverLastId
import org.bezsahara.kittybot.bot.builder.UpdateOrigin
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.conv.ConversationRuntime
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
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


class KittyBotConfig<T : UpdateReceiver>(
    felineDispatcher: FelineDispatcher,
    updaterMode: UpdaterMode,
    val updateOrigin: UpdateOrigin,
    pollingTimeout: Long,
    preActions: List<FelineBuilder.PreAction>,
    token: String,
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

    private val tApiClient = apiClientBuilder.build(token, json)//

    internal val updateReceiver = when (updateOrigin) {
        UpdateOrigin.Polling -> PollingReceiver(tApiClient, pollingTimeout, lastIdRecovery, allowedUpdates)
        UpdateOrigin.Webhook -> null
    }

    internal val supervisorJob = botContext.getOrPut(BOT_SUPERVISOR_JOB) { SupervisorJob() }
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

    internal var waitContinuation: Continuation<Int>? = null

    fun close() {
        updateReceiver?.close()
        apiClientBuilder.close()
    }

    init {
        runBlocking(Dispatchers.IO) {
            preActions.forEach {
                it.execute(kittyBot)
            }
        }
    }

    companion object {
        @JvmField
        internal val BOT_SUPERVISOR_JOB = createTypeAwareKey<CompletableJob>("BotSupervisorJob")
    }
}

fun KittyBotConfig<PollingReceiver>.startPolling(wait: Boolean = true) {
    if (updateReceiver !is PollingReceiver) {
        hiss("To start polling, you need to set updateOrigin to UpdateOrigin.Polling")
    }
    CoroutineScope(Dispatchers.IO + supervisorJob).launch {
        updateReceiver.receiveUpdates(updatesChannel)
    }
    updater.start()
    if (wait) {
        runBlocking {
            suspendCancellableCoroutine {
                waitContinuation = it
            }
        }
        waitContinuation = null
    }
}

fun KittyBotConfig<PollingReceiver>.stopPolling() {
    supervisorJob.cancel()
    close()
    waitContinuation?.resume(0)
}

fun KittyBotConfig<WebhookReceiver>.stop() {
    supervisorJob.cancel()
    close()
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
