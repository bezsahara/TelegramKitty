package org.bezsahara.kittybot.bot.builder

import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotConfig
import org.bezsahara.kittybot.bot.KittyBotConfig.Companion.BOT_SUPERVISOR_JOB
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
import java.net.URI
import kotlin.properties.Delegates


/**
 * This thing returns [KittyBotConfig] which is different from [KittyBot]. [KittyBotConfig] allows you to manage the bot
 * itself and also provides a variable to access [KittyBot] which can be used either via
 * [KittyBotConfig.purr] function or [KittyBotConfig.kittyBot] reference.
 *
 * If you just want to create bot, without dispatcher logic. There is [createTelegramBot]
 */
@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified T : UpdateReceiver> KittyBot(parentJob: Job? = null, noinline builder: FelineBuilder<T>.() -> Unit): KittyBotConfig<T> {
    return when (T::class) {
        PollingReceiver::class -> KittyBotPolling(parentJob, builder as FelineBuilder<PollingReceiver>.() -> Unit)
        WebhookReceiver::class -> KittyBotWebhook(parentJob, builder as FelineBuilder<WebhookReceiver>.() -> Unit)
        else -> error("Unknown update receiver type")
    } as KittyBotConfig<T>
}

@Suppress("FunctionName")
fun KittyBotPolling(parentJob: Job? = null, builder: FelineBuilder<PollingReceiver>.() -> Unit): KittyBotConfig<PollingReceiver> {
    return FelineBuilder<PollingReceiver>(parentJob, UpdateOrigin.Polling).apply(builder).build()
}

@Suppress("FunctionName")
fun KittyBotWebhook(parentJob: Job? = null, builder: FelineBuilder<WebhookReceiver>.() -> Unit): KittyBotConfig<WebhookReceiver> {
    return FelineBuilder<WebhookReceiver>(parentJob, UpdateOrigin.Webhook).apply(builder).build()
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
    val parentJob: Job?,
    val updateOrigin: UpdateOrigin
) {
    val botContext = TypeAwareMap()

    val supervisorJob = SupervisorJob(parentJob)
    init {
        // TODO need to change job init
        val r = botContext.getOrPut(KittyBotConfig.BOT_SUPERVISOR_JOB) { supervisorJob }
        require(r == supervisorJob) {
            "KittyBot internal error. KittyBotConfig.BOT_SUPERVISOR_JOB was defined before needed definition"
        }
    }

    // Bot token
    var token: String by Delegates.notNull()

    var baseUri: URI = URI.create("https://api.telegram.org")

    // Updater mode can be either single or multithreaded or custom
    var updaterMode: UpdaterMode = UpdaterMode.SingleThread
        set(value) {
            checkClosed()
            field = value
        }

    var furballConfig: FurballConfig = FurballConfig.Default
        set(value) {
            checkClosed()
            field = value
        }

    // Timeout is in seconds
    internal var pollingTimeoutP: Long = 60
    private var lastIdRecovery: RecoverLastId? = null

    val dispatchers = FelineDispatcher(this)

    /**
     * Same as [ensureOnlyNewUpdatesWithFile] but allows you
     * to implement your own logic of where to save this id.
     */
    fun ensureOnlyNewUpdatesCustom(engine: RecoverLastId) {
        checkClosed()
        lastIdRecovery = engine
    }

    /**
     * Saves last update's id to a file. Functions with the same idea as [ensureOnlyNewUpdates] but
     * does not wait 1 second.
     */
    fun ensureOnlyNewUpdatesWithFile(file: File) {
        checkClosed()
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
        checkClosed()
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
        init {
            var offset: Long? = -1
            repeat(tries) { i ->
                val result = getUpdates(
                    offset,
                    null,
                    1,
                    null,
                    null
                ).unwrapOrNull()
                if (!result.isNullOrEmpty()) {
                    offset = result.last().updateId + 1
                } else {
                    return@init
                }
            }
        }
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
        checkClosed()
        preActions.add(block)
    }

    private var errorHandler: HandlerErrorHandler = HandlerErrorHandler { e, _, _, _, _ -> e.printStackTrace(); Decision.Consumed }
    internal val errorHandlerInternal: HandlerErrorHandler
        get() = errorHandler

    fun setErrorHandler(h: HandlerErrorHandler) {
        checkClosed()
        errorHandler = h
    }

    // If you want to, you can implement your own api client
    var apiClientBuilder: ClientBuilder? = null
        set(value) {
            checkClosed()
            field = value
        }
    private var useErrorConsumeCB: Boolean = false

    fun useCustomClient(customClient: CustomClient) {
        checkClosed()
        apiClientBuilder = object : ClientBuilder {
            override fun build(botApiServerConfig: BotApiServerConfig, json: Json): KittyBot {
                return TCustomClient(TPathCustom(botApiServerConfig.buildLink()), customClient, json)
            }

            override fun close() {
                customClient.close()
            }
        }
    }

    // Changes KittyBot methods impl so they will always throw on error. Instead of you using .unwrap() all the time.
    fun throwErrorsOnFailure() {
        useErrorConsumeCB = true
    }

    private var allowedUpdates: HashSet<UpdateKind<*>>? = null

    fun allowUpdatesOf(cl: UpdateKind<*>) {
        checkClosed()
        if (allowedUpdates == null) allowedUpdates = hashSetOf()
        allowedUpdates!!.add(cl)
    }

    fun allowUpdatesOf(vararg cl: UpdateKind<*>) {
        checkClosed()
        if (cl.isEmpty()) return
        if (allowedUpdates == null) allowedUpdates = hashSetOf()
        allowedUpdates!!.addAll(cl)
    }


    private var closed = false

    fun close() {
        dispatchers.close()
        closed = true
    }

    private fun checkClosed() {
        if (closed) { error("FelineBuilder was already closed!") }
    }

    internal fun build(): KittyBotConfig<T> {
        checkClosed()
        var deFactoBuilder = try {
            apiClientBuilder ?: tryFindDefaultClient()
        } catch (e: Throwable) {
            throw IllegalStateException("You did not set clientBuilder! " +
                    "Use setClientBuilder or useCustomClient functions to set your client." +
                    "Or include kittybot-client for a default client.", e)
        }

        if (useErrorConsumeCB) {
            deFactoBuilder = ConsumeClientBuilder(deFactoBuilder)
        }

        prepare()
        close()
        return KittyBotConfig<T>(
            dispatchers,
            updaterMode,
            updateOrigin,
            pollingTimeoutP,
            preActions,
            BotApiServerConfig(token, baseUri),
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
