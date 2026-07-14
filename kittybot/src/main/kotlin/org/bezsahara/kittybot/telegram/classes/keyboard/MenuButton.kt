package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = MenuButtonSerializer::class)
sealed interface MenuButton {
    val type: String
}


private object MenuButtonSerializer : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<MenuButton> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "commands" -> MenuButtonCommands.serializer()
            "web_app" -> MenuButtonWebApp.serializer()
            "default" -> MenuButtonDefault.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



