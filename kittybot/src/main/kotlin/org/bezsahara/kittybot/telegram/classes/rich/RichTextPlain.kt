package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = RichTextPlainSerializer::class)
data class RichTextPlain(
    val value: String,
) : RichText {
    override val type: String get() = "text"
}

internal object RichTextPlainSerializer : KSerializer<RichTextPlain> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RichTextPlain", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: RichTextPlain) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): RichTextPlain {
        return RichTextPlain(decoder.decodeString())
    }
}
