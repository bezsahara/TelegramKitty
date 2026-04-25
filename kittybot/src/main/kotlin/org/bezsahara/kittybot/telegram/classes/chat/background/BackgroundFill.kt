package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundFillGradient
import org.bezsahara.kittybot.telegram.values.BackgroundFillType
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundFillFreeformGradient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundFill
import org.bezsahara.kittybot.telegram.classes.chat.background.BackgroundFillSolid


@Serializable(with = BackgroundFillSerializer::class)
sealed interface BackgroundFill {
    val type: BackgroundFillType
}


private object BackgroundFillSerializer : JsonContentPolymorphicSerializer<BackgroundFill>(BackgroundFill::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<BackgroundFill> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "solid" -> BackgroundFillSolid.serializer()
            "gradient" -> BackgroundFillGradient.serializer()
            "freeform_gradient" -> BackgroundFillFreeformGradient.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



