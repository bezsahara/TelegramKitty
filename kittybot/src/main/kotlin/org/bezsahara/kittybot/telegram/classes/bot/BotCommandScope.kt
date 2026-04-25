package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChatAdministrators
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChatMember
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.values.BotCommandScopeType
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChat
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllPrivateChats
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeDefault
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllGroupChats
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllChatAdministrators


@Serializable(with = BotCommandScopeSerializer::class)
sealed interface BotCommandScope {
    val type: BotCommandScopeType
}


private object BotCommandScopeSerializer : JsonContentPolymorphicSerializer<BotCommandScope>(BotCommandScope::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<BotCommandScope> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "default" -> BotCommandScopeDefault.serializer()
            "all_private_chats" -> BotCommandScopeAllPrivateChats.serializer()
            "all_group_chats" -> BotCommandScopeAllGroupChats.serializer()
            "all_chat_administrators" -> BotCommandScopeAllChatAdministrators.serializer()
            "chat" -> BotCommandScopeChat.serializer()
            "chat_administrators" -> BotCommandScopeChatAdministrators.serializer()
            "chat_member" -> BotCommandScopeChatMember.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



