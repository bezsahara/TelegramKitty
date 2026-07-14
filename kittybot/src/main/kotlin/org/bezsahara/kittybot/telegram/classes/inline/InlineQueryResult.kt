package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = InlineQueryResultSerializer::class)
sealed interface InlineQueryResult {
    val type: String
}


private object InlineQueryResultSerializer : JsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<InlineQueryResult> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "audio" -> InlineQueryResultAudio.serializer()
            "document" -> InlineQueryResultDocument.serializer()
            "gif" -> InlineQueryResultGif.serializer()
            "mpeg4_gif" -> InlineQueryResultMpeg4Gif.serializer()
            "photo" -> InlineQueryResultPhoto.serializer()
            "sticker" -> InlineQueryResultCachedSticker.serializer()
            "video" -> InlineQueryResultVideo.serializer()
            "voice" -> InlineQueryResultVoice.serializer()
            "article" -> InlineQueryResultArticle.serializer()
            "contact" -> InlineQueryResultContact.serializer()
            "game" -> InlineQueryResultGame.serializer()
            "location" -> InlineQueryResultLocation.serializer()
            "venue" -> InlineQueryResultVenue.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



