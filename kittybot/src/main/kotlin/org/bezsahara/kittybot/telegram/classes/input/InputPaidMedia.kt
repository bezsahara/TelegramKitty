package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMediaPhoto
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMediaVideo
import kotlin.Unit
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMediaLivePhoto


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
            "live_photo" -> InputPaidMediaLivePhoto.serializer()
            "photo" -> InputPaidMediaPhoto.serializer()
            "video" -> InputPaidMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



