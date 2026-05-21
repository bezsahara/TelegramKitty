package org.bezsahara.kittybot.telegram.classes.message.reactions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.values.ReactionEmoji


internal object ReactionTypeSerializer : KSerializer<ReactionType> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ReactionType") {
        element<String>("type")
        element<ReactionEmoji>("emoji", isOptional = true)
        element<String>("custom_emoji_id", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ReactionType) {
        when (value) {
            is ReactionTypeEmoji -> ReactionTypeEmoji.serializer().serialize(encoder, value)
            is ReactionTypeCustomEmoji -> ReactionTypeCustomEmoji.serializer().serialize(encoder, value)
            is ReactionTypePaid -> ReactionTypePaid.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): ReactionType {
        var type: String? = null
        var emoji: ReactionEmoji? = null
        var customEmojiId: String? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> emoji = dec.decodeSerializableElement(descriptor, 1, ReactionEmoji.serializer())
                2 -> customEmojiId = dec.decodeStringElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing ReactionType")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "emoji" -> ReactionTypeEmoji(
                emoji = emoji ?: throwMissingField("emoji")
            )
            "custom_emoji" -> ReactionTypeCustomEmoji(
                customEmojiId = customEmojiId ?: throwMissingField("custom_emoji_id")
            )
            "paid" -> ReactionTypePaid
            else -> throw SerializationException("Serializer wasn't found for ReactionType with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing ReactionType")
    }
}
