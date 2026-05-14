package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.input.InputMediaLocation
import org.bezsahara.kittybot.telegram.classes.input.InputMediaDocument
import org.bezsahara.kittybot.telegram.classes.input.InputMediaPhoto
import org.bezsahara.kittybot.telegram.classes.input.InputMediaVideo
import org.bezsahara.kittybot.telegram.classes.input.InputMediaAudio
import org.bezsahara.kittybot.telegram.classes.input.InputMediaLivePhoto
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputMediaAnimation
import kotlin.Unit
import org.bezsahara.kittybot.telegram.classes.input.InputMediaVenue
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.input.InputPollMedia


@Serializable(with = InputPollMediaSerializer::class)
sealed interface InputPollMedia {
    val type: String
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}


private object InputPollMediaSerializer : JsonContentPolymorphicSerializer<InputPollMedia>(InputPollMedia::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputPollMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "animation" -> InputMediaAnimation.serializer()
            "audio" -> InputMediaAudio.serializer()
            "document" -> InputMediaDocument.serializer()
            "live_photo" -> InputMediaLivePhoto.serializer()
            "location" -> InputMediaLocation.serializer()
            "photo" -> InputMediaPhoto.serializer()
            "venue" -> InputMediaVenue.serializer()
            "video" -> InputMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



