package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultMpeg4Gif
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultGif
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultPhoto
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultContact
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultCachedSticker
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultDocument
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultVoice
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultArticle
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultGame
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultVideo
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultVenue
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultLocation
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResult
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultAudio


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



