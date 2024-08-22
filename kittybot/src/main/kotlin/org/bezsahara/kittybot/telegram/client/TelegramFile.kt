package org.bezsahara.kittybot.telegram.client


import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File


@Serializable(TFSerializer::class)
sealed class TelegramFile {

    class LocalFile(val file: File) : TelegramFile() {
        override fun MutableList<PartData>.appendFile(name: String) {
            val fileBytes = file.readBytes()
            add(
                PartData.FileItem(
                    { ByteReadPacket(fileBytes) },
                    {},
                    HeadersImpl(
                        mapOf(
                            HttpHeaders.ContentDisposition to listOf("form-data; name=$name; filename=${file.name}"),
                            HttpHeaders.ContentLength to listOf(fileBytes.size.toString()),
                        )
                    )
                )
            )
        }
    }

    // file name can be skipped for sending photos as bytes
    class Bytes(val fileName: String, private val bytes: ByteArray) : TelegramFile() {
        constructor(bytes: ByteArray): this("a", bytes)

        override fun MutableList<PartData>.appendFile(name: String) {
            add(
                PartData.BinaryItem(
                    { ByteReadPacket(bytes) },
                    {},
                    HeadersImpl(
                        mapOf(
                            HttpHeaders.ContentDisposition to listOf("form-data; name=$name; filename=$fileName"),
                            HttpHeaders.ContentLength to listOf(bytes.size.toString())
                        )
                    )
                )
            )
        }
    }

    class Id(val id: String) : TelegramFile() {
        override fun MutableList<PartData>.appendFile(name: String) {
            add(
                PartData.FormItem(
                    id, {}, HeadersImpl(
                        mapOf(
                            HttpHeaders.ContentDisposition to listOf("form-data; name=$name")
                        )
                    )
                )
            )
        }
    }

    abstract fun MutableList<PartData>.appendFile(name: String)

    internal fun MutableList<PartData>.appendOnlyData(name: String) {
        when (this@TelegramFile) {
            is Bytes -> appendFile(name)
            is Id -> Unit
            is LocalFile -> appendFile(name)
        }
    }
}

fun File.toTelegramFile(): TelegramFile.LocalFile {
    return TelegramFile.LocalFile(this)
}

fun ByteArray.toTelegramFile(name: String): TelegramFile.Bytes {
    return TelegramFile.Bytes(name, this)
}

fun ByteArray.toTelegramFile(): TelegramFile.Bytes {
    return TelegramFile.Bytes(this)
}

object TFSerializer : KSerializer<TelegramFile> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TelegramFile", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TelegramFile) {
        encoder.encodeString(
            when (value) {
                is TelegramFile.Bytes -> "attach://${value.fileName}"
                is TelegramFile.Id -> value.id
                is TelegramFile.LocalFile -> "attach://${value.file.name}"
            }
        )
    }

    override fun deserialize(decoder: Decoder): TelegramFile {
        throw SerializationException("Deserialization is not supported for TelegramFile")
    }
}

