package org.bezsahara.kittybot.telegram.classes.message.reactions

import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionTypeEmoji
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionTypeCustomEmoji
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionTypePaid


@Serializable(with = ReactionTypeSerializer::class)
sealed interface ReactionType {
    val type: String
}


private object ReactionTypeSerializer : JsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<ReactionType> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "emoji" -> ReactionTypeEmoji.serializer()
            "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
            "paid" -> ReactionTypePaid.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



