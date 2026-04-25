package org.bezsahara.kittybot.telegram.classes.chat.member

import org.bezsahara.kittybot.telegram.values.ChatMemberStatus
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberMember
import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberOwner
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberRestricted
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberLeft
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberBanned
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMemberAdministrator


@Serializable(with = ChatMemberSerializer::class)
sealed interface ChatMember {
    val status: ChatMemberStatus
}


private object ChatMemberSerializer : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<ChatMember> {
        return when (element.jsonObject["status"]!!.jsonPrimitive.content) {
            "creator" -> ChatMemberOwner.serializer()
            "administrator" -> ChatMemberAdministrator.serializer()
            "member" -> ChatMemberMember.serializer()
            "restricted" -> ChatMemberRestricted.serializer()
            "left" -> ChatMemberLeft.serializer()
            "kicked" -> ChatMemberBanned.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["status"]!!.jsonPrimitive.content}")
        }
    }
}



