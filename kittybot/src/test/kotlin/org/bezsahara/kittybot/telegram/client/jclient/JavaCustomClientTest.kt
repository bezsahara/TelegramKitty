package org.bezsahara.kittybot.telegram.client.jclient

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import java.net.InetSocketAddress
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class JavaCustomClientTest {

    @Test
    fun sendJsonRequestPostsJsonBody() = runBlocking {
        val captured = CompletableFuture<CapturedRequest>()
        val server = createServer("/json", 200, """{"ok":true}""".toByteArray(), captured)

        try {
            val client = JavaCustomClient.createDefault()
            val payload = """{"hello":"world"}""".toByteArray()

            val response = client.sendJSONRequest(
                urlAbs = server.url("/json"),
                json = payload,
                isGetUpdates = false,
            )

            assertEquals(200, response.statusCode())
            assertContentEquals("""{"ok":true}""".toByteArray(), response.body())

            val request = captured.get(5, TimeUnit.SECONDS)
            assertEquals("POST", request.method)
            assertEquals("application/json", request.contentType)
            assertContentEquals(payload, request.body)
        } finally {
            server.stop()
        }
    }

    @Test
    fun multipartRequestStreamsFullBody() = runBlocking {
        val captured = CompletableFuture<CapturedRequest>()
        val server = createServer("/multipart", 201, "uploaded".toByteArray(), captured)

        try {
            val client = JavaCustomClient.createDefault()
            val prefix = "--kitty\r\n".toByteArray()
            val payload = ByteArray(200_000) { index -> (index % 251).toByte() }
            val suffix = "\r\n--kitty--\r\n".toByteArray()

            val response = with(client) {
                val request = createMPRequest(
                    urlAbs = server.url("/multipart"),
                    contentType = "multipart/form-data; boundary=kitty",
                )

                request.write(prefix)

                var offset = 0
                val chunkSize = 8192
                while (offset < payload.size) {
                    val end = minOf(offset + chunkSize, payload.size)
                    request.writeSuspend(payload, offset, end)
                    offset = end
                }

                request.write(suffix)
                request.endAndSend()
            }

            assertEquals(201, response.statusCode())
            assertContentEquals("uploaded".toByteArray(), response.body())

            val request = captured.get(5, TimeUnit.SECONDS)
            assertEquals("POST", request.method)
            assertEquals("multipart/form-data; boundary=kitty", request.contentType)
            assertContentEquals(prefix + payload + suffix, request.body)
        } finally {
            server.stop()
        }
    }

    private fun createServer(
        path: String,
        responseCode: Int,
        responseBody: ByteArray,
        captured: CompletableFuture<CapturedRequest>,
    ): TestServer {
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        server.createContext(path) { exchange ->
            exchange.use {
                captured.complete(
                    CapturedRequest(
                        method = exchange.requestMethod,
                        contentType = exchange.requestHeaders.getFirst("Content-Type"),
                        body = exchange.requestBody.readAllBytes(),
                    )
                )
                exchange.sendResponseHeaders(responseCode, responseBody.size.toLong())
                exchange.responseBody.use { body ->
                    body.write(responseBody)
                }
            }
        }
        server.start()
        return TestServer(server)
    }

    private data class CapturedRequest(
        val method: String,
        val contentType: String?,
        val body: ByteArray,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CapturedRequest

            if (method != other.method) return false
            if (contentType != other.contentType) return false
            if (!body.contentEquals(other.body)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = method.hashCode()
            result = 31 * result + (contentType?.hashCode() ?: 0)
            result = 31 * result + body.contentHashCode()
            return result
        }
    }

    private class TestServer(
        private val server: HttpServer,
    ) {
        fun url(path: String): String = "http://127.0.0.1:${server.address.port}$path"

        fun stop() {
            server.stop(0)
        }
    }
}
