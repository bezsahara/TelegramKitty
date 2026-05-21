package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask


internal object RevenueWithdrawalStateSerializer : KSerializer<RevenueWithdrawalState> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RevenueWithdrawalState") {
        element<String>("type")
        element<Long>("date", isOptional = true)
        element<String>("url", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: RevenueWithdrawalState) {
        when (value) {
            is RevenueWithdrawalStatePending -> RevenueWithdrawalStatePending.serializer().serialize(encoder, value)
            is RevenueWithdrawalStateSucceeded -> RevenueWithdrawalStateSucceeded.serializer().serialize(encoder, value)
            is RevenueWithdrawalStateFailed -> RevenueWithdrawalStateFailed.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): RevenueWithdrawalState {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var date: Long = 0L
        var url: String? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> {
                    date = dec.decodeLongElement(descriptor, 1)
                    nativeMask = nativeMask.setBit(0)
                }
                2 -> url = dec.decodeStringElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing RevenueWithdrawalState")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "pending" -> RevenueWithdrawalStatePending
            "succeeded" -> RevenueWithdrawalStateSucceeded(
                date = if (nativeMask.has(0)) date else throwMissingField("date"),
                url = url ?: throwMissingField("url")
            )
            "failed" -> RevenueWithdrawalStateFailed
            else -> throw SerializationException("Serializer wasn't found for RevenueWithdrawalState with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing RevenueWithdrawalState")
    }
}
