package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.gifts.Gift
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.values.TransactionType


internal object TransactionPartnerSerializer : KSerializer<TransactionPartner> {
    private val listSerializer0 = ListSerializer(PaidMedia.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TransactionPartner") {
        element<String>("type")
        element<TransactionType>("transaction_type", isOptional = true)
        element<User>("user", isOptional = true)
        element<AffiliateInfo?>("affiliate", isOptional = true)
        element<String?>("invoice_payload", isOptional = true)
        element<Long?>("subscription_period", isOptional = true)
        element<List<PaidMedia>?>("paid_media", isOptional = true)
        element<String?>("paid_media_payload", isOptional = true)
        element<Gift?>("gift", isOptional = true)
        element<Long?>("premium_subscription_duration", isOptional = true)
        element<Chat>("chat", isOptional = true)
        element<User?>("sponsor_user", isOptional = true)
        element<Long>("commission_per_mille", isOptional = true)
        element<RevenueWithdrawalState?>("withdrawal_state", isOptional = true)
        element<Long>("request_count", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: TransactionPartner) {
        when (value) {
            is TransactionPartnerUser -> TransactionPartnerUser.serializer().serialize(encoder, value)
            is TransactionPartnerChat -> TransactionPartnerChat.serializer().serialize(encoder, value)
            is TransactionPartnerAffiliateProgram -> TransactionPartnerAffiliateProgram.serializer().serialize(encoder, value)
            is TransactionPartnerFragment -> TransactionPartnerFragment.serializer().serialize(encoder, value)
            is TransactionPartnerTelegramAds -> TransactionPartnerTelegramAds.serializer().serialize(encoder, value)
            is TransactionPartnerTelegramApi -> TransactionPartnerTelegramApi.serializer().serialize(encoder, value)
            is TransactionPartnerOther -> TransactionPartnerOther.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): TransactionPartner {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var transactionType: TransactionType? = null
        var user: User? = null
        var affiliate: AffiliateInfo? = null
        var invoicePayload: String? = null
        var subscriptionPeriod: Long? = null
        var paidMedia: List<PaidMedia>? = null
        var paidMediaPayload: String? = null
        var gift: Gift? = null
        var premiumSubscriptionDuration: Long? = null
        var chat: Chat? = null
        var sponsorUser: User? = null
        var commissionPerMille: Long = 0L
        var withdrawalState: RevenueWithdrawalState? = null
        var requestCount: Long = 0L
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> transactionType = dec.decodeSerializableElement(descriptor, 1, TransactionType.serializer())
                2 -> user = dec.decodeSerializableElement(descriptor, 2, User.serializer())
                3 -> affiliate = dec.decodeNullableSerializableElement(descriptor, 3, AffiliateInfo.serializer(), null)
                4 -> invoicePayload = dec.decodeNullableSerializableElement(descriptor, 4, String.serializer(), null)
                5 -> subscriptionPeriod = dec.decodeNullableSerializableElement(descriptor, 5, Long.serializer(), null)
                6 -> paidMedia = dec.decodeNullableSerializableElement(descriptor, 6, listSerializer0, null)
                7 -> paidMediaPayload = dec.decodeNullableSerializableElement(descriptor, 7, String.serializer(), null)
                8 -> gift = dec.decodeNullableSerializableElement(descriptor, 8, Gift.serializer(), null)
                9 -> premiumSubscriptionDuration = dec.decodeNullableSerializableElement(descriptor, 9, Long.serializer(), null)
                10 -> chat = dec.decodeSerializableElement(descriptor, 10, Chat.serializer())
                11 -> sponsorUser = dec.decodeNullableSerializableElement(descriptor, 11, User.serializer(), null)
                12 -> {
                    commissionPerMille = dec.decodeLongElement(descriptor, 12)
                    nativeMask = nativeMask.setBit(0)
                }
                13 -> withdrawalState = dec.decodeNullableSerializableElement(descriptor, 13, RevenueWithdrawalState.serializer(), null)
                14 -> {
                    requestCount = dec.decodeLongElement(descriptor, 14)
                    nativeMask = nativeMask.setBit(1)
                }
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing TransactionPartner")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "user" -> TransactionPartnerUser(
                transactionType = transactionType ?: throwMissingField("transaction_type"),
                user = user ?: throwMissingField("user"),
                affiliate = affiliate,
                invoicePayload = invoicePayload,
                subscriptionPeriod = subscriptionPeriod,
                paidMedia = paidMedia,
                paidMediaPayload = paidMediaPayload,
                gift = gift,
                premiumSubscriptionDuration = premiumSubscriptionDuration
            )
            "chat" -> TransactionPartnerChat(
                chat = chat ?: throwMissingField("chat"),
                gift = gift
            )
            "affiliate_program" -> TransactionPartnerAffiliateProgram(
                sponsorUser = sponsorUser,
                commissionPerMille = if (nativeMask.has(0)) commissionPerMille else throwMissingField("commission_per_mille")
            )
            "fragment" -> TransactionPartnerFragment(
                withdrawalState = withdrawalState
            )
            "telegram_ads" -> TransactionPartnerTelegramAds
            "telegram_api" -> TransactionPartnerTelegramApi(
                requestCount = if (nativeMask.has(1)) requestCount else throwMissingField("request_count")
            )
            "other" -> TransactionPartnerOther
            else -> throw SerializationException("Serializer wasn't found for TransactionPartner with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing TransactionPartner")
    }
}
