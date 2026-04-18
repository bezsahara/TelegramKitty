package org.bezsahara.kittybot.telegram.client.jclient

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.client.CustomRequest
import org.bezsahara.kittybot.telegram.client.CustomResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * [CustomClient] implementation backed by Java's built-in [HttpClient].
 */
class JavaCustomClient(
    private val client: HttpClient,
    override val requestDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CustomClient {
    override suspend fun CoroutineScope.createMPRequest(
        urlAbs: URI,
        contentType: String,
    ): CustomRequest {
        return JavaCustomRequest(
            urlAbs = urlAbs,
            contentType = contentType,
            client = client,
            requestDispatcher = requestDispatcher,
            scope = this,
        )
    }

    override suspend fun sendJSONRequest(
        urlAbs: URI,
        json: ByteArray,
        isGetUpdates: Boolean,
    ): CustomResponse {
        val requestBuilder = HttpRequest.newBuilder(urlAbs)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofByteArray(json))

        if (isGetUpdates) {
            requestBuilder.timeout(Duration.ofMillis(61_000))
        }

        return JavaResponse(
            client.sendAsync(
                requestBuilder.build(),
                HttpResponse.BodyHandlers.ofByteArray()
            ).awaitResult()
        )
    }

    override fun close() = Unit

    companion object {
        fun defaultJavaClient(): HttpClient {
            return HttpClient.newBuilder().build()
        }

        fun createDefault(): JavaCustomClient {
            return JavaCustomClient(defaultJavaClient())
        }
    }
}

private class JavaResponse(
    private val httpResponse: HttpResponse<ByteArray>,
) : CustomResponse {
    override suspend fun body(): ByteArray = httpResponse.body()

    override fun statusCode(): Int = httpResponse.statusCode()
}

private class JavaCustomRequest(
    urlAbs: URI,
    contentType: String,
    client: HttpClient,
    private val requestDispatcher: CoroutineDispatcher,
    scope: CoroutineScope,
) : CustomRequest {
    private val requestInput = java.io.PipedInputStream(PIPE_BUFFER_SIZE)
    private val requestOutput = java.io.PipedOutputStream(requestInput)
    private val bodyClosed = AtomicBoolean(false)
    private val resourcesClosed = AtomicBoolean(false)
    private val responseFuture = client.sendAsync(
        HttpRequest.newBuilder(urlAbs)
            .header("Content-Type", contentType)
            .POST(HttpRequest.BodyPublishers.ofInputStream { requestInput })
            .build(),
        HttpResponse.BodyHandlers.ofByteArray()
    )
    private val cancellationHandle = scope.coroutineContext[Job]?.invokeOnCompletion { cause ->
        if (cause != null) {
            closeResources()
            responseFuture.cancel(true)
        }
    }

    init {
        responseFuture.whenComplete { _, _ ->
            cancellationHandle?.dispose()
            closeResources()
        }
    }

    override fun write(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        requestOutput.write(bytes, startIndex, endIndex - startIndex)
    }

    override suspend fun writeSuspend(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        write(bytes, startIndex, endIndex)
    }

    override suspend fun endAndSend(): CustomResponse {
        closeBody()
        return JavaResponse(responseFuture.awaitResult())
    }

    private fun closeBody() {
        if (bodyClosed.compareAndSet(false, true)) {
            runCatching { requestOutput.close() }
        }
    }

    private fun closeResources() {
        closeBody()
        if (resourcesClosed.compareAndSet(false, true)) {
            runCatching { requestInput.close() }
        }
    }

    private companion object {
        const val PIPE_BUFFER_SIZE = 64 * 1024
    }
}

private suspend fun <T> CompletableFuture<T>.awaitResult(): T {
    return suspendCancellableCoroutine { cont ->
        whenComplete { value, throwable ->
            if (!cont.isActive) {
                return@whenComplete
            }
            if (throwable == null) {
                cont.resume(value)
            } else {
                cont.resumeWithException(unwrapCompletionCause(throwable))
            }
        }
        cont.invokeOnCancellation {
            cancel(true)
        }
    }
}

private fun unwrapCompletionCause(throwable: Throwable): Throwable {
    return generateSequence(throwable) { current ->
        current.cause?.takeIf {
            current is java.util.concurrent.CompletionException || current is java.util.concurrent.ExecutionException
        }
    }.last()
}
