package org.bezsahara.kittybot.telegram.classes.chat.background

import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundTypeChatTheme
import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundType
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundTypeFill
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundTypeWallpaper
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundTypePattern
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = BackgroundTypeSerializer::class)
sealed interface BackgroundType {
    val type: String
}


private object BackgroundTypeSerializer : JsonContentPolymorphicSerializer<BackgroundType>(BackgroundType::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<BackgroundType> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "fill" -> BackgroundTypeFill.serializer()
            "wallpaper" -> BackgroundTypeWallpaper.serializer()
            "pattern" -> BackgroundTypePattern.serializer()
            "chat_theme" -> BackgroundTypeChatTheme.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



