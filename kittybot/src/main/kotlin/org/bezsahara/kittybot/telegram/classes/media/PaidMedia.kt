package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.PaidMediaLivePhoto
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia
import org.bezsahara.kittybot.telegram.classes.media.PaidMediaVideo
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.media.PaidMediaPreview
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.bezsahara.kittybot.telegram.classes.media.PaidMediaPhoto
import kotlinx.serialization.Serializable


@Serializable(with = PaidMediaSerializer::class)
sealed interface PaidMedia {
    val type: String
}


private object PaidMediaSerializer : JsonContentPolymorphicSerializer<PaidMedia>(PaidMedia::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<PaidMedia> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "live_photo" -> PaidMediaLivePhoto.serializer()
            "photo" -> PaidMediaPhoto.serializer()
            "preview" -> PaidMediaPreview.serializer()
            "video" -> PaidMediaVideo.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



