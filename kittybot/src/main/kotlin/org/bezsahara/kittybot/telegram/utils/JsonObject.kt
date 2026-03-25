package org.bezsahara.kittybot.telegram.utils

//@Serializable(with = JsonObjectSerializer::class)
//abstract class JsonObject {
//    abstract fun json(): String
//}
//
//
//private object JsonObjectSerializer : KSerializer<JsonObject> {
//    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor
//
//    override fun deserialize(decoder: Decoder): JsonObject {
//        throw NotImplementedError("Not supported")
//    }
//
//    @OptIn(ExperimentalSerializationApi::class)
//    override fun serialize(
//        encoder: Encoder,
//        value: JsonObject,
//    ) {
//        (encoder as JsonEncoder).encodeJsonElement(JsonUnquotedLiteral(value.json()))
//    }
//}