@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.telegram.classes.core.update

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.business.BusinessMessagesDeleted
import org.bezsahara.kittybot.telegram.classes.chat.ChatJoinRequest
import org.bezsahara.kittybot.telegram.classes.chat.ChatMemberUpdated
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostRemoved
import org.bezsahara.kittybot.telegram.classes.chat.boosts.ChatBoostUpdated
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.inline.ChosenInlineResult
import org.bezsahara.kittybot.telegram.classes.inline.InlineQuery
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.message.polls.PollAnswer
import org.bezsahara.kittybot.telegram.classes.message.reactions.MessageReactionCountUpdated
import org.bezsahara.kittybot.telegram.classes.message.reactions.MessageReactionUpdated
import org.bezsahara.kittybot.telegram.classes.payments.PaidMediaPurchased
import org.bezsahara.kittybot.telegram.classes.payments.PreCheckoutQuery
import org.bezsahara.kittybot.telegram.classes.payments.ShippingQuery

internal object UpdateSerializer : KSerializer<Update> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Update") {
        element<Long>("update_id") // 0
        element<Message?>("message", isOptional = true) // 1
        element<Message?>("edited_message", isOptional = true) // 2
        element<Message?>("channel_post", isOptional = true) // 3
        element<Message?>("edited_channel_post", isOptional = true) // 4
        element<BusinessConnection?>("business_connection", isOptional = true) // 5
        element<Message?>("business_message", isOptional = true) // 6
        element<Message?>("edited_business_message", isOptional = true) // 7
        element<BusinessMessagesDeleted?>("deleted_business_messages", isOptional = true) // 8
        element<MessageReactionUpdated?>("message_reaction", isOptional = true) // 9
        element<MessageReactionCountUpdated?>("message_reaction_count", isOptional = true) // 10
        element<InlineQuery?>("inline_query", isOptional = true) // 11
        element<ChosenInlineResult?>("chosen_inline_result", isOptional = true) // 12
        element<CallbackQuery?>("callback_query", isOptional = true) // 13
        element<ShippingQuery?>("shipping_query", isOptional = true) // 14
        element<PreCheckoutQuery?>("pre_checkout_query", isOptional = true) // 15
        element<PaidMediaPurchased?>("purchased_paid_media", isOptional = true) // 16
        element<Poll?>("poll", isOptional = true) // 17
        element<PollAnswer?>("poll_answer", isOptional = true) // 18
        element<ChatMemberUpdated?>("my_chat_member", isOptional = true) // 19
        element<ChatMemberUpdated?>("chat_member", isOptional = true) // 20
        element<ChatJoinRequest?>("chat_join_request", isOptional = true) // 21
        element<ChatBoostUpdated?>("chat_boost", isOptional = true) // 22
        element<ChatBoostRemoved?>("removed_chat_boost", isOptional = true) // 23
    }

    override fun deserialize(decoder: Decoder): Update {
        val structure = decoder.beginStructure(descriptor)

        var updateId: Long = Long.MIN_VALUE
        var value: UpdateLambda? = null

        while (true) {
            when (val elIndex = structure.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break

                0 -> {
                    updateId = structure.decodeLongElement(descriptor, 0)
                    if (value != null) {
                        structure.endStructure(descriptor)
                        return value.create(updateId)
                    }
                }

                1 -> {
                    val message = structure.decodeSerializableElement(descriptor, 1, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { MessageUpdate(it, message) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return MessageUpdate(updateId, message)
                }

                2 -> {
                    val editedMessage = structure.decodeSerializableElement(descriptor, 2, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { EditedMessageUpdate(it, editedMessage) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return EditedMessageUpdate(updateId, editedMessage)
                }

                3 -> {
                    val channelPost = structure.decodeSerializableElement(descriptor, 3, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ChannelPostUpdate(it, channelPost) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ChannelPostUpdate(updateId, channelPost)
                }

                4 -> {
                    val editedChannelPost = structure.decodeSerializableElement(descriptor, 4, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { EditedChannelPostUpdate(it, editedChannelPost) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return EditedChannelPostUpdate(updateId, editedChannelPost)
                }

                5 -> {
                    val businessConnection = structure.decodeSerializableElement(descriptor, 5, BusinessConnection.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { BusinessConnectionUpdate(it, businessConnection) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return BusinessConnectionUpdate(updateId, businessConnection)
                }

                6 -> {
                    val businessMessage = structure.decodeSerializableElement(descriptor, 6, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { BusinessMessageUpdate(it, businessMessage) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return BusinessMessageUpdate(updateId, businessMessage)
                }

                7 -> {
                    val editedBusinessMessage = structure.decodeSerializableElement(descriptor, 7, Message.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { EditedBusinessMessageUpdate(it, editedBusinessMessage) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return EditedBusinessMessageUpdate(updateId, editedBusinessMessage)
                }

                8 -> {
                    val deletedBusinessMessages = structure.decodeSerializableElement(descriptor, 8, BusinessMessagesDeleted.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { DeletedBusinessMessagesUpdate(it, deletedBusinessMessages) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return DeletedBusinessMessagesUpdate(updateId, deletedBusinessMessages)
                }

                9 -> {
                    val messageReaction = structure.decodeSerializableElement(descriptor, 9, MessageReactionUpdated.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { MessageReactionUpdate(it, messageReaction) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return MessageReactionUpdate(updateId, messageReaction)
                }

                10 -> {
                    val messageReactionCount = structure.decodeSerializableElement(descriptor, 10, MessageReactionCountUpdated.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { MessageReactionCountUpdate(it, messageReactionCount) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return MessageReactionCountUpdate(updateId, messageReactionCount)
                }

                11 -> {
                    val inlineQuery = structure.decodeSerializableElement(descriptor, 11, InlineQuery.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { InlineQueryUpdate(it, inlineQuery) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return InlineQueryUpdate(updateId, inlineQuery)
                }

                12 -> {
                    val chosenInlineResult = structure.decodeSerializableElement(descriptor, 12, ChosenInlineResult.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ChosenInlineResultUpdate(it, chosenInlineResult) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ChosenInlineResultUpdate(updateId, chosenInlineResult)
                }

                13 -> {
                    val callbackQuery = structure.decodeSerializableElement(descriptor, 13, CallbackQuery.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { CallbackQueryUpdate(it, callbackQuery) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return CallbackQueryUpdate(updateId, callbackQuery)
                }

                14 -> {
                    val shippingQuery = structure.decodeSerializableElement(descriptor, 14, ShippingQuery.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ShippingQueryUpdate(it, shippingQuery) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ShippingQueryUpdate(updateId, shippingQuery)
                }

                15 -> {
                    val preCheckoutQuery = structure.decodeSerializableElement(descriptor, 15, PreCheckoutQuery.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { PreCheckoutQueryUpdate(it, preCheckoutQuery) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return PreCheckoutQueryUpdate(updateId, preCheckoutQuery)
                }

                16 -> {
                    val purchasedPaidMedia = structure.decodeSerializableElement(descriptor, 16, PaidMediaPurchased.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { PaidMediaPurchasedUpdate(it, purchasedPaidMedia) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return PaidMediaPurchasedUpdate(updateId, purchasedPaidMedia)
                }

                17 -> {
                    val poll = structure.decodeSerializableElement(descriptor, 17, Poll.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { PollUpdate(it, poll) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return PollUpdate(updateId, poll)
                }

                18 -> {
                    val pollAnswer = structure.decodeSerializableElement(descriptor, 18, PollAnswer.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { PollAnswerUpdate(it, pollAnswer) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return PollAnswerUpdate(updateId, pollAnswer)
                }

                19 -> {
                    val myChatMember = structure.decodeSerializableElement(descriptor, 19, ChatMemberUpdated.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { MyChatMemberUpdate(it, myChatMember) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return MyChatMemberUpdate(updateId, myChatMember)
                }

                20 -> {
                    val chatMember = structure.decodeSerializableElement(descriptor, 20, ChatMemberUpdated.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ChatMemberUpdate(it, chatMember) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ChatMemberUpdate(updateId, chatMember)
                }

                21 -> {
                    val chatJoinRequest = structure.decodeSerializableElement(descriptor, 21, ChatJoinRequest.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ChatJoinRequestUpdate(it, chatJoinRequest) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ChatJoinRequestUpdate(updateId, chatJoinRequest)
                }

                22 -> {
                    val chatBoost = structure.decodeSerializableElement(descriptor, 22, ChatBoostUpdated.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { ChatBoostUpdate(it, chatBoost) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return ChatBoostUpdate(updateId, chatBoost)
                }

                23 -> {
                    val removedChatBoost = structure.decodeSerializableElement(descriptor, 23, ChatBoostRemoved.serializer())
                    if (updateId == Long.MIN_VALUE) {
                        value = UpdateLambda { RemovedChatBoostUpdate(it, removedChatBoost) }
                        continue
                    }
                    structure.endStructure(descriptor)
                    return RemovedChatBoostUpdate(updateId, removedChatBoost)
                }

                else -> error("Unexpected index: $elIndex")
            }
        }

        structure.endStructure(descriptor)
        error("Update serializer did not serialize properly! No valid data found for some reason")
    }

    override fun serialize(encoder: Encoder, value: Update) {
        error("Serialization is not supported")
    }
}
