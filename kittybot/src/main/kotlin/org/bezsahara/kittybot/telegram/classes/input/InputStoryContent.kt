package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContentPhoto
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import kotlinx.serialization.json.JsonElement
import kotlin.Unit
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContentVideo


@Serializable(with = InputStoryContentSerializer::class)
sealed interface InputStoryContent {
    val type: String
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
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



