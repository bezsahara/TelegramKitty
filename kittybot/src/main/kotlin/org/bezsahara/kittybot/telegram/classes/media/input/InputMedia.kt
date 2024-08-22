package org.bezsahara.kittybot.telegram.classes.media.input


import io.ktor.http.content.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(InputMediaSerialization::class)
sealed interface InputMedia {
    val type: String
    fun MutableList<PartData>.appendFiles()
}

internal object InputMediaSerialization : JsonContentPolymorphicSerializer<InputMedia>(InputMedia::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<InputMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "photo" -> InputMediaPhoto.serializer()
            "video" -> InputMediaVideo.serializer()
            "animation" -> InputMediaAnimation.serializer()
            "audio" -> InputMediaAudio.serializer()
            "document" -> InputMediaDocument.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


