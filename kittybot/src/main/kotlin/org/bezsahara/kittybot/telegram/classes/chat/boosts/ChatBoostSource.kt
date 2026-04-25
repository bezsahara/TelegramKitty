package org.bezsahara.kittybot.telegram.classes.chat.boosts

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bezsahara.kittybot.telegram.values.ChatBoostSourceKind
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourceGiveaway
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourceGiftCode
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSource
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostSourcePremium
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = ChatBoostSourceSerializer::class)
sealed interface ChatBoostSource {
    val source: ChatBoostSourceKind
}


private object ChatBoostSourceSerializer : JsonContentPolymorphicSerializer<ChatBoostSource>(ChatBoostSource::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<ChatBoostSource> {
        return when (element.jsonObject["source"]!!.jsonPrimitive.content) {
            "premium" -> ChatBoostSourcePremium.serializer()
            "gift_code" -> ChatBoostSourceGiftCode.serializer()
            "giveaway" -> ChatBoostSourceGiveaway.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["source"]!!.jsonPrimitive.content}")
        }
    }
}



