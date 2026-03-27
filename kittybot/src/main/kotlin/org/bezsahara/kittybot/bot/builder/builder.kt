package org.bezsahara.kittybot.bot.builder

import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotConfig
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.bot.updates.CustomUpdaterSetup
import org.bezsahara.kittybot.bot.updates.MultiIdentity
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.TypeAwareMap
import org.bezsahara.kittybot.bot.dispatchers.prepare
import org.bezsahara.kittybot.bot.updates.FurballConfig
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.client.TCustomClient
import org.bezsahara.kittybot.telegram.client.TPathCustom
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.utils.unwrapOrNull
import java.io.File
import kotlin.properties.Delegates


/**
 * This thing returns [KittyBotConfig] which is different from [KittyBot]. [KittyBotConfig] allows you to manage the bot
 * itself and also provides a variable to access [KittyBot] which can be used either via
 * [KittyBotConfig.purr] function or [KittyBotConfig.kittyBot] reference.
 *
 * If you just want to create bot, without dispatcher logic. There is [createTelegramBot]
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
    class MultiThread(val multiIdentity: MultiIdentity, val parallelism: Int = 8) : UpdaterMode

    class Custom(val customUpdater: CustomUpdaterSetup) : UpdaterMode
}

enum class UpdateOrigin {
    Polling, Webhook
}

class FelineBuilder<T : UpdateReceiver> internal constructor(
    val updateOrigin: UpdateOrigin
) {
    // Bot token
    var token: String by Delegates.notNull()

    // Updater mode can be either single or multithreaded or custom
    var updaterMode: UpdaterMode = UpdaterMode.SingleThread

    var furballConfig: FurballConfig = FurballConfig.Default

    // Timeout is in seconds
    internal var pollingTimeoutP: Long = 60
    private var lastIdRecovery: RecoverLastId? = null

    val dispatchers = FelineDispatcher(this)

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
                }
            }

            override fun recover(): Long? {
                return file.readText().toLongOrNull()
            }
        }
    }

    fun ensureOnlyNewUpdates(onSave: (Long?) -> Unit, onRecover: () -> Long?) {
        lastIdRecovery = object : RecoverLastId {
            override fun save(id: Long?) = onSave(id)
            override fun recover(): Long? = onRecover()
        }
    }

    /**
     * Tries to get previous updates from Telegram and invalidates them. Takes 1 second.
     * It Can be used if you do not want the bot to get updates from when it was offline
     * or if it was killed without a chance to invalidate the last update.
     */
    fun ensureOnlyNewUpdates(tries: Int = 2) {
        preActions.add(PreAction {
            var offset: Long? = -1
            repeat(tries) { i ->
                val result = it.getUpdates(
                    offset,
                    null,
                    1,
                    null,
                    null
                ).unwrapOrNull()
                if (!result.isNullOrEmpty()) {
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
    inline fun dispatchers(builder: FelineDispatcher.() -> Unit) {
        dispatchers.apply(builder)
    }

    private val preActions = arrayListOf<PreAction>()

    fun interface PreAction {
        suspend fun execute(bot: KittyBot)
    }

    fun init(block: suspend KittyBot.() -> Unit) {
        preActions.add(block)
    }

    private var errorHandler: HandlerErrorHandler = HandlerErrorHandler { e, _, _, _, _ -> e.printStackTrace(); Decision.Consumed }

    fun setErrorHandler(h: HandlerErrorHandler) { errorHandler = h }

    // If you want to, you can implement your own api client
    var apiClientBuilder: ClientBuilder? = null

    fun useCustomClient(customClient: CustomClient) {
        apiClientBuilder = object : ClientBuilder {
            override fun build(token: String, json: Json): KittyBot {
                return TCustomClient(TPathCustom("https://api.telegram.org/bot$token"), customClient, json)
            }

            override fun close() {
                customClient.close()
            }
        }
    }

    private var allowedUpdates: HashSet<UpdateKind<*>>? = null

    fun allowUpdatesOf(cl: UpdateKind<*>) {
        if (allowedUpdates == null) allowedUpdates = hashSetOf()
        allowedUpdates!!.add(cl)
    }

    fun allowUpdatesOf(vararg cl: UpdateKind<*>) {
        if (cl.isEmpty()) return
        if (allowedUpdates == null) allowedUpdates = hashSetOf()
        allowedUpdates!!.addAll(cl)
    }

    val botContext = TypeAwareMap()

    internal fun build(): KittyBotConfig<T> {
        val deFactoBuilder = try {
            apiClientBuilder ?: tryFindDefaultClient()
        } catch (e: Throwable) {
            throw IllegalStateException("You did not set clientBuilder! " +
                    "Use setClientBuilder or useCustomClient functions to set your client." +
                    "Or include kittybot-client for a default client.", e)
        }

        prepare()

        return KittyBotConfig<T>(
            dispatchers,
            updaterMode,
            updateOrigin,
            pollingTimeoutP,
            preActions,
            token,
            lastIdRecovery,
            errorHandler,
            deFactoBuilder,
            allowedUpdates?.map { it.name },
            furballConfig,
            botContext
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
    require(updateOrigin == UpdateOrigin.Webhook)
    init {
        if (deletePreviousWebhook) {
            deleteWebhook()
        }
        setWebhook(
            url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken
        )
    }
}
