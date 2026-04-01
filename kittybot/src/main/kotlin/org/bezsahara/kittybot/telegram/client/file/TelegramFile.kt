package org.bezsahara.kittybot.telegram.client.file

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File
import java.io.InputStream

@Serializable(TFSerializer::class)
interface TelegramFile {
    companion object {
        fun withFile(file: File): TelegramFile = InputStreamFile(file)
        fun withBytes(
            byteArray: ByteArray,
            fileName: String? = null,
            contentType: String? = null,
        ) = TelegramFileVertx.Bytes(byteArray, fileName, contentType)

        fun withInputStream(fileName: String? = null, contentType: String? = null, provider: () -> InputStream): TelegramFile {
            return object : InputStreamAdapter() {
                override fun provide(): InputStream = provider()

                override val fileName: String
                    get() = fileName ?: super.fileName
                override val contentType: String
                    get() = contentType ?: super.contentType
            }
        }

        fun withId(id: String) = TelegramFileVertx.Id(id)
        fun withUrl(url: String) = TelegramFileVertx.Url(url)

        // Can be used with local telergam bot server
        // Such as file being uploaded by local path (via file: protocol syntax for example)
        fun withStringValue(value: String) = TelegramFileVertx.StringValue(value)
    }

    fun toJsonString(): String

    fun asVertx(): TelegramFileVertx {
        error("Not supported: you are using different TelegramFile impl, default client does not support it. U need to impl ur own custom client")
    }
}

object TFSerializer : KSerializer<TelegramFile> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TelegramFile", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TelegramFile) {
        encoder.encodeString(value.toJsonString())
    }

    override fun deserialize(decoder: Decoder): TelegramFile {
        throw SerializationException("Deserialization is not supported for TelegramFile")
    }
}