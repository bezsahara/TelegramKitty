package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(InlineQueryResultSerialization::class)
sealed interface InlineQueryResult {
    val type: String
}

internal object InlineQueryResultSerialization : JsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<InlineQueryResult> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "article" -> InlineQueryResultArticle.serializer()
            "photo" -> InlineQueryResultPhoto.serializer()
            "gif" -> InlineQueryResultGif.serializer()
            "mpeg4_gif" -> InlineQueryResultMpeg4Gif.serializer()
            "video" -> InlineQueryResultVideo.serializer()
            "audio" -> InlineQueryResultAudio.serializer()
            "voice" -> InlineQueryResultVoice.serializer()
            "document" -> InlineQueryResultDocument.serializer()
            "location" -> InlineQueryResultLocation.serializer()
            "venue" -> InlineQueryResultVenue.serializer()
            "contact" -> InlineQueryResultContact.serializer()
            "game" -> InlineQueryResultGame.serializer()
            "sticker" -> InlineQueryResultCachedSticker.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


