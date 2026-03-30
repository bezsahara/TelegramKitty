package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = ChatIdSerializer::class)
value class ChatId(@JvmField val value: String) {
    constructor(value: Long) : this(value.toString())

    fun isAt(): Boolean = value.length > 0 && value[0] == '@'
    fun isGroupChat(): Boolean = value.toLong() < 0
}

fun Long.toChatId(): ChatId = ChatId(this.toString())
fun String.toChatId(): ChatId = ChatId(this)

internal object ChatIdSerializer : KSerializer<ChatId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ChatId {
        return ChatId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ChatId) {
        encoder.encodeString(value.value)
    }
}