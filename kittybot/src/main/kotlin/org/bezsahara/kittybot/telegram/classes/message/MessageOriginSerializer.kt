package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.user.User


internal object MessageOriginSerializer : KSerializer<MessageOrigin> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MessageOrigin") {
        element<String>("type")
        element<Long>("date")
        element<User>("sender_user", isOptional = true)
        element<String>("sender_user_name", isOptional = true)
        element<Chat>("sender_chat", isOptional = true)
        element<String?>("author_signature", isOptional = true)
        element<Chat>("chat", isOptional = true)
        element<Long>("message_id", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: MessageOrigin) {
        when (value) {
            is MessageOriginUser -> MessageOriginUser.serializer().serialize(encoder, value)
            is MessageOriginHiddenUser -> MessageOriginHiddenUser.serializer().serialize(encoder, value)
            is MessageOriginChat -> MessageOriginChat.serializer().serialize(encoder, value)
            is MessageOriginChannel -> MessageOriginChannel.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): MessageOrigin {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var date: Long = 0L
        var senderUser: User? = null
        var senderUserName: String? = null
        var senderChat: Chat? = null
        var authorSignature: String? = null
        var chat: Chat? = null
        var messageId: Long = 0L
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> {
                    date = dec.decodeLongElement(descriptor, 1)
                    nativeMask = nativeMask.setBit(0)
                }
                2 -> senderUser = dec.decodeSerializableElement(descriptor, 2, User.serializer())
                3 -> senderUserName = dec.decodeStringElement(descriptor, 3)
                4 -> senderChat = dec.decodeSerializableElement(descriptor, 4, Chat.serializer())
                5 -> authorSignature = dec.decodeNullableSerializableElement(descriptor, 5, String.serializer(), null)
                6 -> chat = dec.decodeSerializableElement(descriptor, 6, Chat.serializer())
                7 -> {
                    messageId = dec.decodeLongElement(descriptor, 7)
                    nativeMask = nativeMask.setBit(1)
                }
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing MessageOrigin")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "user" -> MessageOriginUser(
                date = if (nativeMask.has(0)) date else throwMissingField("date"),
                senderUser = senderUser ?: throwMissingField("sender_user")
            )
            "hidden_user" -> MessageOriginHiddenUser(
                date = if (nativeMask.has(0)) date else throwMissingField("date"),
                senderUserName = senderUserName ?: throwMissingField("sender_user_name")
            )
            "chat" -> MessageOriginChat(
                date = if (nativeMask.has(0)) date else throwMissingField("date"),
                senderChat = senderChat ?: throwMissingField("sender_chat"),
                authorSignature = authorSignature
            )
            "channel" -> MessageOriginChannel(
                date = if (nativeMask.has(0)) date else throwMissingField("date"),
                chat = chat ?: throwMissingField("chat"),
                messageId = if (nativeMask.has(1)) messageId else throwMissingField("message_id"),
                authorSignature = authorSignature
            )
            else -> throw SerializationException("Serializer wasn't found for MessageOrigin with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing MessageOrigin")
    }
}
