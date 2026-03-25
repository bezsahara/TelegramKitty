package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhotoStatic
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhotoAnimated
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = InputProfilePhotoSerializer::class)
sealed interface InputProfilePhoto {
    val type: String
}


private object InputProfilePhotoSerializer : JsonContentPolymorphicSerializer<InputProfilePhoto>(InputProfilePhoto::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputProfilePhoto> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "static" -> InputProfilePhotoStatic.serializer()
            "animated" -> InputProfilePhotoAnimated.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



