package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotConfig
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.purr
import org.bezsahara.kittybot.bot.updates.FelineBuilder.PreAction
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.client.TelegramFile
import org.bezsahara.kittybot.telegram.utils.unwrapOrNull
import java.io.File


/**
 * This thing returns [KittyBotConfig] which is different from [KittyBot]. [KittyBotConfig] allows you to manage the bot
 * itself and also provides a variable to access [KittyBot] which can be used either via
 * [KittyBotConfig.purr] function or [KittyBotConfig.kittyBot] reference.
 */
@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified T : UpdateReceiver> KittyBot(noinline builder: FelineBuilder<T>.() -> Unit): KittyBotConfig<T> {
    return when (T::class) {
        PollingReceiver::class -> KittyBotPolling(builder as FelineBuilder<PollingReceiver>.() -> Unit)
        WebhookReceiver::class -> KittyBotWebhook(builder as FelineBuilder<WebhookReceiver>.() -> Unit)
        else -> error("Unknown update receiver type")
    } as KittyBotConfig<T>
}

@Suppress("FunctionName")
fun KittyBotPolling(builder: FelineBuilder<PollingReceiver>.() -> Unit): KittyBotConfig<PollingReceiver> {
    return FelineBuilder<PollingReceiver>(UpdateOrigin.Polling).apply(builder).build()
}

@Suppress("FunctionName")
fun KittyBotWebhook(builder: FelineBuilder<WebhookReceiver>.() -> Unit): KittyBotConfig<WebhookReceiver> {
    return FelineBuilder<WebhookReceiver>(UpdateOrigin.Webhook).apply(builder).build()
}

/**
 * Specifies whether bot will handle updates on a single or several threads.
 */
sealed interface UpdaterMode {
    /**
     * Updates will be handled on a single thread. It is sufficient for most use cases.
     */
    object SingleThread : UpdaterMode

    /**
     * Updates will be handled on several threads.
     * @param parallelism number of maximum coroutines running together and handling updates.
     */
    class MultiThread(val parallelism: Int) : UpdaterMode
}

enum class UpdateOrigin {
    Polling, Webhook
}

class FelineBuilder<T : UpdateReceiver> internal constructor(
    val updateOrigin: UpdateOrigin
) {

    // Bot token
    var token: String? = null
        set(value) = if (value == null) error("Token cannot be null") else field = value

    // Updater mode can be either single or multithreaded
    var updaterMode: UpdaterMode = UpdaterMode.SingleThread

    // Timeout is in seconds
    internal var pollingTimeoutP: Long = 60
    private var lastIdRecovery: RecoverLastId? = null

    val dispatchers = FelineDispatcher()

    /**
     * Same as [ensureOnlyNewUpdatesWithFile] but allows you
     * to implement your own logic of where to save this id.
     */
    fun ensureOnlyNewUpdatesCustom(engine: RecoverLastId) {
        lastIdRecovery = engine
    }

    /**
     * Saves last update's id to a file. Functions with the same idea as [ensureOnlyNewUpdates] but
     * does not wait 1 second.
     */
    fun ensureOnlyNewUpdatesWithFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!file.isFile || !file.canWrite() || !file.canRead()) {
            hiss("File ${file.name} is not a file or the program cannot either read or write it")
        }

        lastIdRecovery = object : RecoverLastId {
            override fun save(id: Long?) {
                if (id != null) {
                    file.writeText(id.toString())
                } else {
                    file.writeText("")
                }
            }

            override fun recover(): Long? {
                return file.readText().toLongOrNull()
            }
        }
    }

    /**
     * Tries to get previous updates from Telegram and invalidates them. Takes 1 second.
     * It Can be used if you do not want the bot to get updates from when it was offline
     * or if it was killed without a chance to invalidate the last update.
     */
    fun ensureOnlyNewUpdates() {
        preActions.add(PreAction {
            var offset: Long? = null
            repeat(2) { i ->
                val result = it.apiClient.getUpdates(
                    offset,
                    null,
                    1,
                    null
                ).unwrapOrNull()
                if (result != null && result.isNotEmpty()) {
                    offset = result.last().updateId + 1
                } else {
                    return@PreAction
                }
            }
        })
    }

    /**
     * Adds handlers for the bot.
     */
    fun dispatchers(builder: FelineDispatcher.() -> Unit) {
        dispatchers.apply(builder)
    }

    internal val preActions = mutableListOf<PreAction>()

    internal fun interface PreAction {
        suspend fun execute(bot: KittyBot)
    }

    fun init(block: suspend KittyBot.() -> Unit) {
        preActions.add(block)
    }

    internal fun build(): KittyBotConfig<T> {
        require(token != null) {
            "Token must be set"
        }

        return KittyBotConfig<T>(
            dispatchers,
            updaterMode,
            updateOrigin,
            pollingTimeoutP,
            preActions,
            token ?: hiss("Token must be set"),
            lastIdRecovery
        )
    }
}

// In seconds
var FelineBuilder<PollingReceiver>.pollingTimeout: Long
    get() = pollingTimeoutP
    set(value) { pollingTimeoutP = value }

/**
 * Sets a webhook in builder function.
 */
fun FelineBuilder<WebhookReceiver>.webhook(
    url: String,
    certificate: TelegramFile? = null,
    ipAddress: String? = null,
    maxConnections: Long? = null,
    allowedUpdates: List<String>? = null,
    dropPendingUpdates: Boolean? = null,
    secretToken: String? = null,
    deletePreviousWebhook: Boolean = true
) {
    preActions.add(FelineBuilder.PreAction {
        if (deletePreviousWebhook) {
            it.deleteWebhook()
        }
        it.setWebhook(
            url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken
        )
    })
}
