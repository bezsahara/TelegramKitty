package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = RichTextArraySerializer::class)
data class RichTextArray(
    val value: List<RichText>,
) : RichText {
    override val type: String get() = "array"
}

internal object RichTextArraySerializer : KSerializer<RichTextArray> {
    private val listSerializer = ListSerializer(RichTextSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: RichTextArray) {
        listSerializer.serialize(encoder, value.value)
    }

    override fun deserialize(decoder: Decoder): RichTextArray {
        return RichTextArray(listSerializer.deserialize(decoder))
    }
}
