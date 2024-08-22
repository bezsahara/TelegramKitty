package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.json.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable



@Serializable(BackgroundTypeSerialization::class)
sealed interface BackgroundType {
    val type: String
}

internal object BackgroundTypeSerialization : JsonContentPolymorphicSerializer<BackgroundType>(BackgroundType::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BackgroundType> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "fill" -> BackgroundTypeFill.serializer()
            "wallpaper" -> BackgroundTypeWallpaper.serializer()
            "pattern" -> BackgroundTypePattern.serializer()
            "chat_theme" -> BackgroundTypeChatTheme.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


