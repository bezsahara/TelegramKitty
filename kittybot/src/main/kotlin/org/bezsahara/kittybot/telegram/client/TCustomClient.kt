package org.bezsahara.kittybot.telegram.client

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.classes.keyboard.PreparedKeyboardButton
import org.bezsahara.kittybot.telegram.values.StickerType
import org.bezsahara.kittybot.telegram.client.file.createBoundary
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.values.ChatAction
import org.bezsahara.kittybot.telegram.utils.TBytesInfo
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.classes.core.MessageId
import org.bezsahara.kittybot.telegram.values.DiceEmoji
import org.bezsahara.kittybot.telegram.classes.inline.PreparedInlineMessage
import org.bezsahara.kittybot.telegram.classes.payments.ShippingOption
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import org.bezsahara.kittybot.telegram.client.OkBoolOpt
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import org.bezsahara.kittybot.telegram.classes.core.WebhookInfo
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.user.UserProfilePhotos
import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResult
import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResultsButton
import org.bezsahara.kittybot.telegram.classes.message.ReplyParameters
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.polls.InputPollOption
import org.bezsahara.kittybot.bot.json.JsonByteBuffer
import org.bezsahara.kittybot.telegram.classes.input.InputChecklist
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.classes.bot.BotShortDescription
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementError
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import kotlinx.serialization.builtins.serializer
import org.bezsahara.kittybot.telegram.classes.chat.ChatPermissions
import org.bezsahara.kittybot.telegram.utils.TSerials
import org.bezsahara.kittybot.telegram.classes.chat.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.user.UserProfileAudios
import org.bezsahara.kittybot.telegram.classes.games.GameHighScore
import org.bezsahara.kittybot.telegram.classes.payments.StarAmount
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.telegram.classes.chat.boosts.UserChatBoosts
import org.bezsahara.kittybot.telegram.client.file.mpContentType
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.json.JsonObject
import org.bezsahara.kittybot.telegram.classes.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGifts
import org.bezsahara.kittybot.telegram.classes.bot.BotName
import org.bezsahara.kittybot.telegram.utils.TResultFailure
import org.bezsahara.kittybot.telegram.classes.chat.ForumTopic
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.classes.gifts.Gifts
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostParameters
import org.bezsahara.kittybot.telegram.values.PollType
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import org.bezsahara.kittybot.telegram.classes.inline.SentWebAppMessage
import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButton
import org.bezsahara.kittybot.telegram.client.TPathCustom
import org.bezsahara.kittybot.telegram.classes.payments.StarTransactions
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.chat.ChatAdministratorRights
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import org.bezsahara.kittybot.telegram.classes.bot.BotDescription
import org.bezsahara.kittybot.telegram.classes.media.stickers.StickerSet
import org.bezsahara.kittybot.telegram.values.StickerFormat
import org.bezsahara.kittybot.telegram.classes.gifts.AcceptedGiftTypes
import org.bezsahara.kittybot.telegram.utils.TResultFailureEither
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.client.opt.BufferSizePredictor
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.coroutines.withContext
import org.bezsahara.kittybot.telegram.utils.TResult.Either
import org.bezsahara.kittybot.telegram.classes.media.stickers.MaskPosition


class TCustomClient internal constructor(
    internal val tPathC: TPathCustom,
    internal val client: CustomClient,
    internal val json: Json
) : KittyBot() {
    val dispatcher = client.requestDispatcher
    override suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMessages,
            JsonByteBuffer(2163).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setChatPhoto(
        chatId: ChatId,
        photo: TelegramFile
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setChatPhoto, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        photo.asVertx().executeCustom(mpb, "photo")
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinAllGeneralForumTopicMessages,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editChatSubscriptionInviteLinkBSP = BufferSizePredictor(22, 1073741824, 44, 88)
    override suspend fun editChatSubscriptionInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editChatSubscriptionInviteLink,
            run {
                val bbSize0 = editChatSubscriptionInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                    if (name != null) putStringUnsafe(TBytesInfo.name, name)
                    editChatSubscriptionInviteLinkBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteForumTopic,
            JsonByteBuffer(88).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val readBusinessMessageBSP = BufferSizePredictor(39, 1073741824, 78, 156)
    override suspend fun readBusinessMessage(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.readBusinessMessage,
            run {
                val bbSize0 = readBusinessMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    readBusinessMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatPermissionsBSP = BufferSizePredictor(50, 1073741824, 100, 200)
    override suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatPermissions,
            run {
                val bbSize0 = setChatPermissionsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putJsonObject(TBytesInfo.permissions, ChatPermissions.serializer(), json, permissions)
                    if (useIndependentChatPermissions != null) putBoolUnsafe(TBytesInfo.use_independent_chat_permissions, useIndependentChatPermissions)
                    setChatPermissionsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setMyProfilePhoto(
        photo: InputProfilePhoto
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setMyProfilePhoto, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        photo.executeAll(mpb)
        mpb.writeJsonPart("photo", InputProfilePhoto.serializer(), photo, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editMessageReplyMarkupBSP = BufferSizePredictor(68, 1073741824, 136, 272)
    override suspend fun editMessageReplyMarkup(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editMessageReplyMarkup,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editMessageReplyMarkupBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) editMessageReplyMarkupBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.banChatMember,
            JsonByteBuffer(136).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                if (untilDate != null) putNumberUnsafe(TBytesInfo.until_date, untilDate)
                if (revokeMessages != null) putBoolUnsafe(TBytesInfo.revoke_messages, revokeMessages)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getBusinessAccountStarBalanceBSP = BufferSizePredictor(22, 1073741824, 44, 88)
    override suspend fun getBusinessAccountStarBalance(
        businessConnectionId: String
    ): TResult<StarAmount> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessAccountStarBalance,
            run {
                val bbSize0 = getBusinessAccountStarBalanceBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    getBusinessAccountStarBalanceBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StarAmount>(
                json.decodeFromString(TSerials.sStarAmount, strResult).result
            )
        } else {
            TResultFailure<StarAmount>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unhideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unhideGeneralForumTopic,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun verifyChat(
        chatId: ChatId,
        customDescription: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.verifyChat,
            JsonByteBuffer(491).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                if (customDescription != null) putStringUnsafe(TBytesInfo.custom_description, customDescription)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun editStory(
        businessConnectionId: String,
        storyId: Long,
        content: InputStoryContent,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        areas: List<StoryArea>?
    ): TResult<Story> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.editStory, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("business_connection_id", businessConnectionId)
        mpb.writeNormalPart("story_id", storyId.toString())
        content.executeAll(mpb)
        mpb.writeJsonPart("content", InputStoryContent.serializer(), content, json)
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (areas != null) mpb.writeJsonPart("areas", TSerials.aListStoryArea, areas, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Story>(
                json.decodeFromString(TSerials.sStory, strResult).result
            )
        } else {
            TResultFailure<Story>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteChatPhoto(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteChatPhoto,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val copyMessageBSP = BufferSizePredictor(273, 1073741824, 546, 1092)
    override suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        videoStartTimestamp: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<MessageId> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.copyMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: copyMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (videoStartTimestamp != null) putNumberUnsafe(TBytesInfo.video_start_timestamp, videoStartTimestamp)
                    if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                    if (parseMode != null) putStringUnsafe(TBytesInfo.parse_mode, parseMode.value)
                    if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                    if (showCaptionAboveMedia != null) putBoolUnsafe(TBytesInfo.show_caption_above_media, showCaptionAboveMedia)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) copyMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<MessageId>(
                json.decodeFromString(TSerials.sMessageId, strResult).result
            )
        } else {
            TResultFailure<MessageId>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.reopenForumTopic,
            JsonByteBuffer(88).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendDiceBSP = BufferSizePredictor(200, 1073741824, 400, 800)
    override suspend fun sendDice(
        chatId: ChatId,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        emoji: DiceEmoji?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendDice,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendDiceBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (emoji != null) putStringUnsafe(TBytesInfo.emoji, emoji.value)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendDiceBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun removeMyProfilePhoto(): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.removeMyProfilePhoto, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val refundStarPaymentBSP = BufferSizePredictor(33, 1073741824, 66, 132)
    override suspend fun refundStarPayment(
        userId: Long,
        telegramPaymentChargeId: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.refundStarPayment,
            run {
                val bbSize0 = refundStarPaymentBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putStringUnsafe(TBytesInfo.telegram_payment_charge_id, telegramPaymentChargeId)
                    refundStarPaymentBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getAvailableGifts(): TResult<Gifts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getAvailableGifts, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Gifts>(
                json.decodeFromString(TSerials.sGifts, strResult).result
            )
        } else {
            TResultFailure<Gifts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendLocationBSP = BufferSizePredictor(271, 1073741824, 542, 1084)
    override suspend fun sendLocation(
        chatId: ChatId,
        latitude: Double,
        longitude: Double,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        horizontalAccuracy: Double?,
        livePeriod: Long?,
        heading: Long?,
        proximityAlertRadius: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendLocation,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendLocationBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.latitude, latitude)
                    putNumberUnsafe(TBytesInfo.longitude, longitude)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (horizontalAccuracy != null) putNumberUnsafe(TBytesInfo.horizontal_accuracy, horizontalAccuracy)
                    if (livePeriod != null) putNumberUnsafe(TBytesInfo.live_period, livePeriod)
                    if (heading != null) putNumberUnsafe(TBytesInfo.heading, heading)
                    if (proximityAlertRadius != null) putNumberUnsafe(TBytesInfo.proximity_alert_radius, proximityAlertRadius)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendLocationBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatAdministratorCustomTitle,
            JsonByteBuffer(192).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                putStringUnsafe(TBytesInfo.custom_title, customTitle)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setUserEmojiStatusBSP = BufferSizePredictor(63, 1073741824, 126, 252)
    override suspend fun setUserEmojiStatus(
        userId: Long,
        emojiStatusCustomEmojiId: String?,
        emojiStatusExpirationDate: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setUserEmojiStatus,
            run {
                val bbSize0 = setUserEmojiStatusBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (emojiStatusCustomEmojiId != null) putStringUnsafe(TBytesInfo.emoji_status_custom_emoji_id, emojiStatusCustomEmojiId)
                    if (emojiStatusExpirationDate != null) putNumberUnsafe(TBytesInfo.emoji_status_expiration_date, emojiStatusExpirationDate)
                    setUserEmojiStatusBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setChatTitle(
        chatId: ChatId,
        title: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatTitle,
            JsonByteBuffer(826).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putStringUnsafe(TBytesInfo.title, title)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setChatDescription(
        chatId: ChatId,
        description: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatDescription,
            JsonByteBuffer(1594).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                if (description != null) putStringUnsafe(TBytesInfo.description, description)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatAdministrators(
        chatId: ChatId
    ): TResult<List<ChatMember>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatAdministrators,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<ChatMember>>(
                json.decodeFromString(TSerials.sListChatMember, strResult).result
            )
        } else {
            TResultFailure<List<ChatMember>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getManagedBotTokenBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun getManagedBotToken(
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<String> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getManagedBotToken,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getManagedBotTokenBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (requestOptions == null) getManagedBotTokenBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(TSerials.sString, strResult).result
            )
        } else {
            TResultFailure<String>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatMemberCount,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Long>(
                json.decodeFromString(TSerials.sLong, strResult).result
            )
        } else {
            TResultFailure<Long>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun removeUserVerification(
        userId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.removeUserVerification,
            JsonByteBuffer(32).run {
                putNumberUnsafe(TBytesInfo.user_id, userId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val removeBusinessAccountProfilePhotoBSP = BufferSizePredictor(31, 1073741824, 62, 124)
    override suspend fun removeBusinessAccountProfilePhoto(
        businessConnectionId: String,
        isPublic: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.removeBusinessAccountProfilePhoto,
            run {
                val bbSize0 = removeBusinessAccountProfilePhotoBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (isPublic != null) putBoolUnsafe(TBytesInfo.is_public, isPublic)
                    removeBusinessAccountProfilePhotoBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendPhoto(
        chatId: ChatId,
        photo: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendPhoto, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        photo.asVertx().executeCustom(mpb, "photo")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (showCaptionAboveMedia != null) mpb.writeNormalPart("show_caption_above_media", showCaptionAboveMedia.toString())
        if (hasSpoiler != null) mpb.writeNormalPart("has_spoiler", hasSpoiler.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendSticker(
        chatId: ChatId,
        sticker: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendSticker, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        sticker.asVertx().executeCustom(mpb, "sticker")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (emoji != null) mpb.writeNormalPart("emoji", emoji)
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getUserProfileAudiosBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun getUserProfileAudios(
        userId: Long,
        offset: Long?,
        limit: Long?,
        requestOptions: RequestOptions?
    ): TResult<UserProfileAudios> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUserProfileAudios,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getUserProfileAudiosBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                    if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                    if (requestOptions == null) getUserProfileAudiosBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<UserProfileAudios>(
                json.decodeFromString(TSerials.sUserProfileAudios, strResult).result
            )
        } else {
            TResultFailure<UserProfileAudios>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editMessageChecklistBSP = BufferSizePredictor(60, 1073741824, 120, 240)
    override suspend fun editMessageChecklist(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long,
        checklist: InputChecklist,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editMessageChecklist,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editMessageChecklistBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    putJsonObject(TBytesInfo.checklist, InputChecklist.serializer(), json, checklist)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) editMessageChecklistBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setPassportDataErrorsBSP = BufferSizePredictor(13, 1073741824, 26, 52)
    override suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setPassportDataErrors,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setPassportDataErrorsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putListOfJsonObjects(TBytesInfo.errors, PassportElementError.serializer(), json, errors)
                    if (requestOptions == null) setPassportDataErrorsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatMenuButtonBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun setChatMenuButton(
        chatId: Long?,
        menuButton: MenuButton?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatMenuButton,
            run {
                val bbSize0 = setChatMenuButtonBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    if (menuButton != null) putJsonObject(TBytesInfo.menu_button, MenuButton.serializer(), json, menuButton)
                    setChatMenuButtonBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun hideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.hideGeneralForumTopic,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendPollBSP = BufferSizePredictor(465, 1073741824, 930, 1860)
    override suspend fun sendPoll(
        chatId: ChatId,
        question: String,
        options: List<InputPollOption>,
        businessConnectionId: String?,
        messageThreadId: Long?,
        questionParseMode: ParseMode?,
        questionEntities: List<MessageEntity>?,
        isAnonymous: Boolean?,
        type: PollType?,
        allowsMultipleAnswers: Boolean?,
        allowsRevoting: Boolean?,
        shuffleOptions: Boolean?,
        allowAddingOptions: Boolean?,
        hideResultsUntilCloses: Boolean?,
        correctOptionIds: List<Long>?,
        explanation: String?,
        explanationParseMode: ParseMode?,
        explanationEntities: List<MessageEntity>?,
        openPeriod: Long?,
        closeDate: Long?,
        isClosed: Boolean?,
        description: String?,
        descriptionParseMode: ParseMode?,
        descriptionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendPoll,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendPollBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.question, question)
                    putListOfJsonObjects(TBytesInfo.options, InputPollOption.serializer(), json, options)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (questionParseMode != null) putStringUnsafe(TBytesInfo.question_parse_mode, questionParseMode.value)
                    if (questionEntities != null) putListOfJsonObjects(TBytesInfo.question_entities, MessageEntity.serializer(), json, questionEntities)
                    if (isAnonymous != null) putBoolUnsafe(TBytesInfo.is_anonymous, isAnonymous)
                    if (type != null) putStringUnsafe(TBytesInfo.type, type.value)
                    if (allowsMultipleAnswers != null) putBoolUnsafe(TBytesInfo.allows_multiple_answers, allowsMultipleAnswers)
                    if (allowsRevoting != null) putBoolUnsafe(TBytesInfo.allows_revoting, allowsRevoting)
                    if (shuffleOptions != null) putBoolUnsafe(TBytesInfo.shuffle_options, shuffleOptions)
                    if (allowAddingOptions != null) putBoolUnsafe(TBytesInfo.allow_adding_options, allowAddingOptions)
                    if (hideResultsUntilCloses != null) putBoolUnsafe(TBytesInfo.hide_results_until_closes, hideResultsUntilCloses)
                    if (correctOptionIds != null) putListOfLongUnsafe(TBytesInfo.correct_option_ids, correctOptionIds)
                    if (explanation != null) putStringUnsafe(TBytesInfo.explanation, explanation)
                    if (explanationParseMode != null) putStringUnsafe(TBytesInfo.explanation_parse_mode, explanationParseMode.value)
                    if (explanationEntities != null) putListOfJsonObjects(TBytesInfo.explanation_entities, MessageEntity.serializer(), json, explanationEntities)
                    if (openPeriod != null) putNumberUnsafe(TBytesInfo.open_period, openPeriod)
                    if (closeDate != null) putNumberUnsafe(TBytesInfo.close_date, closeDate)
                    if (isClosed != null) putBoolUnsafe(TBytesInfo.is_closed, isClosed)
                    if (description != null) putStringUnsafe(TBytesInfo.description, description)
                    if (descriptionParseMode != null) putStringUnsafe(TBytesInfo.description_parse_mode, descriptionParseMode.value)
                    if (descriptionEntities != null) putListOfJsonObjects(TBytesInfo.description_entities, MessageEntity.serializer(), json, descriptionEntities)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendPollBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMenuButton(
        chatId: Long?
    ): TResult<MenuButton> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatMenuButton,
            JsonByteBuffer(32).run {
                if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<MenuButton>(
                json.decodeFromString(TSerials.sMenuButton, strResult).result
            )
        } else {
            TResultFailure<MenuButton>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getUpdatesBSP = BufferSizePredictor(33, 1073741824, 66, 132)
    override suspend fun getUpdates(
        offset: Long?,
        limit: Long?,
        timeout: Long?,
        allowedUpdates: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<List<Update>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUpdates,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getUpdatesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                    if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                    if (timeout != null) putNumberUnsafe(TBytesInfo.timeout, timeout)
                    if (allowedUpdates != null) putListOfStringUnsafe(TBytesInfo.allowed_updates, allowedUpdates)
                    if (requestOptions == null) getUpdatesBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            true
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Update>>(
                json.decodeFromString(TSerials.sListUpdate, strResult).result
            )
        } else {
            TResultFailure<List<Update>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setMyName(
        name: String?,
        languageCode: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyName,
            JsonByteBuffer(416).run {
                if (name != null) putStringUnsafe(TBytesInfo.name, name)
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setBusinessAccountNameBSP = BufferSizePredictor(41, 1073741824, 82, 164)
    override suspend fun setBusinessAccountName(
        businessConnectionId: String,
        firstName: String,
        lastName: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountName,
            run {
                val bbSize0 = setBusinessAccountNameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putStringUnsafe(TBytesInfo.first_name, firstName)
                    if (lastName != null) putStringUnsafe(TBytesInfo.last_name, lastName)
                    setBusinessAccountNameBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun copyMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        removeCaption: Boolean?
    ): TResult<List<MessageId>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.copyMessages,
            JsonByteBuffer(2379).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                if (removeCaption != null) putBoolUnsafe(TBytesInfo.remove_caption, removeCaption)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<MessageId>>(
                json.decodeFromString(TSerials.sListMessageId, strResult).result
            )
        } else {
            TResultFailure<List<MessageId>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val unpinChatMessageBSP = BufferSizePredictor(39, 1073741824, 78, 156)
    override suspend fun unpinChatMessage(
        chatId: ChatId,
        businessConnectionId: String?,
        messageId: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinChatMessage,
            run {
                val bbSize0 = unpinChatMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    unpinChatMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val stopMessageLiveLocationBSP = BufferSizePredictor(68, 1073741824, 136, 272)
    override suspend fun stopMessageLiveLocation(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.stopMessageLiveLocation,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: stopMessageLiveLocationBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) stopMessageLiveLocationBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerEmojiListBSP = BufferSizePredictor(17, 1073741824, 34, 68)
    override suspend fun setStickerEmojiList(
        sticker: String,
        emojiList: List<String>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerEmojiList,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setStickerEmojiListBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.sticker, sticker)
                    putListOfStringUnsafe(TBytesInfo.emoji_list, emojiList)
                    if (requestOptions == null) setStickerEmojiListBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyDescription(
        languageCode: String?
    ): TResult<BotDescription> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyDescription,
            JsonByteBuffer(22).run {
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotDescription>(
                json.decodeFromString(TSerials.sBotDescription, strResult).result
            )
        } else {
            TResultFailure<BotDescription>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMember(
        chatId: ChatId,
        userId: Long
    ): TResult<ChatMember> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatMember,
            JsonByteBuffer(78).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatMember>(
                json.decodeFromString(TSerials.sChatMember, strResult).result
            )
        } else {
            TResultFailure<ChatMember>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): TResult<UserProfilePhotos> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUserProfilePhotos,
            JsonByteBuffer(91).run {
                putNumberUnsafe(TBytesInfo.user_id, userId)
                if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<UserProfilePhotos>(
                json.decodeFromString(TSerials.sUserProfilePhotos, strResult).result
            )
        } else {
            TResultFailure<UserProfilePhotos>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendDocument(
        chatId: ChatId,
        document: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        thumbnail: TelegramFile?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendDocument, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        document.asVertx().executeCustom(mpb, "document")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (disableContentTypeDetection != null) mpb.writeNormalPart("disable_content_type_detection", disableContentTypeDetection.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createChatInviteLink,
            JsonByteBuffer(349).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                if (name != null) putStringUnsafe(TBytesInfo.name, name)
                if (expireDate != null) putNumberUnsafe(TBytesInfo.expire_date, expireDate)
                if (memberLimit != null) putNumberUnsafe(TBytesInfo.member_limit, memberLimit)
                if (createsJoinRequest != null) putBoolUnsafe(TBytesInfo.creates_join_request, createsJoinRequest)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getStarTransactions(
        offset: Long?,
        limit: Long?
    ): TResult<StarTransactions> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getStarTransactions,
            JsonByteBuffer(60).run {
                if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StarTransactions>(
                json.decodeFromString(TSerials.sStarTransactions, strResult).result
            )
        } else {
            TResultFailure<StarTransactions>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatStickerSetBSP = BufferSizePredictor(23, 1073741824, 46, 92)
    override suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatStickerSet,
            run {
                val bbSize0 = setChatStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.sticker_set_name, stickerSetName)
                    setChatStickerSetBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyShortDescription,
            JsonByteBuffer(765).run {
                if (shortDescription != null) putStringUnsafe(TBytesInfo.short_description, shortDescription)
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun uploadStickerFile(
        userId: Long,
        sticker: TelegramFile,
        stickerFormat: StickerFormat
    ): TResult<File> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.uploadStickerFile, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("user_id", userId.toString())
        sticker.asVertx().executeCustom(mpb, "sticker")
        mpb.writeNormalPart("sticker_format", stickerFormat.value)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<File>(
                json.decodeFromString(TSerials.sFile, strResult).result
            )
        } else {
            TResultFailure<File>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editChatInviteLinkBSP = BufferSizePredictor(65, 1073741824, 130, 260)
    override suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editChatInviteLink,
            run {
                val bbSize0 = editChatInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                    if (name != null) putStringUnsafe(TBytesInfo.name, name)
                    if (expireDate != null) putNumberUnsafe(TBytesInfo.expire_date, expireDate)
                    if (memberLimit != null) putNumberUnsafe(TBytesInfo.member_limit, memberLimit)
                    if (createsJoinRequest != null) putBoolUnsafe(TBytesInfo.creates_join_request, createsJoinRequest)
                    editChatInviteLinkBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun leaveChat(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.leaveChat,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun closeGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.closeGeneralForumTopic,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val savePreparedKeyboardButtonBSP = BufferSizePredictor(13, 1073741824, 26, 52)
    override suspend fun savePreparedKeyboardButton(
        userId: Long,
        button: KeyboardButton,
        requestOptions: RequestOptions?
    ): TResult<PreparedKeyboardButton> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.savePreparedKeyboardButton,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: savePreparedKeyboardButtonBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putJsonObject(TBytesInfo.button, KeyboardButton.serializer(), json, button)
                    if (requestOptions == null) savePreparedKeyboardButtonBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<PreparedKeyboardButton>(
                json.decodeFromString(TSerials.sPreparedKeyboardButton, strResult).result
            )
        } else {
            TResultFailure<PreparedKeyboardButton>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: StickerFormat,
        thumbnail: TelegramFile?
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setStickerSetThumbnail, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("name", name)
        mpb.writeNormalPart("user_id", userId.toString())
        mpb.writeNormalPart("format", format.value)
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ): TResult<ChatAdministratorRights> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyDefaultAdministratorRights,
            JsonByteBuffer(22).run {
                if (forChannels != null) putBoolUnsafe(TBytesInfo.for_channels, forChannels)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatAdministratorRights>(
                json.decodeFromString(TSerials.sChatAdministratorRights, strResult).result
            )
        } else {
            TResultFailure<ChatAdministratorRights>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMe(): TResult<User> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getMe, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<User>(
                json.decodeFromString(TSerials.sUser, strResult).result
            )
        } else {
            TResultFailure<User>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerPositionInSetBSP = BufferSizePredictor(15, 1073741824, 30, 60)
    override suspend fun setStickerPositionInSet(
        sticker: String,
        position: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerPositionInSet,
            run {
                val bbSize0 = setStickerPositionInSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.sticker, sticker)
                    putNumberUnsafe(TBytesInfo.position, position)
                    setStickerPositionInSetBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setCustomEmojiStickerSetThumbnailBSP = BufferSizePredictor(19, 1073741824, 38, 76)
    override suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setCustomEmojiStickerSetThumbnail,
            run {
                val bbSize0 = setCustomEmojiStickerSetThumbnailBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.name, name)
                    if (customEmojiId != null) putStringUnsafe(TBytesInfo.custom_emoji_id, customEmojiId)
                    setCustomEmojiStickerSetThumbnailBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun close(): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.close, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val pinChatMessageBSP = BufferSizePredictor(59, 1073741824, 118, 236)
    override suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        disableNotification: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.pinChatMessage,
            run {
                val bbSize0 = pinChatMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    pinChatMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getCustomEmojiStickersBSP = BufferSizePredictor(16, 1073741824, 32, 64)
    override suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>,
        requestOptions: RequestOptions?
    ): TResult<List<Sticker>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getCustomEmojiStickers,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getCustomEmojiStickersBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putListOfStringUnsafe(TBytesInfo.custom_emoji_ids, customEmojiIds)
                    if (requestOptions == null) getCustomEmojiStickersBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Sticker>>(
                json.decodeFromString(TSerials.sListSticker, strResult).result
            )
        } else {
            TResultFailure<List<Sticker>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val upgradeGiftBSP = BufferSizePredictor(66, 1073741824, 132, 264)
    override suspend fun upgradeGift(
        businessConnectionId: String,
        ownedGiftId: String,
        keepOriginalDetails: Boolean?,
        starCount: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.upgradeGift,
            run {
                val bbSize0 = upgradeGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                    if (keepOriginalDetails != null) putBoolUnsafe(TBytesInfo.keep_original_details, keepOriginalDetails)
                    if (starCount != null) putNumberUnsafe(TBytesInfo.star_count, starCount)
                    upgradeGiftBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val answerInlineQueryBSP = BufferSizePredictor(60, 1073741824, 120, 240)
    override suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Long?,
        isPersonal: Boolean?,
        nextOffset: String?,
        button: InlineQueryResultsButton?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerInlineQuery,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: answerInlineQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.inline_query_id, inlineQueryId)
                    putListOfJsonObjects(TBytesInfo.results, InlineQueryResult.serializer(), json, results)
                    if (cacheTime != null) putNumberUnsafe(TBytesInfo.cache_time, cacheTime)
                    if (isPersonal != null) putBoolUnsafe(TBytesInfo.is_personal, isPersonal)
                    if (nextOffset != null) putStringUnsafe(TBytesInfo.next_offset, nextOffset)
                    if (button != null) putJsonObject(TBytesInfo.button, InlineQueryResultsButton.serializer(), json, button)
                    if (requestOptions == null) answerInlineQueryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val revokeChatInviteLinkBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.revokeChatInviteLink,
            run {
                val bbSize0 = revokeChatInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                    revokeChatInviteLinkBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val transferBusinessAccountStarsBSP = BufferSizePredictor(32, 1073741824, 64, 128)
    override suspend fun transferBusinessAccountStars(
        businessConnectionId: String,
        starCount: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.transferBusinessAccountStars,
            run {
                val bbSize0 = transferBusinessAccountStarsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.star_count, starCount)
                    transferBusinessAccountStarsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val answerPreCheckoutQueryBSP = BufferSizePredictor(36, 1073741824, 72, 144)
    override suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerPreCheckoutQuery,
            run {
                val bbSize0 = answerPreCheckoutQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.pre_checkout_query_id, preCheckoutQueryId)
                    putBoolUnsafe(TBytesInfo.ok, ok)
                    if (errorMessage != null) putStringUnsafe(TBytesInfo.error_message, errorMessage)
                    answerPreCheckoutQueryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendMessageBSP = BufferSizePredictor(237, 1073741824, 474, 948)
    override suspend fun sendMessage(
        chatId: ChatId,
        text: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        linkPreviewOptions: LinkPreviewOptions?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.text, text)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (parseMode != null) putStringUnsafe(TBytesInfo.parse_mode, parseMode.value)
                    if (entities != null) putListOfJsonObjects(TBytesInfo.entities, MessageEntity.serializer(), json, entities)
                    if (linkPreviewOptions != null) putJsonObject(TBytesInfo.link_preview_options, LinkPreviewOptions.serializer(), json, linkPreviewOptions)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendChatActionBSP = BufferSizePredictor(52, 1073741824, 104, 208)
    override suspend fun sendChatAction(
        chatId: ChatId,
        action: ChatAction,
        businessConnectionId: String?,
        messageThreadId: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendChatAction,
            run {
                val bbSize0 = sendChatActionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.action, action.value)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    sendChatActionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun createChatSubscriptionInviteLink(
        chatId: ChatId,
        subscriptionPeriod: Long,
        subscriptionPrice: Long,
        name: String?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createChatSubscriptionInviteLink,
            JsonByteBuffer(334).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.subscription_period, subscriptionPeriod)
                putNumberUnsafe(TBytesInfo.subscription_price, subscriptionPrice)
                if (name != null) putStringUnsafe(TBytesInfo.name, name)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMessage,
            JsonByteBuffer(81).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.message_id, messageId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyShortDescription(
        languageCode: String?
    ): TResult<BotShortDescription> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyShortDescription,
            JsonByteBuffer(22).run {
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotShortDescription>(
                json.decodeFromString(TSerials.sBotShortDescription, strResult).result
            )
        } else {
            TResultFailure<BotShortDescription>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val answerShippingQueryBSP = BufferSizePredictor(48, 1073741824, 96, 192)
    override suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerShippingQuery,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: answerShippingQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.shipping_query_id, shippingQueryId)
                    putBoolUnsafe(TBytesInfo.ok, ok)
                    if (shippingOptions != null) putListOfJsonObjects(TBytesInfo.shipping_options, ShippingOption.serializer(), json, shippingOptions)
                    if (errorMessage != null) putStringUnsafe(TBytesInfo.error_message, errorMessage)
                    if (requestOptions == null) answerShippingQueryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val convertGiftToStarsBSP = BufferSizePredictor(35, 1073741824, 70, 140)
    override suspend fun convertGiftToStars(
        businessConnectionId: String,
        ownedGiftId: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.convertGiftToStars,
            run {
                val bbSize0 = convertGiftToStarsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                    convertGiftToStarsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val savePreparedInlineMessageBSP = BufferSizePredictor(80, 1073741824, 160, 320)
    override suspend fun savePreparedInlineMessage(
        userId: Long,
        result: InlineQueryResult,
        allowUserChats: Boolean?,
        allowBotChats: Boolean?,
        allowGroupChats: Boolean?,
        allowChannelChats: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<PreparedInlineMessage> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.savePreparedInlineMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: savePreparedInlineMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putJsonObject(TBytesInfo.result, InlineQueryResult.serializer(), json, result)
                    if (allowUserChats != null) putBoolUnsafe(TBytesInfo.allow_user_chats, allowUserChats)
                    if (allowBotChats != null) putBoolUnsafe(TBytesInfo.allow_bot_chats, allowBotChats)
                    if (allowGroupChats != null) putBoolUnsafe(TBytesInfo.allow_group_chats, allowGroupChats)
                    if (allowChannelChats != null) putBoolUnsafe(TBytesInfo.allow_channel_chats, allowChannelChats)
                    if (requestOptions == null) savePreparedInlineMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<PreparedInlineMessage>(
                json.decodeFromString(TSerials.sPreparedInlineMessage, strResult).result
            )
        } else {
            TResultFailure<PreparedInlineMessage>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteStoryBSP = BufferSizePredictor(30, 1073741824, 60, 120)
    override suspend fun deleteStory(
        businessConnectionId: String,
        storyId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStory,
            run {
                val bbSize0 = deleteStoryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.story_id, storyId)
                    deleteStoryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val giftPremiumSubscriptionBSP = BufferSizePredictor(60, 1073741824, 120, 240)
    override suspend fun giftPremiumSubscription(
        userId: Long,
        monthCount: Long,
        starCount: Long,
        text: String?,
        textParseMode: ParseMode?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.giftPremiumSubscription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: giftPremiumSubscriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putNumberUnsafe(TBytesInfo.month_count, monthCount)
                    putNumberUnsafe(TBytesInfo.star_count, starCount)
                    if (text != null) putStringUnsafe(TBytesInfo.text, text)
                    if (textParseMode != null) putStringUnsafe(TBytesInfo.text_parse_mode, textParseMode.value)
                    if (textEntities != null) putListOfJsonObjects(TBytesInfo.text_entities, MessageEntity.serializer(), json, textEntities)
                    if (requestOptions == null) giftPremiumSubscriptionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editForumTopicBSP = BufferSizePredictor(48, 1073741824, 96, 192)
    override suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String?,
        iconCustomEmojiId: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editForumTopic,
            run {
                val bbSize0 = editForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (name != null) putStringUnsafe(TBytesInfo.name, name)
                    if (iconCustomEmojiId != null) putStringUnsafe(TBytesInfo.icon_custom_emoji_id, iconCustomEmojiId)
                    editForumTopicBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendContactBSP = BufferSizePredictor(231, 1073741824, 462, 924)
    override suspend fun sendContact(
        chatId: ChatId,
        phoneNumber: String,
        firstName: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendContact,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendContactBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.phone_number, phoneNumber)
                    putStringUnsafe(TBytesInfo.first_name, firstName)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (lastName != null) putStringUnsafe(TBytesInfo.last_name, lastName)
                    if (vcard != null) putStringUnsafe(TBytesInfo.vcard, vcard)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendContactBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unpinAllChatMessages(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinAllChatMessages,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val restrictChatMemberBSP = BufferSizePredictor(67, 1073741824, 134, 268)
    override suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.restrictChatMember,
            run {
                val bbSize0 = restrictChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putJsonObject(TBytesInfo.permissions, ChatPermissions.serializer(), json, permissions)
                    if (useIndependentChatPermissions != null) putBoolUnsafe(TBytesInfo.use_independent_chat_permissions, useIndependentChatPermissions)
                    if (untilDate != null) putNumberUnsafe(TBytesInfo.until_date, untilDate)
                    restrictChatMemberBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setBusinessAccountProfilePhoto(
        businessConnectionId: String,
        photo: InputProfilePhoto,
        isPublic: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setBusinessAccountProfilePhoto, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("business_connection_id", businessConnectionId)
        photo.executeAll(mpb)
        mpb.writeJsonPart("photo", InputProfilePhoto.serializer(), photo, json)
        if (isPublic != null) mpb.writeNormalPart("is_public", isPublic.toString())
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getBusinessAccountGiftsBSP = BufferSizePredictor(184, 1073741824, 368, 736)
    override suspend fun getBusinessAccountGifts(
        businessConnectionId: String,
        excludeUnsaved: Boolean?,
        excludeSaved: Boolean?,
        excludeUnlimited: Boolean?,
        excludeLimitedUpgradable: Boolean?,
        excludeLimitedNonUpgradable: Boolean?,
        excludeUnique: Boolean?,
        excludeFromBlockchain: Boolean?,
        sortByPrice: Boolean?,
        offset: String?,
        limit: Long?
    ): TResult<OwnedGifts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessAccountGifts,
            run {
                val bbSize0 = getBusinessAccountGiftsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (excludeUnsaved != null) putBoolUnsafe(TBytesInfo.exclude_unsaved, excludeUnsaved)
                    if (excludeSaved != null) putBoolUnsafe(TBytesInfo.exclude_saved, excludeSaved)
                    if (excludeUnlimited != null) putBoolUnsafe(TBytesInfo.exclude_unlimited, excludeUnlimited)
                    if (excludeLimitedUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_upgradable, excludeLimitedUpgradable)
                    if (excludeLimitedNonUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_non_upgradable, excludeLimitedNonUpgradable)
                    if (excludeUnique != null) putBoolUnsafe(TBytesInfo.exclude_unique, excludeUnique)
                    if (excludeFromBlockchain != null) putBoolUnsafe(TBytesInfo.exclude_from_blockchain, excludeFromBlockchain)
                    if (sortByPrice != null) putBoolUnsafe(TBytesInfo.sort_by_price, sortByPrice)
                    if (offset != null) putStringUnsafe(TBytesInfo.offset, offset)
                    if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                    getBusinessAccountGiftsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<OwnedGifts>(
                json.decodeFromString(TSerials.sOwnedGifts, strResult).result
            )
        } else {
            TResultFailure<OwnedGifts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val forwardMessageBSP = BufferSizePredictor(168, 1073741824, 336, 672)
    override suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        videoStartTimestamp: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.forwardMessage,
            run {
                val bbSize0 = forwardMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (videoStartTimestamp != null) putNumberUnsafe(TBytesInfo.video_start_timestamp, videoStartTimestamp)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    forwardMessageBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editUserStarSubscriptionBSP = BufferSizePredictor(44, 1073741824, 88, 176)
    override suspend fun editUserStarSubscription(
        userId: Long,
        telegramPaymentChargeId: String,
        isCanceled: Boolean
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editUserStarSubscription,
            run {
                val bbSize0 = editUserStarSubscriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putStringUnsafe(TBytesInfo.telegram_payment_charge_id, telegramPaymentChargeId)
                    putBoolUnsafe(TBytesInfo.is_canceled, isCanceled)
                    editUserStarSubscriptionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getForumTopicIconStickers(): TResult<List<Sticker>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getForumTopicIconStickers, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Sticker>>(
                json.decodeFromString(TSerials.sListSticker, strResult).result
            )
        } else {
            TResultFailure<List<Sticker>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setBusinessAccountGiftSettingsBSP = BufferSizePredictor(57, 1073741824, 114, 228)
    override suspend fun setBusinessAccountGiftSettings(
        businessConnectionId: String,
        showGiftButton: Boolean,
        acceptedGiftTypes: AcceptedGiftTypes
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountGiftSettings,
            run {
                val bbSize0 = setBusinessAccountGiftSettingsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putBoolUnsafe(TBytesInfo.show_gift_button, showGiftButton)
                    putJsonObject(TBytesInfo.accepted_gift_types, AcceptedGiftTypes.serializer(), json, acceptedGiftTypes)
                    setBusinessAccountGiftSettingsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setBusinessAccountBioBSP = BufferSizePredictor(25, 1073741824, 50, 100)
    override suspend fun setBusinessAccountBio(
        businessConnectionId: String,
        bio: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountBio,
            run {
                val bbSize0 = setBusinessAccountBioBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (bio != null) putStringUnsafe(TBytesInfo.bio, bio)
                    setBusinessAccountBioBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unbanChatSenderChat,
            JsonByteBuffer(85).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.sender_chat_id, senderChatId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getMyCommandsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<List<BotCommand>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyCommands,
            run {
                val bbSize0 = getMyCommandsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                    getMyCommandsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<BotCommand>>(
                json.decodeFromString(TSerials.sListBotCommand, strResult).result
            )
        } else {
            TResultFailure<List<BotCommand>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setWebhook(
        url: String,
        certificate: TelegramFile?,
        ipAddress: String?,
        maxConnections: Long?,
        allowedUpdates: List<String>?,
        dropPendingUpdates: Boolean?,
        secretToken: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setWebhook, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("url", url)
        certificate?.asVertx()?.executeCustom(mpb, "certificate")
        if (ipAddress != null) mpb.writeNormalPart("ip_address", ipAddress)
        if (maxConnections != null) mpb.writeNormalPart("max_connections", maxConnections.toString())
        if (allowedUpdates != null) mpb.writeJsonPart("allowed_updates", TSerials.aListString, allowedUpdates, json)
        if (dropPendingUpdates != null) mpb.writeNormalPart("drop_pending_updates", dropPendingUpdates.toString())
        if (secretToken != null) mpb.writeNormalPart("secret_token", secretToken)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatMemberTagBSP = BufferSizePredictor(17, 1073741824, 34, 68)
    override suspend fun setChatMemberTag(
        chatId: ChatId,
        userId: Long,
        tag: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatMemberTag,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatMemberTagBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (tag != null) putStringUnsafe(TBytesInfo.tag, tag)
                    if (requestOptions == null) setChatMemberTagBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerMaskPositionBSP = BufferSizePredictor(20, 1073741824, 40, 80)
    override suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerMaskPosition,
            run {
                val bbSize0 = setStickerMaskPositionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.sticker, sticker)
                    if (maskPosition != null) putJsonObject(TBytesInfo.mask_position, MaskPosition.serializer(), json, maskPosition)
                    setStickerMaskPositionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun verifyUser(
        userId: Long,
        customDescription: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.verifyUser,
            JsonByteBuffer(476).run {
                putNumberUnsafe(TBytesInfo.user_id, userId)
                if (customDescription != null) putStringUnsafe(TBytesInfo.custom_description, customDescription)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteWebhook,
            JsonByteBuffer(30).run {
                if (dropPendingUpdates != null) putBoolUnsafe(TBytesInfo.drop_pending_updates, dropPendingUpdates)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendGiftBSP = BufferSizePredictor(68, 1073741824, 136, 272)
    override suspend fun sendGift(
        giftId: String,
        userId: Long?,
        chatId: ChatId?,
        payForUpgrade: Boolean?,
        text: String?,
        textParseMode: ParseMode?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendGift,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.gift_id, giftId)
                    if (userId != null) putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (payForUpgrade != null) putBoolUnsafe(TBytesInfo.pay_for_upgrade, payForUpgrade)
                    if (text != null) putStringUnsafe(TBytesInfo.text, text)
                    if (textParseMode != null) putStringUnsafe(TBytesInfo.text_parse_mode, textParseMode.value)
                    if (textEntities != null) putListOfJsonObjects(TBytesInfo.text_entities, MessageEntity.serializer(), json, textEntities)
                    if (requestOptions == null) sendGiftBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun postStory(
        businessConnectionId: String,
        content: InputStoryContent,
        activePeriod: Long,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        areas: List<StoryArea>?,
        postToChatPage: Boolean?,
        protectContent: Boolean?
    ): TResult<Story> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.postStory, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("business_connection_id", businessConnectionId)
        content.executeAll(mpb)
        mpb.writeJsonPart("content", InputStoryContent.serializer(), content, json)
        mpb.writeNormalPart("active_period", activePeriod.toString())
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (areas != null) mpb.writeJsonPart("areas", TSerials.aListStoryArea, areas, json)
        if (postToChatPage != null) mpb.writeNormalPart("post_to_chat_page", postToChatPage.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Story>(
                json.decodeFromString(TSerials.sStory, strResult).result
            )
        } else {
            TResultFailure<Story>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getFileBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun getFile(
        fileId: String
    ): TResult<File> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getFile,
            run {
                val bbSize0 = getFileBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.file_id, fileId)
                    getFileBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<File>(
                json.decodeFromString(TSerials.sFile, strResult).result
            )
        } else {
            TResultFailure<File>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val transferGiftBSP = BufferSizePredictor(62, 1073741824, 124, 248)
    override suspend fun transferGift(
        businessConnectionId: String,
        ownedGiftId: String,
        newOwnerChatId: Long,
        starCount: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.transferGift,
            run {
                val bbSize0 = transferGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                    putNumberUnsafe(TBytesInfo.new_owner_chat_id, newOwnerChatId)
                    if (starCount != null) putNumberUnsafe(TBytesInfo.star_count, starCount)
                    transferGiftBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerKeywordsBSP = BufferSizePredictor(15, 1073741824, 30, 60)
    override suspend fun setStickerKeywords(
        sticker: String,
        keywords: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerKeywords,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setStickerKeywordsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.sticker, sticker)
                    if (keywords != null) putListOfStringUnsafe(TBytesInfo.keywords, keywords)
                    if (requestOptions == null) setStickerKeywordsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: Long
    ): TResult<UserChatBoosts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUserChatBoosts,
            JsonByteBuffer(78).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<UserChatBoosts>(
                json.decodeFromString(TSerials.sUserChatBoosts, strResult).result
            )
        } else {
            TResultFailure<UserChatBoosts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getGameHighScoresBSP = BufferSizePredictor(41, 1073741824, 82, 164)
    override suspend fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult<List<GameHighScore>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getGameHighScores,
            run {
                val bbSize0 = getGameHighScoresBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    getGameHighScoresBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<GameHighScore>>(
                json.decodeFromString(TSerials.sListGameHighScore, strResult).result
            )
        } else {
            TResultFailure<List<GameHighScore>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val createInvoiceLinkBSP = BufferSizePredictor(301, 1073741824, 602, 1204)
    override suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        currency: CurrencyKind,
        prices: List<LabeledPrice>,
        businessConnectionId: String?,
        providerToken: String?,
        subscriptionPeriod: Long?,
        maxTipAmount: Long?,
        suggestedTipAmounts: List<Long>?,
        providerData: String?,
        photoUrl: String?,
        photoSize: Long?,
        photoWidth: Long?,
        photoHeight: Long?,
        needName: Boolean?,
        needPhoneNumber: Boolean?,
        needEmail: Boolean?,
        needShippingAddress: Boolean?,
        sendPhoneNumberToProvider: Boolean?,
        sendEmailToProvider: Boolean?,
        isFlexible: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<String> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createInvoiceLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: createInvoiceLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.title, title)
                    putStringUnsafe(TBytesInfo.description, description)
                    putStringUnsafe(TBytesInfo.payload, payload)
                    putStringUnsafe(TBytesInfo.currency, currency.value)
                    putListOfJsonObjects(TBytesInfo.prices, LabeledPrice.serializer(), json, prices)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (providerToken != null) putStringUnsafe(TBytesInfo.provider_token, providerToken)
                    if (subscriptionPeriod != null) putNumberUnsafe(TBytesInfo.subscription_period, subscriptionPeriod)
                    if (maxTipAmount != null) putNumberUnsafe(TBytesInfo.max_tip_amount, maxTipAmount)
                    if (suggestedTipAmounts != null) putListOfLongUnsafe(TBytesInfo.suggested_tip_amounts, suggestedTipAmounts)
                    if (providerData != null) putStringUnsafe(TBytesInfo.provider_data, providerData)
                    if (photoUrl != null) putStringUnsafe(TBytesInfo.photo_url, photoUrl)
                    if (photoSize != null) putNumberUnsafe(TBytesInfo.photo_size, photoSize)
                    if (photoWidth != null) putNumberUnsafe(TBytesInfo.photo_width, photoWidth)
                    if (photoHeight != null) putNumberUnsafe(TBytesInfo.photo_height, photoHeight)
                    if (needName != null) putBoolUnsafe(TBytesInfo.need_name, needName)
                    if (needPhoneNumber != null) putBoolUnsafe(TBytesInfo.need_phone_number, needPhoneNumber)
                    if (needEmail != null) putBoolUnsafe(TBytesInfo.need_email, needEmail)
                    if (needShippingAddress != null) putBoolUnsafe(TBytesInfo.need_shipping_address, needShippingAddress)
                    if (sendPhoneNumberToProvider != null) putBoolUnsafe(TBytesInfo.send_phone_number_to_provider, sendPhoneNumberToProvider)
                    if (sendEmailToProvider != null) putBoolUnsafe(TBytesInfo.send_email_to_provider, sendEmailToProvider)
                    if (isFlexible != null) putBoolUnsafe(TBytesInfo.is_flexible, isFlexible)
                    if (requestOptions == null) createInvoiceLinkBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(TSerials.sString, strResult).result
            )
        } else {
            TResultFailure<String>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun reopenGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.reopenGeneralForumTopic,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteChatStickerSet(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteChatStickerSet,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editGeneralForumTopic,
            JsonByteBuffer(825).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putStringUnsafe(TBytesInfo.name, name)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendVoice(
        chatId: ChatId,
        voice: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendVoice, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        voice.asVertx().executeCustom(mpb, "voice")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (duration != null) mpb.writeNormalPart("duration", duration.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.banChatSenderChat,
            JsonByteBuffer(85).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.sender_chat_id, senderChatId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getWebhookInfo(): TResult<WebhookInfo> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getWebhookInfo, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<WebhookInfo>(
                json.decodeFromString(TSerials.sWebhookInfo, strResult).result
            )
        } else {
            TResultFailure<WebhookInfo>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setMyCommandsBSP = BufferSizePredictor(26, 1073741824, 52, 104)
    override suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyCommands,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMyCommandsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putListOfJsonObjects(TBytesInfo.commands, BotCommand.serializer(), json, commands)
                    if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                    if (requestOptions == null) setMyCommandsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editMessageTextBSP = BufferSizePredictor(110, 1073741824, 220, 440)
    override suspend fun editMessageText(
        text: String,
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        linkPreviewOptions: LinkPreviewOptions?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editMessageText,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editMessageTextBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.text, text)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    if (parseMode != null) putStringUnsafe(TBytesInfo.parse_mode, parseMode.value)
                    if (entities != null) putListOfJsonObjects(TBytesInfo.entities, MessageEntity.serializer(), json, entities)
                    if (linkPreviewOptions != null) putJsonObject(TBytesInfo.link_preview_options, LinkPreviewOptions.serializer(), json, linkPreviewOptions)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) editMessageTextBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendInvoiceBSP = BufferSizePredictor(448, 1073741824, 896, 1792)
    override suspend fun sendInvoice(
        chatId: ChatId,
        title: String,
        description: String,
        payload: String,
        currency: CurrencyKind,
        prices: List<LabeledPrice>,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        providerToken: String?,
        maxTipAmount: Long?,
        suggestedTipAmounts: List<Long>?,
        startParameter: String?,
        providerData: String?,
        photoUrl: String?,
        photoSize: Long?,
        photoWidth: Long?,
        photoHeight: Long?,
        needName: Boolean?,
        needPhoneNumber: Boolean?,
        needEmail: Boolean?,
        needShippingAddress: Boolean?,
        sendPhoneNumberToProvider: Boolean?,
        sendEmailToProvider: Boolean?,
        isFlexible: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendInvoice,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendInvoiceBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.title, title)
                    putStringUnsafe(TBytesInfo.description, description)
                    putStringUnsafe(TBytesInfo.payload, payload)
                    putStringUnsafe(TBytesInfo.currency, currency.value)
                    putListOfJsonObjects(TBytesInfo.prices, LabeledPrice.serializer(), json, prices)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (providerToken != null) putStringUnsafe(TBytesInfo.provider_token, providerToken)
                    if (maxTipAmount != null) putNumberUnsafe(TBytesInfo.max_tip_amount, maxTipAmount)
                    if (suggestedTipAmounts != null) putListOfLongUnsafe(TBytesInfo.suggested_tip_amounts, suggestedTipAmounts)
                    if (startParameter != null) putStringUnsafe(TBytesInfo.start_parameter, startParameter)
                    if (providerData != null) putStringUnsafe(TBytesInfo.provider_data, providerData)
                    if (photoUrl != null) putStringUnsafe(TBytesInfo.photo_url, photoUrl)
                    if (photoSize != null) putNumberUnsafe(TBytesInfo.photo_size, photoSize)
                    if (photoWidth != null) putNumberUnsafe(TBytesInfo.photo_width, photoWidth)
                    if (photoHeight != null) putNumberUnsafe(TBytesInfo.photo_height, photoHeight)
                    if (needName != null) putBoolUnsafe(TBytesInfo.need_name, needName)
                    if (needPhoneNumber != null) putBoolUnsafe(TBytesInfo.need_phone_number, needPhoneNumber)
                    if (needEmail != null) putBoolUnsafe(TBytesInfo.need_email, needEmail)
                    if (needShippingAddress != null) putBoolUnsafe(TBytesInfo.need_shipping_address, needShippingAddress)
                    if (sendPhoneNumberToProvider != null) putBoolUnsafe(TBytesInfo.send_phone_number_to_provider, sendPhoneNumberToProvider)
                    if (sendEmailToProvider != null) putBoolUnsafe(TBytesInfo.send_email_to_provider, sendEmailToProvider)
                    if (isFlexible != null) putBoolUnsafe(TBytesInfo.is_flexible, isFlexible)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendInvoiceBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyName(
        languageCode: String?
    ): TResult<BotName> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyName,
            JsonByteBuffer(22).run {
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotName>(
                json.decodeFromString(TSerials.sBotName, strResult).result
            )
        } else {
            TResultFailure<BotName>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setMessageReactionBSP = BufferSizePredictor(31, 1073741824, 62, 124)
    override suspend fun setMessageReaction(
        chatId: ChatId,
        messageId: Long,
        reaction: List<ReactionType>?,
        isBig: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMessageReaction,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMessageReactionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (reaction != null) putListOfJsonObjects(TBytesInfo.reaction, ReactionType.serializer(), json, reaction)
                    if (isBig != null) putBoolUnsafe(TBytesInfo.is_big, isBig)
                    if (requestOptions == null) setMessageReactionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unbanChatMember,
            JsonByteBuffer(101).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                if (onlyIfBanned != null) putBoolUnsafe(TBytesInfo.only_if_banned, onlyIfBanned)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendVideoNote(
        chatId: ChatId,
        videoNote: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        duration: Long?,
        length: Long?,
        thumbnail: TelegramFile?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendVideoNote, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        videoNote.asVertx().executeCustom(mpb, "video_note")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (duration != null) mpb.writeNormalPart("duration", duration.toString())
        if (length != null) mpb.writeNormalPart("length", length.toString())
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setMyDefaultAdministratorRightsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyDefaultAdministratorRights,
            run {
                val bbSize0 = setMyDefaultAdministratorRightsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (rights != null) putJsonObject(TBytesInfo.rights, ChatAdministratorRights.serializer(), json, rights)
                    if (forChannels != null) putBoolUnsafe(TBytesInfo.for_channels, forChannels)
                    setMyDefaultAdministratorRightsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getChatGiftsBSP = BufferSizePredictor(169, 1073741824, 338, 676)
    override suspend fun getChatGifts(
        chatId: ChatId,
        excludeUnsaved: Boolean?,
        excludeSaved: Boolean?,
        excludeUnlimited: Boolean?,
        excludeLimitedUpgradable: Boolean?,
        excludeLimitedNonUpgradable: Boolean?,
        excludeFromBlockchain: Boolean?,
        excludeUnique: Boolean?,
        sortByPrice: Boolean?,
        offset: String?,
        limit: Long?,
        requestOptions: RequestOptions?
    ): TResult<OwnedGifts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatGifts,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getChatGiftsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (excludeUnsaved != null) putBoolUnsafe(TBytesInfo.exclude_unsaved, excludeUnsaved)
                    if (excludeSaved != null) putBoolUnsafe(TBytesInfo.exclude_saved, excludeSaved)
                    if (excludeUnlimited != null) putBoolUnsafe(TBytesInfo.exclude_unlimited, excludeUnlimited)
                    if (excludeLimitedUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_upgradable, excludeLimitedUpgradable)
                    if (excludeLimitedNonUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_non_upgradable, excludeLimitedNonUpgradable)
                    if (excludeFromBlockchain != null) putBoolUnsafe(TBytesInfo.exclude_from_blockchain, excludeFromBlockchain)
                    if (excludeUnique != null) putBoolUnsafe(TBytesInfo.exclude_unique, excludeUnique)
                    if (sortByPrice != null) putBoolUnsafe(TBytesInfo.sort_by_price, sortByPrice)
                    if (offset != null) putStringUnsafe(TBytesInfo.offset, offset)
                    if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                    if (requestOptions == null) getChatGiftsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<OwnedGifts>(
                json.decodeFromString(TSerials.sOwnedGifts, strResult).result
            )
        } else {
            TResultFailure<OwnedGifts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChat,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatFullInfo>(
                json.decodeFromString(TSerials.sChatFullInfo, strResult).result
            )
        } else {
            TResultFailure<ChatFullInfo>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteMyCommandsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMyCommands,
            run {
                val bbSize0 = deleteMyCommandsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                    deleteMyCommandsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendMessageDraftBSP = BufferSizePredictor(54, 1073741824, 108, 216)
    override suspend fun sendMessageDraft(
        chatId: Long,
        draftId: Long,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendMessageDraft,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendMessageDraftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    putNumberUnsafe(TBytesInfo.draft_id, draftId)
                    putStringUnsafe(TBytesInfo.text, text)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (parseMode != null) putStringUnsafe(TBytesInfo.parse_mode, parseMode.value)
                    if (entities != null) putListOfJsonObjects(TBytesInfo.entities, MessageEntity.serializer(), json, entities)
                    if (requestOptions == null) sendMessageDraftBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val createForumTopicBSP = BufferSizePredictor(41, 1073741824, 82, 164)
    override suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Long?,
        iconCustomEmojiId: String?
    ): TResult<ForumTopic> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createForumTopic,
            run {
                val bbSize0 = createForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putStringUnsafe(TBytesInfo.name, name)
                    if (iconColor != null) putNumberUnsafe(TBytesInfo.icon_color, iconColor)
                    if (iconCustomEmojiId != null) putStringUnsafe(TBytesInfo.icon_custom_emoji_id, iconCustomEmojiId)
                    createForumTopicBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ForumTopic>(
                json.decodeFromString(TSerials.sForumTopic, strResult).result
            )
        } else {
            TResultFailure<ForumTopic>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<MediaGroupAccepted>,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?
    ): TResult<List<Message>> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendMediaGroup, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        for (mIdx in media.indices) { media[mIdx].executeAll(mpb) }
        mpb.writeJsonPart("media", ListSerializer(InputMedia.serializer()), media.map { it as InputMedia }, json)
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Message>>(
                json.decodeFromString(TSerials.sListMessage, strResult).result
            )
        } else {
            TResultFailure<List<Message>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendVideo(
        chatId: ChatId,
        video: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: TelegramFile?,
        cover: TelegramFile?,
        startTimestamp: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        hasSpoiler: Boolean?,
        supportsStreaming: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendVideo, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        video.asVertx().executeCustom(mpb, "video")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (duration != null) mpb.writeNormalPart("duration", duration.toString())
        if (width != null) mpb.writeNormalPart("width", width.toString())
        if (height != null) mpb.writeNormalPart("height", height.toString())
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        cover?.asVertx()?.executeCustom(mpb, "cover")
        if (startTimestamp != null) mpb.writeNormalPart("start_timestamp", startTimestamp.toString())
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (showCaptionAboveMedia != null) mpb.writeNormalPart("show_caption_above_media", showCaptionAboveMedia.toString())
        if (hasSpoiler != null) mpb.writeNormalPart("has_spoiler", hasSpoiler.toString())
        if (supportsStreaming != null) mpb.writeNormalPart("supports_streaming", supportsStreaming.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val replaceManagedBotTokenBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun replaceManagedBotToken(
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<String> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.replaceManagedBotToken,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: replaceManagedBotTokenBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (requestOptions == null) replaceManagedBotTokenBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(TSerials.sString, strResult).result
            )
        } else {
            TResultFailure<String>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun promoteChatMember(
        chatId: ChatId,
        userId: Long,
        isAnonymous: Boolean?,
        canManageChat: Boolean?,
        canDeleteMessages: Boolean?,
        canManageVideoChats: Boolean?,
        canRestrictMembers: Boolean?,
        canPromoteMembers: Boolean?,
        canChangeInfo: Boolean?,
        canInviteUsers: Boolean?,
        canPostStories: Boolean?,
        canEditStories: Boolean?,
        canDeleteStories: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canPinMessages: Boolean?,
        canManageTopics: Boolean?,
        canManageDirectMessages: Boolean?,
        canManageTags: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.promoteChatMember,
            JsonByteBuffer(503).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                if (isAnonymous != null) putBoolUnsafe(TBytesInfo.is_anonymous, isAnonymous)
                if (canManageChat != null) putBoolUnsafe(TBytesInfo.can_manage_chat, canManageChat)
                if (canDeleteMessages != null) putBoolUnsafe(TBytesInfo.can_delete_messages, canDeleteMessages)
                if (canManageVideoChats != null) putBoolUnsafe(TBytesInfo.can_manage_video_chats, canManageVideoChats)
                if (canRestrictMembers != null) putBoolUnsafe(TBytesInfo.can_restrict_members, canRestrictMembers)
                if (canPromoteMembers != null) putBoolUnsafe(TBytesInfo.can_promote_members, canPromoteMembers)
                if (canChangeInfo != null) putBoolUnsafe(TBytesInfo.can_change_info, canChangeInfo)
                if (canInviteUsers != null) putBoolUnsafe(TBytesInfo.can_invite_users, canInviteUsers)
                if (canPostStories != null) putBoolUnsafe(TBytesInfo.can_post_stories, canPostStories)
                if (canEditStories != null) putBoolUnsafe(TBytesInfo.can_edit_stories, canEditStories)
                if (canDeleteStories != null) putBoolUnsafe(TBytesInfo.can_delete_stories, canDeleteStories)
                if (canPostMessages != null) putBoolUnsafe(TBytesInfo.can_post_messages, canPostMessages)
                if (canEditMessages != null) putBoolUnsafe(TBytesInfo.can_edit_messages, canEditMessages)
                if (canPinMessages != null) putBoolUnsafe(TBytesInfo.can_pin_messages, canPinMessages)
                if (canManageTopics != null) putBoolUnsafe(TBytesInfo.can_manage_topics, canManageTopics)
                if (canManageDirectMessages != null) putBoolUnsafe(TBytesInfo.can_manage_direct_messages, canManageDirectMessages)
                if (canManageTags != null) putBoolUnsafe(TBytesInfo.can_manage_tags, canManageTags)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinAllForumTopicMessages,
            JsonByteBuffer(88).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setBusinessAccountUsernameBSP = BufferSizePredictor(30, 1073741824, 60, 120)
    override suspend fun setBusinessAccountUsername(
        businessConnectionId: String,
        username: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountUsername,
            run {
                val bbSize0 = setBusinessAccountUsernameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (username != null) putStringUnsafe(TBytesInfo.username, username)
                    setBusinessAccountUsernameBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setGameScoreBSP = BufferSizePredictor(71, 1073741824, 142, 284)
    override suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setGameScore,
            run {
                val bbSize0 = setGameScoreBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    putNumberUnsafe(TBytesInfo.score, score)
                    if (force != null) putBoolUnsafe(TBytesInfo.force, force)
                    if (disableEditMessage != null) putBoolUnsafe(TBytesInfo.disable_edit_message, disableEditMessage)
                    if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    setGameScoreBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendPaidMedia(
        chatId: ChatId,
        starCount: Long,
        media: List<InputPaidMedia>,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        payload: String?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendPaidMedia, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        mpb.writeNormalPart("star_count", starCount.toString())
        for (mIdx in media.indices) { media[mIdx].executeAll(mpb) }
        mpb.writeJsonPart("media", TSerials.aListInputPaidMedia, media, json)
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (payload != null) mpb.writeNormalPart("payload", payload)
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (showCaptionAboveMedia != null) mpb.writeNormalPart("show_caption_above_media", showCaptionAboveMedia.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun removeChatVerification(
        chatId: ChatId
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.removeChatVerification,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getBusinessConnectionBSP = BufferSizePredictor(22, 1073741824, 44, 88)
    override suspend fun getBusinessConnection(
        businessConnectionId: String
    ): TResult<BusinessConnection> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessConnection,
            run {
                val bbSize0 = getBusinessConnectionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    getBusinessConnectionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BusinessConnection>(
                json.decodeFromString(TSerials.sBusinessConnection, strResult).result
            )
        } else {
            TResultFailure<BusinessConnection>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendGameBSP = BufferSizePredictor(161, 1073741824, 322, 644)
    override suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendGame,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendGameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    putStringUnsafe(TBytesInfo.game_short_name, gameShortName)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendGameBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getUserGiftsBSP = BufferSizePredictor(141, 1073741824, 282, 564)
    override suspend fun getUserGifts(
        userId: Long,
        excludeUnlimited: Boolean?,
        excludeLimitedUpgradable: Boolean?,
        excludeLimitedNonUpgradable: Boolean?,
        excludeFromBlockchain: Boolean?,
        excludeUnique: Boolean?,
        sortByPrice: Boolean?,
        offset: String?,
        limit: Long?,
        requestOptions: RequestOptions?
    ): TResult<OwnedGifts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUserGifts,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getUserGiftsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.user_id, userId)
                    if (excludeUnlimited != null) putBoolUnsafe(TBytesInfo.exclude_unlimited, excludeUnlimited)
                    if (excludeLimitedUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_upgradable, excludeLimitedUpgradable)
                    if (excludeLimitedNonUpgradable != null) putBoolUnsafe(TBytesInfo.exclude_limited_non_upgradable, excludeLimitedNonUpgradable)
                    if (excludeFromBlockchain != null) putBoolUnsafe(TBytesInfo.exclude_from_blockchain, excludeFromBlockchain)
                    if (excludeUnique != null) putBoolUnsafe(TBytesInfo.exclude_unique, excludeUnique)
                    if (sortByPrice != null) putBoolUnsafe(TBytesInfo.sort_by_price, sortByPrice)
                    if (offset != null) putStringUnsafe(TBytesInfo.offset, offset)
                    if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                    if (requestOptions == null) getUserGiftsBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<OwnedGifts>(
                json.decodeFromString(TSerials.sOwnedGifts, strResult).result
            )
        } else {
            TResultFailure<OwnedGifts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.declineChatJoinRequest,
            JsonByteBuffer(78).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendVenueBSP = BufferSizePredictor(284, 1073741824, 568, 1136)
    override suspend fun sendVenue(
        chatId: ChatId,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        foursquareId: String?,
        foursquareType: String?,
        googlePlaceId: String?,
        googlePlaceType: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendVenue,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendVenueBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.latitude, latitude)
                    putNumberUnsafe(TBytesInfo.longitude, longitude)
                    putStringUnsafe(TBytesInfo.title, title)
                    putStringUnsafe(TBytesInfo.address, address)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                    if (foursquareId != null) putStringUnsafe(TBytesInfo.foursquare_id, foursquareId)
                    if (foursquareType != null) putStringUnsafe(TBytesInfo.foursquare_type, foursquareType)
                    if (googlePlaceId != null) putStringUnsafe(TBytesInfo.google_place_id, googlePlaceId)
                    if (googlePlaceType != null) putStringUnsafe(TBytesInfo.google_place_type, googlePlaceType)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendVenueBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val stopPollBSP = BufferSizePredictor(51, 1073741824, 102, 204)
    override suspend fun stopPoll(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Poll> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.stopPoll,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: stopPollBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) stopPollBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Poll>(
                json.decodeFromString(TSerials.sPoll, strResult).result
            )
        } else {
            TResultFailure<Poll>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.approveChatJoinRequest,
            JsonByteBuffer(78).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.user_id, userId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendAnimation(
        chatId: ChatId,
        animation: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: TelegramFile?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendAnimation, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        animation.asVertx().executeCustom(mpb, "animation")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (duration != null) mpb.writeNormalPart("duration", duration.toString())
        if (width != null) mpb.writeNormalPart("width", width.toString())
        if (height != null) mpb.writeNormalPart("height", height.toString())
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (showCaptionAboveMedia != null) mpb.writeNormalPart("show_caption_above_media", showCaptionAboveMedia.toString())
        if (hasSpoiler != null) mpb.writeNormalPart("has_spoiler", hasSpoiler.toString())
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val sendChecklistBSP = BufferSizePredictor(118, 1073741824, 236, 472)
    override suspend fun sendChecklist(
        businessConnectionId: String,
        chatId: Long,
        checklist: InputChecklist,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendChecklist,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendChecklistBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.chat_id, chatId)
                    putJsonObject(TBytesInfo.checklist, InputChecklist.serializer(), json, checklist)
                    if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                    if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) sendChecklistBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun declineSuggestedPost(
        chatId: Long,
        messageId: Long,
        comment: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.declineSuggestedPost,
            JsonByteBuffer(847).run {
                putNumberUnsafe(TBytesInfo.chat_id, chatId)
                putNumberUnsafe(TBytesInfo.message_id, messageId)
                if (comment != null) putStringUnsafe(TBytesInfo.comment, comment)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun forwardMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?
    ): TResult<List<MessageId>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.forwardMessages,
            JsonByteBuffer(2356).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<MessageId>>(
                json.decodeFromString(TSerials.sListMessageId, strResult).result
            )
        } else {
            TResultFailure<List<MessageId>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getStickerSetBSP = BufferSizePredictor(4, 1073741824, 8, 16)
    override suspend fun getStickerSet(
        name: String
    ): TResult<StickerSet> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getStickerSet,
            run {
                val bbSize0 = getStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.name, name)
                    getStickerSetBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StickerSet>(
                json.decodeFromString(TSerials.sStickerSet, strResult).result
            )
        } else {
            TResultFailure<StickerSet>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editMessageLiveLocationBSP = BufferSizePredictor(144, 1073741824, 288, 576)
    override suspend fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        livePeriod: Long?,
        horizontalAccuracy: Double?,
        heading: Long?,
        proximityAlertRadius: Long?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editMessageLiveLocation,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editMessageLiveLocationBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe(TBytesInfo.latitude, latitude)
                    putNumberUnsafe(TBytesInfo.longitude, longitude)
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    if (livePeriod != null) putNumberUnsafe(TBytesInfo.live_period, livePeriod)
                    if (horizontalAccuracy != null) putNumberUnsafe(TBytesInfo.horizontal_accuracy, horizontalAccuracy)
                    if (heading != null) putNumberUnsafe(TBytesInfo.heading, heading)
                    if (proximityAlertRadius != null) putNumberUnsafe(TBytesInfo.proximity_alert_radius, proximityAlertRadius)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) editMessageLiveLocationBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun sendAudio(
        chatId: ChatId,
        audio: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        performer: String?,
        title: String?,
        thumbnail: TelegramFile?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.sendAudio, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("chat_id", chatId.value)
        audio.asVertx().executeCustom(mpb, "audio")
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
        if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
        if (caption != null) mpb.writeNormalPart("caption", caption)
        if (parseMode != null) mpb.writeNormalPart("parse_mode", parseMode.value)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
        if (duration != null) mpb.writeNormalPart("duration", duration.toString())
        if (performer != null) mpb.writeNormalPart("performer", performer)
        if (title != null) mpb.writeNormalPart("title", title)
        thumbnail?.asVertx()?.executeCustom(mpb, "thumbnail")
        if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
        if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
        if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
        if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
        if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
        if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteStickerFromSetBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun deleteStickerFromSet(
        sticker: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStickerFromSet,
            run {
                val bbSize0 = deleteStickerFromSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.sticker, sticker)
                    deleteStickerFromSetBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteBusinessMessagesBSP = BufferSizePredictor(33, 1073741824, 66, 132)
    override suspend fun deleteBusinessMessages(
        businessConnectionId: String,
        messageIds: List<Long>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteBusinessMessages,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteBusinessMessagesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                    if (requestOptions == null) deleteBusinessMessagesBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteStickerSetBSP = BufferSizePredictor(4, 1073741824, 8, 16)
    override suspend fun deleteStickerSet(
        name: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStickerSet,
            run {
                val bbSize0 = deleteStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.name, name)
                    deleteStickerSetBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val repostStoryBSP = BufferSizePredictor(92, 1073741824, 184, 368)
    override suspend fun repostStory(
        businessConnectionId: String,
        fromChatId: Long,
        fromStoryId: Long,
        activePeriod: Long,
        postToChatPage: Boolean?,
        protectContent: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Story> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.repostStory,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: repostStoryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    putNumberUnsafe(TBytesInfo.from_chat_id, fromChatId)
                    putNumberUnsafe(TBytesInfo.from_story_id, fromStoryId)
                    putNumberUnsafe(TBytesInfo.active_period, activePeriod)
                    if (postToChatPage != null) putBoolUnsafe(TBytesInfo.post_to_chat_page, postToChatPage)
                    if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                    if (requestOptions == null) repostStoryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Story>(
                json.decodeFromString(TSerials.sStory, strResult).result
            )
        } else {
            TResultFailure<Story>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val answerWebAppQueryBSP = BufferSizePredictor(22, 1073741824, 44, 88)
    override suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult,
        requestOptions: RequestOptions?
    ): TResult<SentWebAppMessage> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerWebAppQuery,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: answerWebAppQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.web_app_query_id, webAppQueryId)
                    putJsonObject(TBytesInfo.result, InlineQueryResult.serializer(), json, result)
                    if (requestOptions == null) answerWebAppQueryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<SentWebAppMessage>(
                json.decodeFromString(TSerials.sSentWebAppMessage, strResult).result
            )
        } else {
            TResultFailure<SentWebAppMessage>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyStarBalance(): TResult<StarAmount> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getMyStarBalance, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StarAmount>(
                json.decodeFromString(TSerials.sStarAmount, strResult).result
            )
        } else {
            TResultFailure<StarAmount>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerSetTitleBSP = BufferSizePredictor(9, 1073741824, 18, 36)
    override suspend fun setStickerSetTitle(
        name: String,
        title: String
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerSetTitle,
            run {
                val bbSize0 = setStickerSetTitleBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.name, name)
                    putStringUnsafe(TBytesInfo.title, title)
                    setStickerSetTitleBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun exportChatInviteLink(
        chatId: ChatId
    ): TResult<String> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.exportChatInviteLink,
            JsonByteBuffer(47).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(TSerials.sString, strResult).result
            )
        } else {
            TResultFailure<String>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun editMessageMedia(
        media: InputMedia,
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.editMessageMedia, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        media.executeAll(mpb)
        mpb.writeJsonPart("media", InputMedia.serializer(), media, json)
        if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
        if (chatId != null) mpb.writeNormalPart("chat_id", chatId.value)
        if (messageId != null) mpb.writeNormalPart("message_id", messageId.toString())
        if (inlineMessageId != null) mpb.writeNormalPart("inline_message_id", inlineMessageId)
        if (replyMarkup != null) mpb.writeJsonPart("reply_markup", InlineKeyboardMarkup.serializer(), replyMarkup, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.closeForumTopic,
            JsonByteBuffer(88).run {
                putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.replaceStickerInSet, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("user_id", userId.toString())
        mpb.writeNormalPart("name", name)
        mpb.writeNormalPart("old_sticker", oldSticker)
        sticker.executeAll(mpb)
        mpb.writeJsonPart("sticker", InputSticker.serializer(), sticker, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val answerCallbackQueryBSP = BufferSizePredictor(44, 1073741824, 88, 176)
    override suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerCallbackQuery,
            run {
                val bbSize0 = answerCallbackQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe(TBytesInfo.callback_query_id, callbackQueryId)
                    if (text != null) putStringUnsafe(TBytesInfo.text, text)
                    if (showAlert != null) putBoolUnsafe(TBytesInfo.show_alert, showAlert)
                    if (url != null) putStringUnsafe(TBytesInfo.url, url)
                    if (cacheTime != null) putNumberUnsafe(TBytesInfo.cache_time, cacheTime)
                    answerCallbackQueryBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun setMyDescription(
        description: String?,
        languageCode: String?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyDescription,
            JsonByteBuffer(3111).run {
                if (description != null) putStringUnsafe(TBytesInfo.description, description)
                if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun approveSuggestedPost(
        chatId: Long,
        messageId: Long,
        sendDate: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.approveSuggestedPost,
            JsonByteBuffer(99).run {
                putNumberUnsafe(TBytesInfo.chat_id, chatId)
                putNumberUnsafe(TBytesInfo.message_id, messageId)
                if (sendDate != null) putNumberUnsafe(TBytesInfo.send_date, sendDate)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.addStickerToSet, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("user_id", userId.toString())
        mpb.writeNormalPart("name", name)
        sticker.executeAll(mpb)
        mpb.writeJsonPart("sticker", InputSticker.serializer(), sticker, json)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: StickerType?,
        needsRepainting: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.createNewStickerSet, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("user_id", userId.toString())
        mpb.writeNormalPart("name", name)
        mpb.writeNormalPart("title", title)
        for (mIdx in stickers.indices) { stickers[mIdx].executeAll(mpb) }
        mpb.writeJsonPart("stickers", TSerials.aListInputSticker, stickers, json)
        if (stickerType != null) mpb.writeNormalPart("sticker_type", stickerType.value)
        if (needsRepainting != null) mpb.writeNormalPart("needs_repainting", needsRepainting.toString())
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editMessageCaptionBSP = BufferSizePredictor(125, 1073741824, 250, 500)
    override suspend fun editMessageCaption(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        showCaptionAboveMedia: Boolean?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editMessageCaption,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editMessageCaptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                    if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                    if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                    if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                    if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                    if (parseMode != null) putStringUnsafe(TBytesInfo.parse_mode, parseMode.value)
                    if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                    if (showCaptionAboveMedia != null) putBoolUnsafe(TBytesInfo.show_caption_above_media, showCaptionAboveMedia)
                    if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    if (requestOptions == null) editMessageCaptionBSP.record(size9, bbSize0)
                    toByteArray()
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            val response = json.decodeFromString(JsonObject.serializer(), strResult)
            val resultObj = response["result"] ?: error("Response does not contain result")
            if (resultObj !is JsonObject) {
                TResult.Either.Second<Boolean>((resultObj as JsonPrimitive).content == "true")
            } else {
                TResult.Either.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResultFailureEither(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun logOut(): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.logOut, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

}

