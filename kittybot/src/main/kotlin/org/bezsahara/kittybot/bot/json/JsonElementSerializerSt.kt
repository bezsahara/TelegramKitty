package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
abstract class SealedJsonElementSerializer<T: Any>(baseClass: Class<T>) : KSerializer<T> {
    constructor(baseClass: KClass<T>) : this(baseClass.java)

    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("SealedJsonElementSerializer<${baseClass.simpleName}>", PolymorphicKind.SEALED)

    private val map = (baseClass.permittedSubclasses ?: error("Not a sealed class: ${baseClass.name}")).associateWithTo(
        hashMapOf()
    ) { it.kotlin.serializer() as KSerializer<Any> }

    override fun serialize(encoder: Encoder, value: T) {
        val sr = map[value.javaClass] ?: throw SerializationException("Serializer not found for ${value.javaClass.name}")
        sr.serialize(encoder, value)
    }

    final override fun deserialize(decoder: Decoder): T {
        val input = decoder as JsonDecoder
        val tree = input.decodeJsonElement()

        @Suppress("UNCHECKED_CAST")
        val actualSerializer = selectDeserializer(tree) as KSerializer<T>
        return input.json.decodeFromJsonElement(actualSerializer, tree)
    }

    protected abstract fun selectDeserializer(element: JsonElement): DeserializationStrategy<T>
}
