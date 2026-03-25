package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object MaybeInaccessibleMessageSerializer : JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MaybeInaccessibleMessage> {
        return when (element.jsonObject["date"]!!.jsonPrimitive.content) {
            "0" -> InaccessibleMessage.serializer()
            else -> Message.serializer()
        }
    }
}
