package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder


@Serializable(with = InputPaidMediaSerializer::class)
sealed interface InputPaidMedia {
    val type: String
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}


private object InputPaidMediaSerializer : JsonContentPolymorphicSerializer<InputPaidMedia>(InputPaidMedia::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputPaidMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "photo" -> InputPaidMediaPhoto.serializer()
            "video" -> InputPaidMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



