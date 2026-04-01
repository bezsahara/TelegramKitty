package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonUnquotedLiteral

abstract class PureJsonSerializer<T>(name: String, private val obj: T, private val jsonObj: JsonObject) : KSerializer<T> {
    @OptIn(ExperimentalSerializationApi::class)
    private val jsonStr = JsonUnquotedLiteral(jsonObj.toString())
    final override val descriptor: SerialDescriptor = jsonObj.toDescriptor(name)

    final override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        (encoder as JsonEncoder).encodeJsonElement(jsonStr)
    }

    final override fun deserialize(decoder: Decoder): T {
        if ((decoder as JsonDecoder).decodeJsonElement() != jsonObj) {
            error("Unexpected json element")
        }
        return obj
    }
}