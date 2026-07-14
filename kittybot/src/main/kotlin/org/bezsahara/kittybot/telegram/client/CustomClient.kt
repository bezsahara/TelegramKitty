package org.bezsahara.kittybot.telegram.client

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.net.URI

/**
 * SPI for plugging a custom HTTP engine into [TCustomClient].
 *
 * The library passes absolute Telegram method URLs into this interface, so an implementation does not
 * need to know anything about bot tokens or path building.
 *
 * @see org.bezsahara.kittybot.telegram.client.ktor.KtorCustomClient
 * @see org.bezsahara.kittybot.telegram.client.jclient.JavaCustomClient
 */
interface CustomClient {
    /**
     * Create a multipart POST request.
     *
     * The returned [CustomRequest] is used as a write sink for the request body. Implementations may
     * start the underlying HTTP request immediately, but [CustomRequest.endAndSend] must only return
     * after the body is fully finished and the response is available.
     */
    suspend fun CoroutineScope.createMPRequest(
        urlAbs: URI,
        contentType: String
    ): CustomRequest

    // An id if u need to cache anything
    // each method is guaranteed to have a unique id
    // Smallest id is 0
    suspend fun CoroutineScope.createMPRequest(
        urlAbs: URI,
        contentType: String,
        id: Int
    ): CustomRequest {
        return createMPRequest(urlAbs, contentType)
    }

    /**
     * Send a JSON POST request and return the raw HTTP response.
     *
     * [isGetUpdates] is only a hint for clients that want a special timeout policy for long polling.
     */
    suspend fun sendJSONRequest(
        urlAbs: URI,
        json: ByteArray,
        isGetUpdates: Boolean
    ): CustomResponse

    // An id if u need to cache anything
    // each method is guaranteed to have a unique id
    // Smallest id is 0
    suspend fun sendJSONRequest(
        urlAbs: URI,
        json: ByteArray,
        isGetUpdates: Boolean,
        id: Int
    ): CustomResponse {
        return sendJSONRequest(urlAbs, json, isGetUpdates)
    }

    /**
     * Dispatcher used by [TCustomClient] for all requests made through this SPI.
     */
    val requestDispatcher: CoroutineDispatcher get() = Dispatchers.IO

    /**
     * Release engine resources.
     */
    fun close()
}

/**
 * Writable request body used for multipart uploads.
 */
interface CustomRequest {
    /**
     * Write a byte range into the request body.
     *
     * [endIndex] is exclusive.
     */
    fun write(bytes: ByteArray, startIndex: Int, endIndex: Int)

    /**
     * Suspended variant of [write]. Override it if the underlying client needs real backpressure.
     */
    suspend fun writeSuspend(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        write(bytes, startIndex, endIndex)
    }

    /**
     * Writes a UTF-8 string into the request body.
     */
    fun write(string: String) {
        val strBytes = string.toByteArray(Charsets.UTF_8)
        write(strBytes, 0, strBytes.size)
    }

    fun write(bytes: ByteArray) {
        write(bytes, 0, bytes.size)
    }

    /**
     * Finish the request body, send any remaining data, and return the response.
     */
    suspend fun endAndSend(): CustomResponse
}


/**
 * Minimal raw HTTP response abstraction used by [TCustomClient].
 */
interface CustomResponse {
    fun statusCode(): Int

    /**
     * Return the full response body as bytes.
     */
    suspend fun body(): ByteArray
}
