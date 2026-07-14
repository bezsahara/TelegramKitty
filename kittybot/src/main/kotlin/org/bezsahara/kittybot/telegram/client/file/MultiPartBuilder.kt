package org.bezsahara.kittybot.telegram.client.file

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.file.OpenOptions
import io.vertx.core.http.HttpClientRequest
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.coroutines.CoroutineContext


// Maybe I need to improve this.
class MultiPartBuilder(@JvmField val request: HttpClientRequest) {
    val boundary = generateMultiPartBoundary()

    init {
        request.putHeader("Content-Type", "multipart/form-data; boundary=$boundary")
//        val file: AsyncFile = Vertx.vertx().fileSystem()
//            .open("example.jpg", OpenOptions().setRead(true))
//            .await()
//        file.pipeTo(request)
    }

    fun writeFilePart(file: File, vertx: Vertx) {
        val file = vertx.fileSystem()
            .open(file.absolutePath, OpenOptions().setRead(true))

    }

    fun write(byteBuffer: Buffer) {
        request.write(byteBuffer)
    }

    fun beginPart(attributes: String) {
        request.write("--$boundary\r\nContent-Disposition: form-data; $attributes\r\n\r\n")
    }

    fun endPart() {
        request.write("\r\n")
    }

    fun writePart(data: String, attributes: String) {
        request.write("--$boundary\r\nContent-Disposition: form-data; $attributes\r\n\r\n$data\r\n")
    }

    fun writeNormalPart(name: String, value: String) {
        request.write("--$boundary\r\nContent-Disposition: form-data; name=$name\r\n\r\n")
        request.write(value)
        request.write("\r\n")
    }

    fun <T> writeJsonPart(name: String, serializer: SerializationStrategy<T>, value: T, json: Json) {
        request.write("--$boundary\r\nContent-Disposition: form-data; name=$name\r\n\r\n")
        request.write(json.encodeToString(serializer, value))
        request.write("\r\n")
    }

    fun finish() {
        request.write("--$boundary--\r\n")
        request.end()
    }

    companion object {
        val d9: ((cause: Throwable, value: Unit, context: CoroutineContext) -> Unit)? = null
    }
}

private val encoder = Base64.getUrlEncoder().withoutPadding()
private val prefix = "----kitty-".toByteArray(Charsets.US_ASCII)

internal fun generateMultiPartBoundary(): String {
    val bytes = ByteArray(18)
    ThreadLocalRandom.current()
        .nextBytes(bytes)
    val output = ByteArray(34) // 24 is base64 + 10 prefix
    encoder.encode(bytes, output)
    System.arraycopy(output, 0, output, 10, 24)
    System.arraycopy(prefix, 0, output, 0, 10)
    @Suppress("DEPRECATION", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    return java.lang.String(output, 0, 0, 34) as String
}