package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.chat.ChatId


internal object BotCommandScopeSerializer : KSerializer<BotCommandScope> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BotCommandScope") {
        element<String>("type")
        element<ChatId>("chat_id", isOptional = true)
        element<Long>("user_id", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: BotCommandScope) {
        when (value) {
            is BotCommandScopeDefault -> BotCommandScopeDefault.serializer().serialize(encoder, value)
            is BotCommandScopeAllPrivateChats -> BotCommandScopeAllPrivateChats.serializer().serialize(encoder, value)
            is BotCommandScopeAllGroupChats -> BotCommandScopeAllGroupChats.serializer().serialize(encoder, value)
            is BotCommandScopeAllChatAdministrators -> BotCommandScopeAllChatAdministrators.serializer().serialize(encoder, value)
            is BotCommandScopeChat -> BotCommandScopeChat.serializer().serialize(encoder, value)
            is BotCommandScopeChatAdministrators -> BotCommandScopeChatAdministrators.serializer().serialize(encoder, value)
            is BotCommandScopeChatMember -> BotCommandScopeChatMember.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): BotCommandScope {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var chatId: ChatId? = null
        var userId: Long = 0L
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> chatId = dec.decodeSerializableElement(descriptor, 1, ChatId.serializer())
                2 -> {
                    userId = dec.decodeLongElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(0)
                }
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing BotCommandScope")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "default" -> BotCommandScopeDefault
            "all_private_chats" -> BotCommandScopeAllPrivateChats
            "all_group_chats" -> BotCommandScopeAllGroupChats
            "all_chat_administrators" -> BotCommandScopeAllChatAdministrators
            "chat" -> BotCommandScopeChat(
                chatId = chatId ?: throwMissingField("chat_id")
            )
            "chat_administrators" -> BotCommandScopeChatAdministrators(
                chatId = chatId ?: throwMissingField("chat_id")
            )
            "chat_member" -> BotCommandScopeChatMember(
                chatId = chatId ?: throwMissingField("chat_id"),
                userId = if (nativeMask.has(0)) userId else throwMissingField("user_id")
            )
            else -> throw SerializationException("Serializer wasn't found for BotCommandScope with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing BotCommandScope")
    }
}
