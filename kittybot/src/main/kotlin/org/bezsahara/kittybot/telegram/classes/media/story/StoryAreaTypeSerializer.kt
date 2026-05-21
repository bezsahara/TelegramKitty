package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType


internal object StoryAreaTypeSerializer : KSerializer<StoryAreaType> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("StoryAreaType") {
        element<String>("type")
        element<Double>("latitude", isOptional = true)
        element<Double>("longitude", isOptional = true)
        element<LocationAddress?>("address", isOptional = true)
        element<ReactionType>("reaction_type", isOptional = true)
        element<Boolean?>("is_dark", isOptional = true)
        element<Boolean?>("is_flipped", isOptional = true)
        element<String>("url", isOptional = true)
        element<Double>("temperature", isOptional = true)
        element<String>("emoji", isOptional = true)
        element<Long>("background_color", isOptional = true)
        element<String>("name", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: StoryAreaType) {
        when (value) {
            is StoryAreaTypeLocation -> StoryAreaTypeLocation.serializer().serialize(encoder, value)
            is StoryAreaTypeSuggestedReaction -> StoryAreaTypeSuggestedReaction.serializer().serialize(encoder, value)
            is StoryAreaTypeLink -> StoryAreaTypeLink.serializer().serialize(encoder, value)
            is StoryAreaTypeWeather -> StoryAreaTypeWeather.serializer().serialize(encoder, value)
            is StoryAreaTypeUniqueGift -> StoryAreaTypeUniqueGift.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): StoryAreaType {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var address: LocationAddress? = null
        var reactionType: ReactionType? = null
        var isDark: Boolean? = null
        var isFlipped: Boolean? = null
        var url: String? = null
        var temperature: Double = 0.0
        var emoji: String? = null
        var backgroundColor: Long = 0L
        var name: String? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> {
                    latitude = dec.decodeDoubleElement(descriptor, 1)
                    nativeMask = nativeMask.setBit(0)
                }
                2 -> {
                    longitude = dec.decodeDoubleElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(1)
                }
                3 -> address = dec.decodeNullableSerializableElement(descriptor, 3, LocationAddress.serializer(), null)
                4 -> reactionType = dec.decodeSerializableElement(descriptor, 4, ReactionType.serializer())
                5 -> isDark = dec.decodeNullableSerializableElement(descriptor, 5, Boolean.serializer(), null)
                6 -> isFlipped = dec.decodeNullableSerializableElement(descriptor, 6, Boolean.serializer(), null)
                7 -> url = dec.decodeStringElement(descriptor, 7)
                8 -> {
                    temperature = dec.decodeDoubleElement(descriptor, 8)
                    nativeMask = nativeMask.setBit(2)
                }
                9 -> emoji = dec.decodeStringElement(descriptor, 9)
                10 -> {
                    backgroundColor = dec.decodeLongElement(descriptor, 10)
                    nativeMask = nativeMask.setBit(3)
                }
                11 -> name = dec.decodeStringElement(descriptor, 11)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing StoryAreaType")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "location" -> StoryAreaTypeLocation(
                latitude = if (nativeMask.has(0)) latitude else throwMissingField("latitude"),
                longitude = if (nativeMask.has(1)) longitude else throwMissingField("longitude"),
                address = address
            )
            "suggested_reaction" -> StoryAreaTypeSuggestedReaction(
                reactionType = reactionType ?: throwMissingField("reaction_type"),
                isDark = isDark,
                isFlipped = isFlipped
            )
            "link" -> StoryAreaTypeLink(
                url = url ?: throwMissingField("url")
            )
            "weather" -> StoryAreaTypeWeather(
                temperature = if (nativeMask.has(2)) temperature else throwMissingField("temperature"),
                emoji = emoji ?: throwMissingField("emoji"),
                backgroundColor = if (nativeMask.has(3)) backgroundColor else throwMissingField("background_color")
            )
            "unique_gift" -> StoryAreaTypeUniqueGift(
                name = name ?: throwMissingField("name")
            )
            else -> throw SerializationException("Serializer wasn't found for StoryAreaType with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing StoryAreaType")
    }
}
