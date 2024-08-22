package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.json.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable



@Serializable(BackgroundFillSerialization::class)
sealed interface BackgroundFill {
    val type: String
}

internal object BackgroundFillSerialization : JsonContentPolymorphicSerializer<BackgroundFill>(BackgroundFill::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BackgroundFill> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "solid" -> BackgroundFillSolid.serializer()
            "gradient" -> BackgroundFillGradient.serializer()
            "freeform_gradient" -> BackgroundFillFreeformGradient.serializer()
            else -> error("No such type ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}


