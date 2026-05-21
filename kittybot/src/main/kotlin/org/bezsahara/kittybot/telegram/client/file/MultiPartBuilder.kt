package org.bezsahara.kittybot.telegram.client.file

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.file.OpenOptions
import io.vertx.core.http.HttpClientRequest
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext


// Maybe I need to improve this.
class MultiPartBuilder(@JvmField val request: HttpClientRequest) {
    val boundary = "----kitty-${UUID.randomUUID()}"

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

    suspend fun writeSuspend(byteBuffer: ByteArray) {

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
