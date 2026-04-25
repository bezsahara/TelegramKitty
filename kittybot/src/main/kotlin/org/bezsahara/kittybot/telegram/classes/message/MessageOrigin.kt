package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.MessageOriginChat
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.message.MessageOriginHiddenUser
import org.bezsahara.kittybot.telegram.values.MessageOriginType
import org.bezsahara.kittybot.telegram.classes.message.MessageOrigin
import org.bezsahara.kittybot.telegram.classes.message.MessageOriginUser
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageOriginChannel


@Serializable(with = MessageOriginSerializer::class)
sealed interface MessageOrigin {
    val type: MessageOriginType
}


private object MessageOriginSerializer : JsonContentPolymorphicSerializer<MessageOrigin>(MessageOrigin::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<MessageOrigin> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "user" -> MessageOriginUser.serializer()
            "hidden_user" -> MessageOriginHiddenUser.serializer()
            "chat" -> MessageOriginChat.serializer()
            "channel" -> MessageOriginChannel.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



