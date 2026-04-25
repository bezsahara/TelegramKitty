package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.values.InputProfilePhotoType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhotoStatic
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhotoAnimated
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import kotlin.Unit
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = InputProfilePhotoSerializer::class)
sealed interface InputProfilePhoto {
    val type: InputProfilePhotoType
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
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



