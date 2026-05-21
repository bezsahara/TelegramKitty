package org.bezsahara.kittybot.telegram.classes.chat.boosts

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
import org.bezsahara.kittybot.telegram.classes.user.User


internal object ChatBoostSourceSerializer : KSerializer<ChatBoostSource> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ChatBoostSource") {
        element<String>("source")
        element<User?>("user", isOptional = true)
        element<Long>("giveaway_message_id", isOptional = true)
        element<Long?>("prize_star_count", isOptional = true)
        element<Boolean?>("is_unclaimed", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ChatBoostSource) {
        when (value) {
            is ChatBoostSourcePremium -> ChatBoostSourcePremium.serializer().serialize(encoder, value)
            is ChatBoostSourceGiftCode -> ChatBoostSourceGiftCode.serializer().serialize(encoder, value)
            is ChatBoostSourceGiveaway -> ChatBoostSourceGiveaway.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): ChatBoostSource {
        var nativeMask = IntMask.EMPTY
        var source: String? = null
        var user: User? = null
        var giveawayMessageId: Long = 0L
        var prizeStarCount: Long? = null
        var isUnclaimed: Boolean? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> source = dec.decodeStringElement(descriptor, 0)
                1 -> user = dec.decodeNullableSerializableElement(descriptor, 1, User.serializer(), null)
                2 -> {
                    giveawayMessageId = dec.decodeLongElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(0)
                }
                3 -> prizeStarCount = dec.decodeNullableSerializableElement(descriptor, 3, Long.serializer(), null)
                4 -> isUnclaimed = dec.decodeNullableSerializableElement(descriptor, 4, Boolean.serializer(), null)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing ChatBoostSource")
            }
        }
        dec.endStructure(descriptor)
        return when (source ?: throwMissingField("source")) {
            "premium" -> ChatBoostSourcePremium(
                user = user ?: throwMissingField("user")
            )
            "gift_code" -> ChatBoostSourceGiftCode(
                user = user ?: throwMissingField("user")
            )
            "giveaway" -> ChatBoostSourceGiveaway(
                giveawayMessageId = if (nativeMask.has(0)) giveawayMessageId else throwMissingField("giveaway_message_id"),
                user = user,
                prizeStarCount = prizeStarCount,
                isUnclaimed = isUnclaimed
            )
            else -> throw SerializationException("Serializer wasn't found for ChatBoostSource with source $source")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing ChatBoostSource")
    }
}
