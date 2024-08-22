@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.telegram.classes.updates

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.business.BusinessMessagesDeleted
import org.bezsahara.kittybot.telegram.classes.chats.boosts.ChatBoostRemoved
import org.bezsahara.kittybot.telegram.classes.chats.boosts.ChatBoostUpdated
import org.bezsahara.kittybot.telegram.classes.chats.members.ChatMemberUpdated
import org.bezsahara.kittybot.telegram.classes.chats.requests.ChatJoinRequest
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.messages.reactions.MessageReactionCountUpdated
import org.bezsahara.kittybot.telegram.classes.messages.reactions.MessageReactionUpdated
import org.bezsahara.kittybot.telegram.classes.payments.PreCheckoutQuery
import org.bezsahara.kittybot.telegram.classes.payments.ShippingQuery
import org.bezsahara.kittybot.telegram.classes.polls.Poll
import org.bezsahara.kittybot.telegram.classes.polls.PollAnswer
import org.bezsahara.kittybot.telegram.classes.queries.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.queries.InlineQuery
import org.bezsahara.kittybot.telegram.classes.queries.results.ChosenInlineResult

internal object UpdateSerializer : KSerializer<Update> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Update") {
        element<Long>("update_id")
        element<Message?>("message", isOptional = true)
        element<Message?>("edited_message", isOptional = true)
        element<Message?>("channel_post", isOptional = true)
        element<Message?>("edited_channel_post", isOptional = true)
        element<BusinessConnection?>("business_connection", isOptional = true)
        element<Message?>("business_message", isOptional = true)
        element<Message?>("edited_business_message", isOptional = true)
        element<BusinessMessagesDeleted?>("deleted_business_messages", isOptional = true)
        element<MessageReactionUpdated?>("message_reaction", isOptional = true)
        element<MessageReactionCountUpdated?>("message_reaction_count", isOptional = true)
        element<InlineQuery?>("inline_query", isOptional = true)
        element<ChosenInlineResult?>("chosen_inline_result", isOptional = true)
        element<CallbackQuery?>("callback_query", isOptional = true)
        element<ShippingQuery?>("shipping_query", isOptional = true)
        element<PreCheckoutQuery?>("pre_checkout_query", isOptional = true)
        element<Poll?>("poll", isOptional = true)
        element<PollAnswer?>("poll_answer", isOptional = true)
        element<ChatMemberUpdated?>("my_chat_member", isOptional = true)
        element<ChatMemberUpdated?>("chat_member", isOptional = true)
        element<ChatJoinRequest?>("chat_join_request", isOptional = true)
        element<ChatBoostUpdated?>("chat_boost", isOptional = true)
        element<ChatBoostRemoved?>("removed_chat_boost", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): Update {
        val structure = decoder.beginStructure(descriptor)

        var updateId: Long = 0

        while (true) {
            when (val elIndex = structure.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> updateId = structure.decodeLongElement(descriptor, 0)
                1 -> {
                    val message = structure.decodeSerializableElement(descriptor, 1, Message.serializer())
                    structure.endStructure(descriptor)
                    return MessageUpdate(updateId, message)
                }
                2 -> {
                    val editedMessage = structure.decodeSerializableElement(descriptor, 2, Message.serializer())
                    structure.endStructure(descriptor)
                    return EditedMessageUpdate(updateId, editedMessage)
                }
                3 -> {
                    val channelPost = structure.decodeSerializableElement(descriptor, 3, Message.serializer())
                    structure.endStructure(descriptor)
                    return ChannelPostUpdate(updateId, channelPost)
                }
                4 -> {
                    val editedChannelPost = structure.decodeSerializableElement(descriptor, 4, Message.serializer())
                    structure.endStructure(descriptor)
                    return EditedChannelPostUpdate(updateId, editedChannelPost)
                }
                5 -> {
                    val businessConnection = structure.decodeSerializableElement(descriptor, 5, BusinessConnection.serializer())
                    structure.endStructure(descriptor)
                    return BusinessConnectionUpdate(updateId, businessConnection)
                }
                6 -> {
                    val businessMessage = structure.decodeSerializableElement(descriptor, 6, Message.serializer())
                    structure.endStructure(descriptor)
                    return BusinessMessageUpdate(updateId, businessMessage)
                }
                7 -> {
                    val editedBusinessMessage = structure.decodeSerializableElement(descriptor, 7, Message.serializer())
                    structure.endStructure(descriptor)
                    return EditedBusinessMessageUpdate(updateId, editedBusinessMessage)
                }
                8 -> {
                    val deletedBusinessMessages = structure.decodeSerializableElement(descriptor, 8, BusinessMessagesDeleted.serializer())
                    structure.endStructure(descriptor)
                    return DeletedBusinessMessagesUpdate(updateId, deletedBusinessMessages)
                }
                9 -> {
                    val messageReaction = structure.decodeSerializableElement(descriptor, 9, MessageReactionUpdated.serializer())
                    structure.endStructure(descriptor)
                    return MessageReactionUpdate(updateId, messageReaction)
                }
                10 -> {
                    val messageReactionCount = structure.decodeSerializableElement(descriptor, 10, MessageReactionCountUpdated.serializer())
                    structure.endStructure(descriptor)
                    return MessageReactionCountUpdate(updateId, messageReactionCount)
                }
                11 -> {
                    val inlineQuery = structure.decodeSerializableElement(descriptor, 11, InlineQuery.serializer())
                    structure.endStructure(descriptor)
                    return InlineQueryUpdate(updateId, inlineQuery)
                }
                12 -> {
                    val chosenInlineResult = structure.decodeSerializableElement(descriptor, 12, ChosenInlineResult.serializer())
                    structure.endStructure(descriptor)
                    return ChosenInlineResultUpdate(updateId, chosenInlineResult)
                }
                13 -> {
                    val callbackQuery = structure.decodeSerializableElement(descriptor, 13, CallbackQuery.serializer())
                    structure.endStructure(descriptor)
                    return CallbackQueryUpdate(updateId, callbackQuery)
                }
                14 -> {
                    val shippingQuery = structure.decodeSerializableElement(descriptor, 14, ShippingQuery.serializer())
                    structure.endStructure(descriptor)
                    return ShippingQueryUpdate(updateId, shippingQuery)
                }
                15 -> {
                    val preCheckoutQuery = structure.decodeSerializableElement(descriptor, 15, PreCheckoutQuery.serializer())
                    structure.endStructure(descriptor)
                    return PreCheckoutQueryUpdate(updateId, preCheckoutQuery)
                }
                16 -> {
                    val poll = structure.decodeSerializableElement(descriptor, 16, Poll.serializer())
                    structure.endStructure(descriptor)
                    return PollUpdate(updateId, poll)
                }
                17 -> {
                    val pollAnswer = structure.decodeSerializableElement(descriptor, 17, PollAnswer.serializer())
                    structure.endStructure(descriptor)
                    return PollAnswerUpdate(updateId, pollAnswer)
                }
                18 -> {
                    val myChatMember = structure.decodeSerializableElement(descriptor, 18, ChatMemberUpdated.serializer())
                    structure.endStructure(descriptor)
                    return MyChatMemberUpdate(updateId, myChatMember)
                }
                19 -> {
                    val chatMember = structure.decodeSerializableElement(descriptor, 19, ChatMemberUpdated.serializer())
                    structure.endStructure(descriptor)
                    return ChatMemberUpdate(updateId, chatMember)
                }
                20 -> {
                    val chatJoinRequest = structure.decodeSerializableElement(descriptor, 20, ChatJoinRequest.serializer())
                    structure.endStructure(descriptor)
                    return ChatJoinRequestUpdate(updateId, chatJoinRequest)
                }
                21 -> {
                    val chatBoost = structure.decodeSerializableElement(descriptor, 21, ChatBoostUpdated.serializer())
                    structure.endStructure(descriptor)
                    return ChatBoostUpdate(updateId, chatBoost)
                }
                22 -> {
                    val removedChatBoost = structure.decodeSerializableElement(descriptor, 22, ChatBoostRemoved.serializer())
                    structure.endStructure(descriptor)
                    return RemovedChatBoostUpdate(updateId, removedChatBoost)
                }
                else -> error("Unexpected index: $elIndex")
            }
        }

        structure.endStructure(descriptor)
        error("No valid data found")
    }

    override fun serialize(encoder: Encoder, value: Update) {
        error("Serialization is not supported")
    }
}
