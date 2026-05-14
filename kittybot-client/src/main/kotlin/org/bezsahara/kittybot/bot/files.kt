@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot

import io.vertx.core.http.HttpMethod
import io.vertx.core.http.RequestOptions
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.client.TApiClient
import org.bezsahara.kittybot.telegram.client.TConsumeBot
import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.utils.TResultFailure
import org.bezsahara.kittybot.telegram.utils.onResult
import java.lang.invoke.MethodHandles
import java.util.function.Function

inline fun KittyBotConfig<*>.purr(block: KittyBot.() -> Unit) {
    kittyBot.block()
}

inline fun KittyBotConfig<*>.purrBlocking(crossinline block: suspend KittyBot.() -> Unit) =
    runBlocking(Dispatchers.IO) {
        kittyBot.block()
    }

/**
 * Downloads a file as a ByteArray.
 */
suspend inline fun KittyBot.downloadFileAsByteArrayById(fileId: String): TResult<ByteArray> {
    val link = getFile(fileId).onResult(
        onError = { return TResultFailure(it) },
        onSuccess = { it.filePath }
    )
    if (link == null) {
        throw KittyError("Download file could not be downloaded: $fileId. No file path!")
    }
    return downloadFileAsByteArray(link)
}

suspend inline fun KittyBot.downloadFileAsByteArray(file: File): TResult<ByteArray> = downloadFileAsByteArray(file.filePath ?: throw KittyError("Download file could not be downloaded: $file. No file path!"))

suspend fun KittyBot.downloadFileAsByteArray(filePath: String): TResult<ByteArray> {
    val apiClient = this.vertxClient()
    val (bytes, isJson) = withContext(apiClient.dispatcher) {
        apiClient.client.request(createDownloadUrl(filePath, apiClient.ro00.token)).compose(Function {
            it.send()
        }).compose(Function {
            val starts = it.headers()["Content-Type"]?.startsWith("application/json") == true
            it.body().map(Function { b -> b.bytes to starts })
        }).coAwait()
    }

    if (isJson) {
        return TResultFailure(
            apiClient.json.decodeFromString(
                TelegramError.serializer(), String(bytes, Charsets.UTF_8)
            )
        )
    }
    return TResult(bytes)
}

private fun createDownloadUrl(filePath: String, token: String): RequestOptions {
    return RequestOptions().setPort(443).setURI("/file/bot$token/$filePath").setMethod(HttpMethod.GET)
        .setHost("api.telegram.org").setSsl(true)
}

fun KittyBot.vertxClient(reason: String): TApiClient {
    val jc = javaClass
    return when {
        jc === TApiClient::class.java -> this as TApiClient
        jc === DelegatingKittyBot::class.java -> (DKBMh.mh.invokeExact((this as DelegatingKittyBot)) as KittyBot).vertxClient(reason)
        jc === TConsumeBot::class.java -> (this as TConsumeBot).delegate.vertxClient(reason)
        else -> hiss(reason)
    }
}

fun KittyBot.vertxClient(): TApiClient {
    return vertxClient("Only TApiClient supports file downloads! But your client is ${this::class.java.name}")
}


private object DKBMh {
    @JvmField val mh = MethodHandles
        .privateLookupIn(DelegatingKittyBot::class.java, MethodHandles.lookup())
        .findGetter(DelegatingKittyBot::class.java, "delegate", KittyBot::class.java)!!
}
