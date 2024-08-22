package org.bezsahara.kittybot.telegram.classes.menus


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(MenuButtonSerialization::class)
sealed interface MenuButton {
    val type: String
}

internal object MenuButtonSerialization : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MenuButton> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "commands" -> MenuButtonCommands.serializer()
            "web_app" -> MenuButtonWebApp.serializer()
            "default" -> MenuButtonDefault.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


