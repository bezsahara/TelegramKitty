package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.bot.json.SealedJsonElementSerializer

object InputMessageContentSerializer : SealedJsonElementSerializer<InputMessageContent>(InputMessageContent::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<InputMessageContent> {
        val jsonObject = element.jsonObject

        if (jsonObject.containsKey("address"))
            return InputVenueMessageContent.serializer()

        if (jsonObject.containsKey("messageText"))
            return InputTextMessageContent.serializer()

        if (jsonObject.containsKey("latitude"))
            return InputLocationMessageContent.serializer()

        if (jsonObject.containsKey("description"))
            return InputInvoiceMessageContent.serializer()

        if (jsonObject.containsKey("phoneNumber")) {
            return InputContactMessageContent.serializer()
        }

        error("?")
    }
}