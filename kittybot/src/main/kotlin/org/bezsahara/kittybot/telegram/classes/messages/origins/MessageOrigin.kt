package org.bezsahara.kittybot.telegram.classes.messages.origins


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(MessageOriginSerialization::class)
sealed interface MessageOrigin {
    val type: String
}

internal object MessageOriginSerialization : JsonContentPolymorphicSerializer<MessageOrigin>(MessageOrigin::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MessageOrigin> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "user" -> MessageOriginUser.serializer()
            "hidden_user" -> MessageOriginHiddenUser.serializer()
            "chat" -> MessageOriginChat.serializer()
            "channel" -> MessageOriginChannel.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


