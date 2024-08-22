package org.bezsahara.kittybot.telegram.classes.chats.boosts.sources


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(ChatBoostSourceSerialization::class)
sealed interface ChatBoostSource {
    val source: String
}

internal object ChatBoostSourceSerialization : JsonContentPolymorphicSerializer<ChatBoostSource>(ChatBoostSource::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatBoostSource> {
        return when (element.jsonObject["source"]!!.jsonPrimitive.content) {
            "premium" -> ChatBoostSourcePremium.serializer()
            "gift_code" -> ChatBoostSourceGiftCode.serializer()
            "giveaway" -> ChatBoostSourceGiveaway.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


