package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.values.InputMediaType
import org.bezsahara.kittybot.telegram.classes.input.InputMediaDocument
import org.bezsahara.kittybot.telegram.classes.input.InputMediaPhoto
import org.bezsahara.kittybot.telegram.classes.input.InputMediaVideo
import org.bezsahara.kittybot.telegram.classes.input.InputMediaAudio
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputMediaAnimation
import kotlin.Unit
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = InputMediaSerializer::class)
sealed interface InputMedia {
    val type: InputMediaType
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}


private object InputMediaSerializer : JsonContentPolymorphicSerializer<InputMedia>(InputMedia::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "animation" -> InputMediaAnimation.serializer()
            "document" -> InputMediaDocument.serializer()
            "audio" -> InputMediaAudio.serializer()
            "photo" -> InputMediaPhoto.serializer()
            "video" -> InputMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



