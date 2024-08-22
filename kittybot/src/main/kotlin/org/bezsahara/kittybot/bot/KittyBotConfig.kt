package org.bezsahara.kittybot.bot


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.updates.*
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.classes.updates.Update
import org.bezsahara.kittybot.telegram.client.TApiClient
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class KittyBotConfig<T : UpdateReceiver<*>> internal constructor(
    felineDispatcher: FelineDispatcher,
    updaterMode: UpdaterMode,
    updateOrigin: UpdateOrigin,
    pollingTimeout: Long,
    preActions: List<FelineBuilder.PreAction>,
    token: String,
    lastIdRecovery: RecoverLastId?
) {
    internal val client = HttpClient(CIO) {
        install(ContentNegotiation)
        install(HttpTimeout)
        install(DefaultRequest) {
            url("https://api.telegram.org/bot$token/")
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    internal val json = Json {
        classDiscriminatorMode = ClassDiscriminatorMode.NONE
        encodeDefaults = true
        explicitNulls = false
    }

    @JvmField
    internal val updatesChannel = Channel<Update>()

    private val tApiClient = TApiClient(json, client, token)

    internal val updateReceiver = when (updateOrigin) {
        UpdateOrigin.Polling -> PollingReceiver(tApiClient, pollingTimeout, lastIdRecovery)
        UpdateOrigin.Webhook -> WebhookReceiver(updatesChannel)
    }

    internal val updater: Aktualisierer = when (updaterMode) {
        is UpdaterMode.SingleThread -> {
            SingleUpdater(
                KittyBot(tApiClient),
                felineDispatcher,
                updateReceiver
            )
        }

        is UpdaterMode.MultiThread -> {
            MultiUpdater(
                KittyBot(tApiClient),
                felineDispatcher,
                updateReceiver,
                updaterMode.parallelism
            )
        }
    }

    @JvmField
    val kittyBot: KittyBot = updater.bot

    internal var waitContinuation: Continuation<Int>? = null

    init {
        runBlocking(Dispatchers.IO) {
            preActions.forEach {
                it.execute(kittyBot)
            }
        }
    }
}

fun KittyBotConfig<PollingReceiver>.startPolling(wait: Boolean = true) {
    if (updateReceiver !is PollingReceiver) {
        hiss("To start polling, you need to set updateOrigin to UpdateOrigin.Polling")
    }
    updater.start()
    if (wait) {
        runBlocking {
            suspendCoroutine {
                waitContinuation = it
            }
        }
        waitContinuation = null
    }
}

fun KittyBotConfig<PollingReceiver>.stopPolling() {
    updater.stop()
    waitContinuation?.resume(0)
}

fun KittyBotConfig<WebhookReceiver>.stop() {
    updater.stop()
}

fun KittyBotConfig<WebhookReceiver>.start() {
    updater.start()
}

suspend fun KittyBotConfig<WebhookReceiver>.onUpdate(data: String) {
    updatesChannel.send(
            json.decodeFromString(Update.serializer(), data)
    )
}