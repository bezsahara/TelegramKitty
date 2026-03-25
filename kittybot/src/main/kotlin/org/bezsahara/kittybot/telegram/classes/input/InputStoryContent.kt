package org.bezsahara.kittybot.telegram.classes.input

import org.bezsahara.kittybot.telegram.classes.input.InputStoryContentPhoto
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContentVideo


@Serializable(with = InputStoryContentSerializer::class)
sealed interface InputStoryContent {
    val type: String
}


private object InputStoryContentSerializer : JsonContentPolymorphicSerializer<InputStoryContent>(InputStoryContent::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputStoryContent> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "photo" -> InputStoryContentPhoto.serializer()
            "video" -> InputStoryContentVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



