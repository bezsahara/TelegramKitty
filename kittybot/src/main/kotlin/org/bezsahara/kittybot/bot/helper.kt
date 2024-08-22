@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.utils.onResult
import java.io.File

internal inline fun <T> notNull(obj: T): Int = if (obj == null) 0 else 1

inline fun KittyBotConfig<*>.purr(block: KittyBot.() -> Unit) {
    kittyBot.block()
    arrayOf(2).toList()
}

inline fun KittyBotConfig<*>.purrBlocking(crossinline block: suspend KittyBot.() -> Unit) =
    runBlocking(Dispatchers.IO) {
        kittyBot.block()
    }

/**
 * Downloads and writes data into [File] instance.
 * @param fileId id of a file to download.
 * @param toFile [File] instance where the file will be written
 * @return a number of copied bytes
 */
suspend fun KittyBot.downloadFile(fileId: String, toFile: File): TResult<Long> {
    val link = getFile(fileId).onResult(
        onFailure = { return it },
        onSuccess = { "https://api.telegram.org/file/bot${apiClient.token}/${it.filePath}" }
    )
    val response = apiClient.client.get {
        url(link)
    }
    if (ContentType.parse(response.headers[HttpHeaders.ContentType]!!) == ContentType.Application.Json)
        return TResult.Failure(apiClient.json.decodeFromString(TelegramError.serializer(), response.bodyAsText()))
    return TResult.Success(response.bodyAsChannel().copyAndClose(toFile.writeChannel()))
}

/**
 * Downloads a file as a ByteArray.
 */
suspend fun KittyBot.downloadFileAsByteArray(fileId: String): TResult<ByteArray> {
    val link = getFile(fileId).onResult(
        onFailure = { return it },
        onSuccess = { "https://api.telegram.org/file/bot${apiClient.token}/${it.filePath}" }
    )
    val response = apiClient.client.get {
        url(link)
    }
    if (ContentType.parse(response.headers[HttpHeaders.ContentType]!!) == ContentType.Application.Json)
        return TResult.Failure(apiClient.json.decodeFromString(TelegramError.serializer(), response.bodyAsText()))
    return TResult.Success(response.bodyAsChannel().toByteArray())
}
