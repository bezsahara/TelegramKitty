package org.bezsahara.kittybot.telegram.classes.chats.members


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(ChatMemberSerialization::class)
sealed interface ChatMember {
    val status: String
}

internal object ChatMemberSerialization : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatMember> {
        return when (element.jsonObject["status"]!!.jsonPrimitive.content) {
            "creator" -> ChatMemberOwner.serializer()
            "administrator" -> ChatMemberAdministrator.serializer()
            "member" -> ChatMemberMember.serializer()
            "restricted" -> ChatMemberRestricted.serializer()
            "left" -> ChatMemberLeft.serializer()
            "kicked" -> ChatMemberBanned.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


