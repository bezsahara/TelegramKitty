package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMediaVideo
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMediaPhoto
import kotlinx.serialization.Serializable


@Serializable(with = InputPaidMediaSerializer::class)
sealed interface InputPaidMedia {
    val type: String
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



