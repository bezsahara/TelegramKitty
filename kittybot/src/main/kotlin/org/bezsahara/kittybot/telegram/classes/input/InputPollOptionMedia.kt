package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.InputPollOptionMedia
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.input.InputMediaLocation
import org.bezsahara.kittybot.telegram.classes.input.InputMediaPhoto
import org.bezsahara.kittybot.telegram.classes.input.InputMediaSticker
import org.bezsahara.kittybot.telegram.classes.input.InputMediaVideo
import org.bezsahara.kittybot.telegram.classes.input.InputMediaLivePhoto
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.input.InputMediaAnimation
import kotlin.Unit
import org.bezsahara.kittybot.telegram.classes.input.InputMediaVenue
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = InputPollOptionMediaSerializer::class)
sealed interface InputPollOptionMedia {
    val type: String
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}


private object InputPollOptionMediaSerializer : JsonContentPolymorphicSerializer<InputPollOptionMedia>(InputPollOptionMedia::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InputPollOptionMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "animation" -> InputMediaAnimation.serializer()
            "live_photo" -> InputMediaLivePhoto.serializer()
            "location" -> InputMediaLocation.serializer()
            "photo" -> InputMediaPhoto.serializer()
            "sticker" -> InputMediaSticker.serializer()
            "venue" -> InputMediaVenue.serializer()
            "video" -> InputMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



