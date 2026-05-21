package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bezsahara.kittybot.bot.json.SealedJsonElementSerializer


@Serializable(with = OwnedGiftSerializer::class)
sealed interface OwnedGift {
    val type: String
}


private object OwnedGiftSerializer : SealedJsonElementSerializer<OwnedGift>(OwnedGift::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<OwnedGift> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "regular" -> OwnedGiftRegular.serializer()
            "unique" -> OwnedGiftUnique.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



