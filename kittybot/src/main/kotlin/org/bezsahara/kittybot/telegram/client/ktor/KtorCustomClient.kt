package org.bezsahara.kittybot.telegram.client.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.trySendBlocking
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.client.CustomRequest
import org.bezsahara.kittybot.telegram.client.CustomResponse
import java.net.URI

/**
 * Reference [CustomClient] implementation backed by Ktor.
 */
class KtorCustomClient(
    private val client: HttpClient,
    private val ownsClient: Boolean = true,
) : CustomClient {
    override suspend fun CoroutineScope.createMPRequest(
        urlAbs: URI,
        contentType: String,
    ): CustomRequest {
        return KtorCustomRequest(
            HttpRequestBuilder().apply {
                method = HttpMethod.Post
                url.takeFrom(urlAbs)
            },
            client,
            contentType,
            this
        )
    }

    override suspend fun sendJSONRequest(
        urlAbs: URI,
        json: ByteArray,
        isGetUpdates: Boolean,
    ): CustomResponse {
        val r = client.request {
            url.takeFrom(urlAbs)
            method = HttpMethod.Post
            if (isGetUpdates) {
                timeout {
                    requestTimeoutMillis = 61000
                }
            }
            setBody(ByteArrayContent(json, ContentType.Application.Json, HttpStatusCode.OK))
        }
        return KtorResp(r)
    }

    override fun close() {
        if (ownsClient) {
            client.close()
        }
    }

    companion object {
        fun defaultKtorClient(): HttpClient {
            val client = HttpClient {}
            return client
        }


        fun createDefault(): KtorCustomClient {
            return KtorCustomClient(defaultKtorClient())
        }
    }
}

class KtorResp(
    private val httpResponse: HttpResponse
) : CustomResponse {
    override suspend fun body(): ByteArray {
        return httpResponse.bodyAsBytes()
    }

    override fun statusCode(): Int {
        return httpResponse.status.value
    }
}

class KtorCustomRequest(
    private val builder: HttpRequestBuilder,
    private val client: HttpClient,
    contentType: String,
    coroutineScope: CoroutineScope
) : CustomRequest {
    private val sendChannel = Channel<ByteArray>(1024)
    private val response: Deferred<HttpResponse> = coroutineScope.async {
        builder.setBody(object : OutgoingContent.WriteChannelContent() {
            override val contentType: ContentType = ContentType.parse(contentType)

            override suspend fun writeTo(channel: ByteWriteChannel) {
                sendChannel.consumeEach {
                    channel.writeFully(it, 0, it.size)
                }
            }
        })
        client.request(builder)
    }.also { req ->
        req.invokeOnCompletion { cause ->
            if (cause != null) {
                sendChannel.close(cause)
            }
        }
    }

    override fun write(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        sendChannel.trySendBlocking(bytes.copyOfRange(startIndex, endIndex)).getOrThrow()
    }

    override fun write(bytes: ByteArray) {
        write(bytes, 0, bytes.size)
    }

    override suspend fun writeSuspend(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        sendChannel.send(bytes.copyOfRange(startIndex, endIndex))
    }

    override suspend fun endAndSend(): CustomResponse {
        sendChannel.close()
        return KtorResp(response.await())
    }
}
