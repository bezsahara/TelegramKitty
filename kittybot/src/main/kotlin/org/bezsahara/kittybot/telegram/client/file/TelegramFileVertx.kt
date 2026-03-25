package org.bezsahara.kittybot.telegram.client.file

import io.netty.buffer.Unpooled
import io.vertx.core.buffer.Buffer
import io.vertx.core.internal.buffer.BufferInternal
import java.util.concurrent.atomic.AtomicLong

sealed class TelegramFileVertx : TelegramFile {
    @JvmField val identity: Long = counter.getAndAdd(1)

    fun identityString(): String = "f$identity"

    companion object {
        private val counter = AtomicLong(0)
        private val el = {}
    }

    // Implement stuff urself if u want. Tho do NOT fuck with JSON stuff.
    abstract class Custom : TelegramFileVertx() {
        final override fun toJsonString(): String {
            return super.toJsonString()
        }
    }

    @Suppress("DEPRECATION")
    class Bytes(private val bytes: ByteArray, fileName: String? = null, contentType: String? = null) : TelegramFileVertx() {
        private val contentType = contentType ?: "application/octet-stream"
        private val fileName = fileName ?: "data.bin"
        constructor(bytes: ByteArray) : this(bytes, null, null)

        override suspend fun execute(
            builder: MultiPartBuilder,
            name: String?,
        ) {
            builder.beginPart("name=${name ?: identityString()}; filename=\"$fileName\"\r\nContent-Type: $contentType\r\nContent-Length: ${bytes.size}")
            builder.request.write(BufferInternal.buffer(Unpooled.wrappedBuffer(bytes)))
            builder.endPart()
        }

        override suspend fun executeCustom(
            builder: CustomMPB,
            name: String?,
        ) {
            builder.beginPart("name=${name ?: identityString()}; filename=\"$fileName\"\r\nContent-Type: $contentType\r\nContent-Length: ${bytes.size}")
            builder.write(bytes)
            builder.endPart()
        }
    }

    class Id(val id: String) : TelegramFileVertx() {
        override suspend fun execute(
            builder: MultiPartBuilder,
            name: String?,
        ) {
            if (name == null) return
            builder.writePart(id, "name=$name")
        }

        override suspend fun executeCustom(
            builder: CustomMPB,
            name: String?,
        ) {
            if (name == null) return
            builder.writePart(id, "name=$name")
        }

        final override fun toJsonString(): String = id
    }

    class Url(val url: String) : TelegramFileVertx() {
        override suspend fun execute(
            builder: MultiPartBuilder,
            name: String?,
        ) {
            if (name == null) return
            builder.writePart(url, "name=$name")
        }

        override suspend fun executeCustom(
            builder: CustomMPB,
            name: String?,
        ) {
            if (name == null) return
            builder.writePart(url, "name=$name")
        }

        final override fun toJsonString(): String = url
    }

    final override fun asVertx(): TelegramFileVertx {
        return this
    }

    abstract suspend fun execute(builder: MultiPartBuilder, name: String?)

    abstract suspend fun executeCustom(builder: CustomMPB, name: String?)

    open val size: Int get() = -1

    open override fun toJsonString(): String = "attach://${identityString()}"
}

