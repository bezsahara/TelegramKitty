@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot

import io.vertx.core.http.HttpMethod
import io.vertx.core.http.RequestOptions
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.client.TApiClient
import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.utils.TResultFailure
import org.bezsahara.kittybot.telegram.utils.onResult
import org.bezsahara.kittybot.telegram.utils.unwrapOrNull
import java.util.function.Function

//internal inline fun <T> notNull(obj: T): Int = if (obj == null) 0 else 1

inline fun KittyBotConfig<*>.purr(block: KittyBot.() -> Unit) {
    kittyBot.block()
//    arrayOf(2).toList()
}

inline fun KittyBotConfig<*>.purrBlocking(crossinline block: suspend KittyBot.() -> Unit) =
    runBlocking(Dispatchers.IO) {
        kittyBot.block()
    }

/**
 * Downloads a file as a ByteArray.
 */
suspend fun KittyBot.downloadFileAsByteArray(fileId: String): TResult<ByteArray> {
    val link = getFile(fileId).onResult(
        onError = { return TResultFailure(it) },
        onSuccess = { it.filePath }
    )
    if (link == null) {
        throw KittyError("Download file could not be downloaded: $fileId. No file path!")
    }
    val apiClient = this.vertxClient()
    val response = apiClient.client.request(createDownloadUrl(link)).compose(Function {
        it.send()
    }).coAwait()

    if (response.headers()["Content-Type"].startsWith("application/json")) {
        return TResultFailure(
            apiClient.json.decodeFromString(
                TelegramError.serializer(), response.body().coAwait().toString(
                    Charsets.UTF_8
                )
            )
        )
    }
    return TResult(response.body().coAwait().bytes)
}

suspend fun KittyBot.downloadFileAsByteArray(file: File): TResult<ByteArray> {
    val link = file.filePath ?: throw KittyError("Download file could not be downloaded: $file. No file path!")
    val apiClient = this.vertxClient()
    val response = apiClient.client.request(createDownloadUrl(link)).compose(Function {
        it.send()
    }).coAwait()

    if (response.headers()["Content-Type"].startsWith("application/json")) {
        return TResultFailure(
            apiClient.json.decodeFromString(
                TelegramError.serializer(), response.body().coAwait().toString(
                    Charsets.UTF_8
                )
            )
        )
    }
    return TResult(response.body().coAwait().bytes)
}

private fun createDownloadUrl(filePath: String): RequestOptions {
    return RequestOptions().setPort(443).setURI("/file/bot/$filePath").setMethod(HttpMethod.GET)
        .setHost("api.telegram.org").setSsl(true)
}

fun KittyBot.vertxClient(reason: String): TApiClient {
    return (this as? TApiClient) ?: hiss(reason)
}

fun KittyBot.vertxClient(): TApiClient {
    return (this as? TApiClient)
        ?: error("Only TApiClient supports file downloads! But your client is ${this::class.java.name}")
}
