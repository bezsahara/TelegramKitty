package org.bezsahara.kittybot.telegram.classes.reactions


import kotlinx.serialization.json.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable



@Serializable(ReactionTypeSerialization::class)
sealed interface ReactionType {
    val type: String
}

internal object ReactionTypeSerialization : JsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReactionType> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "emoji" -> ReactionTypeEmoji.serializer()
            "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


