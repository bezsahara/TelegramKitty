package org.bezsahara.kittybot.telegram.client.file

import io.vertx.core.buffer.Buffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

abstract class InputStreamAdapter : TelegramFileVertx() {
    abstract fun provide(): InputStream

    open val fileName: String get() = "data.bin"
    open val contentType: String get() = "application/octet-stream"
    open val dispatcher get() = Dispatchers.IO

    final override suspend fun execute(
        builder: MultiPartBuilder,
        name: String?
    ) {
        provide().use { inputStream ->
//            val cl = size.let {
//                if (it == -1) "" else "\r\nContent-Length: $it"
//            }
            builder.beginPart("name=${name ?: identityString()}; filename=\"$fileName\"\r\nContent-Type: $contentType")

            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytes = withContext(dispatcher) { inputStream.read(buffer) }
            while (bytes >= 0) {
                if (builder.request.writeQueueFull()) {
                    suspendCancellableCoroutine { cont ->
                        builder.request.drainHandler {
                            cont.resume(Unit, null as ((cause: Throwable, value: Unit, context: CoroutineContext) -> Unit)?)
                        }
                    }
                }
                builder.write(Buffer.buffer(bytes).appendBytes(buffer, 0, bytes))
                bytes = withContext(dispatcher) { inputStream.read(buffer) }
            }

            builder.endPart()
        }
    }

    override suspend fun executeCustom(
        builder: CustomMPB,
        name: String?,
    ) {
        provide().use { inputStream ->
//            val cl = size.let {
//                if (it == -1) "" else "\r\nContent-Length: $it"
//            }
            builder.beginPart("name=${name ?: identityString()}; filename=\"$fileName\"\r\nContent-Type: $contentType")

            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytes = withContext(dispatcher) { inputStream.read(buffer) }
            while (bytes >= 0) {
                builder.writeSuspend(buffer, 0, bytes)
                bytes = withContext(dispatcher) { inputStream.read(buffer) }
            }

            builder.endPart()
        }
    }
}

class InputStreamFile(val file: File) : InputStreamAdapter() {
    override val fileName: String
        get() = file.name

    override fun provide(): InputStream {
        return file.inputStream()
    }
}
