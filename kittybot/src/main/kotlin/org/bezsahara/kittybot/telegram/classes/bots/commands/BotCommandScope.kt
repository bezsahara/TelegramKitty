package org.bezsahara.kittybot.telegram.classes.bots.commands


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(BotCommandScopeSerialization::class)
sealed interface BotCommandScope {
    val type: String
}

internal object BotCommandScopeSerialization : JsonContentPolymorphicSerializer<BotCommandScope>(BotCommandScope::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BotCommandScope> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "default" -> BotCommandScopeDefault.serializer()
            "all_private_chats" -> BotCommandScopeAllPrivateChats.serializer()
            "all_group_chats" -> BotCommandScopeAllGroupChats.serializer()
            "all_chat_administrators" -> BotCommandScopeAllChatAdministrators.serializer()
            "chat" -> BotCommandScopeChat.serializer()
            "chat_administrators" -> BotCommandScopeChatAdministrators.serializer()
            "chat_member" -> BotCommandScopeChatMember.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


