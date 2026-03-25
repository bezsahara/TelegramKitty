package org.bezsahara.kittybot.telegram.client

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.client.file.createBoundary
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.utils.ParseMode
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.classes.core.MessageId
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
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementError
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import kotlinx.serialization.builtins.serializer
import org.bezsahara.kittybot.telegram.classes.chat.ChatPermissions
import org.bezsahara.kittybot.telegram.classes.chat.ChatInviteLink
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
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import org.bezsahara.kittybot.telegram.classes.inline.SentWebAppMessage
import org.bezsahara.kittybot.telegram.client.TPathCustom
import org.bezsahara.kittybot.telegram.classes.payments.StarTransactions
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.chat.ChatAdministratorRights
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlinx.serialization.builtins.ListSerializer
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.classes.bot.BotDescription
import org.bezsahara.kittybot.telegram.utils.ChatAction
import org.bezsahara.kittybot.telegram.classes.media.stickers.StickerSet
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
    private val deleteMessagesBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMessages,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteMessagesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putListOfNumberUnsafe("message_ids", messageIds)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteMessagesBSP.record(b00.size, bbSize0)
                    b00
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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
        name: String?,
        requestOptions: RequestOptions?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editChatSubscriptionInviteLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editChatSubscriptionInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("invite_link", inviteLink)
                    if (name != null) putStringUnsafe("name", name)
                    val b00 = toByteArray()
                    if (requestOptions == null) editChatSubscriptionInviteLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteForumTopicBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_thread_id", messageThreadId)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteForumTopicBSP.record(b00.size, bbSize0)
                    b00
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

    private val readBusinessMessageBSP = BufferSizePredictor(39, 1073741824, 78, 156)
    override suspend fun readBusinessMessage(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.readBusinessMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: readBusinessMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("chat_id", chatId)
                    putNumberUnsafe("message_id", messageId)
                    val b00 = toByteArray()
                    if (requestOptions == null) readBusinessMessageBSP.record(b00.size, bbSize0)
                    b00
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
        useIndependentChatPermissions: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatPermissions,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatPermissionsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putJsonObject("permissions", ChatPermissions.serializer(), json, permissions)
                    if (useIndependentChatPermissions != null) putBoolUnsafe("use_independent_chat_permissions", useIndependentChatPermissions)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatPermissionsBSP.record(b00.size, bbSize0)
                    b00
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
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) editMessageReplyMarkupBSP.record(b00.size, bbSize0)
                    b00
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

    private val banChatMemberBSP = BufferSizePredictor(39, 1073741824, 78, 156)
    override suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.banChatMember,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: banChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    if (untilDate != null) putNumberUnsafe("until_date", untilDate)
                    if (revokeMessages != null) putBoolUnsafe("revoke_messages", revokeMessages)
                    val b00 = toByteArray()
                    if (requestOptions == null) banChatMemberBSP.record(b00.size, bbSize0)
                    b00
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

    private val getBusinessAccountStarBalanceBSP = BufferSizePredictor(22, 1073741824, 44, 88)
    override suspend fun getBusinessAccountStarBalance(
        businessConnectionId: String,
        requestOptions: RequestOptions?
    ): TResult<StarAmount> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessAccountStarBalance,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getBusinessAccountStarBalanceBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getBusinessAccountStarBalanceBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StarAmount>(
                json.decodeFromString(Ok.serializer(StarAmount.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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

    private val verifyChatBSP = BufferSizePredictor(25, 1073741824, 50, 100)
    override suspend fun verifyChat(
        chatId: ChatId,
        customDescription: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.verifyChat,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: verifyChatBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    if (customDescription != null) putStringUnsafe("custom_description", customDescription)
                    val b00 = toByteArray()
                    if (requestOptions == null) verifyChatBSP.record(b00.size, bbSize0)
                    b00
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

    private val editStoryBSP = BufferSizePredictor(75, 1073741824, 150, 300)
    override suspend fun editStory(
        businessConnectionId: String,
        storyId: Long,
        content: InputStoryContent,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        areas: List<StoryArea>?,
        requestOptions: RequestOptions?
    ): TResult<Story> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editStory,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editStoryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("story_id", storyId)
                    putJsonObject("content", InputStoryContent.serializer(), json, content)
                    if (caption != null) putStringUnsafe("caption", caption)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (captionEntities != null) putListOfJsonObjects("caption_entities", MessageEntity.serializer(), json, captionEntities)
                    if (areas != null) putListOfJsonObjects("areas", StoryArea.serializer(), json, areas)
                    val b00 = toByteArray()
                    if (requestOptions == null) editStoryBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Story>(
                json.decodeFromString(Ok.serializer(Story.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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

    private val copyMessageBSP = BufferSizePredictor(256, 1073741824, 512, 1024)
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
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("from_chat_id", fromChatId.value)
                    putNumberUnsafe("message_id", messageId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (videoStartTimestamp != null) putNumberUnsafe("video_start_timestamp", videoStartTimestamp)
                    if (caption != null) putStringUnsafe("caption", caption)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (captionEntities != null) putListOfJsonObjects("caption_entities", MessageEntity.serializer(), json, captionEntities)
                    if (showCaptionAboveMedia != null) putBoolUnsafe("show_caption_above_media", showCaptionAboveMedia)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) copyMessageBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<MessageId>(
                json.decodeFromString(Ok.serializer(MessageId.serializer()), strResult).result
            )
        } else {
            TResultFailure<MessageId>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val reopenForumTopicBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.reopenForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: reopenForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_thread_id", messageThreadId)
                    val b00 = toByteArray()
                    if (requestOptions == null) reopenForumTopicBSP.record(b00.size, bbSize0)
                    b00
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

    private val sendDiceBSP = BufferSizePredictor(200, 1073741824, 400, 800)
    override suspend fun sendDice(
        chatId: ChatId,
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
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendDice,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendDiceBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (emoji != null) putStringUnsafe("emoji", emoji)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendDiceBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val refundStarPaymentBSP = BufferSizePredictor(33, 1073741824, 66, 132)
    override suspend fun refundStarPayment(
        userId: Long,
        telegramPaymentChargeId: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.refundStarPayment,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: refundStarPaymentBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("telegram_payment_charge_id", telegramPaymentChargeId)
                    val b00 = toByteArray()
                    if (requestOptions == null) refundStarPaymentBSP.record(b00.size, bbSize0)
                    b00
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
                json.decodeFromString(Ok.serializer(Gifts.serializer()), strResult).result
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
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("latitude", latitude)
                    putNumberUnsafe("longitude", longitude)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (horizontalAccuracy != null) putNumberUnsafe("horizontal_accuracy", horizontalAccuracy)
                    if (livePeriod != null) putNumberUnsafe("live_period", livePeriod)
                    if (heading != null) putNumberUnsafe("heading", heading)
                    if (proximityAlertRadius != null) putNumberUnsafe("proximity_alert_radius", proximityAlertRadius)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendLocationBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatAdministratorCustomTitleBSP = BufferSizePredictor(26, 1073741824, 52, 104)
    override suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatAdministratorCustomTitle,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatAdministratorCustomTitleBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("custom_title", customTitle)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatAdministratorCustomTitleBSP.record(b00.size, bbSize0)
                    b00
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

    private val setUserEmojiStatusBSP = BufferSizePredictor(63, 1073741824, 126, 252)
    override suspend fun setUserEmojiStatus(
        userId: Long,
        emojiStatusCustomEmojiId: String?,
        emojiStatusExpirationDate: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setUserEmojiStatus,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setUserEmojiStatusBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    if (emojiStatusCustomEmojiId != null) putStringUnsafe("emoji_status_custom_emoji_id", emojiStatusCustomEmojiId)
                    if (emojiStatusExpirationDate != null) putNumberUnsafe("emoji_status_expiration_date", emojiStatusExpirationDate)
                    val b00 = toByteArray()
                    if (requestOptions == null) setUserEmojiStatusBSP.record(b00.size, bbSize0)
                    b00
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

    private val setChatTitleBSP = BufferSizePredictor(12, 1073741824, 24, 48)
    override suspend fun setChatTitle(
        chatId: ChatId,
        title: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatTitle,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatTitleBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("title", title)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatTitleBSP.record(b00.size, bbSize0)
                    b00
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

    private val setChatDescriptionBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun setChatDescription(
        chatId: ChatId,
        description: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatDescription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatDescriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    if (description != null) putStringUnsafe("description", description)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatDescriptionBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun getChatAdministrators(
        chatId: ChatId
    ): TResult<List<ChatMember>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatAdministrators,
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<ChatMember>>(
                json.decodeFromString(Ok.serializer(ListSerializer(ChatMember.serializer())), strResult).result
            )
        } else {
            TResultFailure<List<ChatMember>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatMemberCount,
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Long>(
                json.decodeFromString(Ok.serializer(Long.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putNumberUnsafe("user_id", userId)
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
        isPublic: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.removeBusinessAccountProfilePhoto,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: removeBusinessAccountProfilePhotoBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    if (isPublic != null) putBoolUnsafe("is_public", isPublic)
                    val b00 = toByteArray()
                    if (requestOptions == null) removeBusinessAccountProfilePhotoBSP.record(b00.size, bbSize0)
                    b00
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
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
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("chat_id", chatId)
                    putNumberUnsafe("message_id", messageId)
                    putJsonObject("checklist", InputChecklist.serializer(), json, checklist)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) editMessageChecklistBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
                    putNumberUnsafe("user_id", userId)
                    putListOfJsonObjects("errors", PassportElementError.serializer(), json, errors)
                    val b00 = toByteArray()
                    if (requestOptions == null) setPassportDataErrorsBSP.record(b00.size, bbSize0)
                    b00
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
        menuButton: MenuButton?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatMenuButton,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatMenuButtonBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (chatId != null) putNumberUnsafe("chat_id", chatId)
                    if (menuButton != null) putJsonObject("menu_button", MenuButton.serializer(), json, menuButton)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatMenuButtonBSP.record(b00.size, bbSize0)
                    b00
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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

    private val sendPollBSP = BufferSizePredictor(336, 1073741824, 672, 1344)
    override suspend fun sendPoll(
        chatId: ChatId,
        question: String,
        options: List<InputPollOption>,
        businessConnectionId: String?,
        messageThreadId: Long?,
        questionParseMode: String?,
        questionEntities: List<MessageEntity>?,
        isAnonymous: Boolean?,
        type: String?,
        allowsMultipleAnswers: Boolean?,
        correctOptionId: Long?,
        explanation: String?,
        explanationParseMode: String?,
        explanationEntities: List<MessageEntity>?,
        openPeriod: Long?,
        closeDate: Long?,
        isClosed: Boolean?,
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
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("question", question)
                    putListOfJsonObjects("options", InputPollOption.serializer(), json, options)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (questionParseMode != null) putStringUnsafe("question_parse_mode", questionParseMode)
                    if (questionEntities != null) putListOfJsonObjects("question_entities", MessageEntity.serializer(), json, questionEntities)
                    if (isAnonymous != null) putBoolUnsafe("is_anonymous", isAnonymous)
                    if (type != null) putStringUnsafe("type", type)
                    if (allowsMultipleAnswers != null) putBoolUnsafe("allows_multiple_answers", allowsMultipleAnswers)
                    if (correctOptionId != null) putNumberUnsafe("correct_option_id", correctOptionId)
                    if (explanation != null) putStringUnsafe("explanation", explanation)
                    if (explanationParseMode != null) putStringUnsafe("explanation_parse_mode", explanationParseMode)
                    if (explanationEntities != null) putListOfJsonObjects("explanation_entities", MessageEntity.serializer(), json, explanationEntities)
                    if (openPeriod != null) putNumberUnsafe("open_period", openPeriod)
                    if (closeDate != null) putNumberUnsafe("close_date", closeDate)
                    if (isClosed != null) putBoolUnsafe("is_closed", isClosed)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendPollBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                if (chatId != null) putNumberUnsafe("chat_id", chatId)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<MenuButton>(
                json.decodeFromString(Ok.serializer(MenuButton.serializer()), strResult).result
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
                    if (offset != null) putNumberUnsafe("offset", offset)
                    if (limit != null) putNumberUnsafe("limit", limit)
                    if (timeout != null) putNumberUnsafe("timeout", timeout)
                    if (allowedUpdates != null) putListOfStringUnsafe("allowed_updates", allowedUpdates)
                    val b00 = toByteArray()
                    if (requestOptions == null) getUpdatesBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            true
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Update>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Update.serializer())), strResult).result
            )
        } else {
            TResultFailure<List<Update>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setMyNameBSP = BufferSizePredictor(17, 1073741824, 34, 68)
    override suspend fun setMyName(
        name: String?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyName,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMyNameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (name != null) putStringUnsafe("name", name)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMyNameBSP.record(b00.size, bbSize0)
                    b00
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

    private val setBusinessAccountNameBSP = BufferSizePredictor(41, 1073741824, 82, 164)
    override suspend fun setBusinessAccountName(
        businessConnectionId: String,
        firstName: String,
        lastName: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountName,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setBusinessAccountNameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putStringUnsafe("first_name", firstName)
                    if (lastName != null) putStringUnsafe("last_name", lastName)
                    val b00 = toByteArray()
                    if (requestOptions == null) setBusinessAccountNameBSP.record(b00.size, bbSize0)
                    b00
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

    private val copyMessagesBSP = BufferSizePredictor(120, 1073741824, 240, 480)
    override suspend fun copyMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        removeCaption: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<List<MessageId>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.copyMessages,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: copyMessagesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("from_chat_id", fromChatId.value)
                    putListOfNumberUnsafe("message_ids", messageIds)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (removeCaption != null) putBoolUnsafe("remove_caption", removeCaption)
                    val b00 = toByteArray()
                    if (requestOptions == null) copyMessagesBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<MessageId>>(
                json.decodeFromString(Ok.serializer(ListSerializer(MessageId.serializer())), strResult).result
            )
        } else {
            TResultFailure<List<MessageId>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val unpinChatMessageBSP = BufferSizePredictor(39, 1073741824, 78, 156)
    override suspend fun unpinChatMessage(
        chatId: ChatId,
        businessConnectionId: String?,
        messageId: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinChatMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: unpinChatMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    val b00 = toByteArray()
                    if (requestOptions == null) unpinChatMessageBSP.record(b00.size, bbSize0)
                    b00
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
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) stopMessageLiveLocationBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("sticker", sticker)
                    putListOfStringUnsafe("emoji_list", emojiList)
                    val b00 = toByteArray()
                    if (requestOptions == null) setStickerEmojiListBSP.record(b00.size, bbSize0)
                    b00
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

    private val getMyDescriptionBSP = BufferSizePredictor(13, 1073741824, 26, 52)
    override suspend fun getMyDescription(
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<BotDescription> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyDescription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getMyDescriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) getMyDescriptionBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotDescription>(
                json.decodeFromString(Ok.serializer(BotDescription.serializer()), strResult).result
            )
        } else {
            TResultFailure<BotDescription>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getChatMemberBSP = BufferSizePredictor(14, 1073741824, 28, 56)
    override suspend fun getChatMember(
        chatId: ChatId,
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<ChatMember> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChatMember,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getChatMemberBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatMember>(
                json.decodeFromString(Ok.serializer(ChatMember.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putNumberUnsafe("user_id", userId)
                if (offset != null) putNumberUnsafe("offset", offset)
                if (limit != null) putNumberUnsafe("limit", limit)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<UserProfilePhotos>(
                json.decodeFromString(Ok.serializer(UserProfilePhotos.serializer()), strResult).result
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val createChatInviteLinkBSP = BufferSizePredictor(54, 1073741824, 108, 216)
    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createChatInviteLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: createChatInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    if (name != null) putStringUnsafe("name", name)
                    if (expireDate != null) putNumberUnsafe("expire_date", expireDate)
                    if (memberLimit != null) putNumberUnsafe("member_limit", memberLimit)
                    if (createsJoinRequest != null) putBoolUnsafe("creates_join_request", createsJoinRequest)
                    val b00 = toByteArray()
                    if (requestOptions == null) createChatInviteLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                if (offset != null) putNumberUnsafe("offset", offset)
                if (limit != null) putNumberUnsafe("limit", limit)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StarTransactions>(
                json.decodeFromString(Ok.serializer(StarTransactions.serializer()), strResult).result
            )
        } else {
            TResultFailure<StarTransactions>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setChatStickerSetBSP = BufferSizePredictor(23, 1073741824, 46, 92)
    override suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setChatStickerSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setChatStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("sticker_set_name", stickerSetName)
                    val b00 = toByteArray()
                    if (requestOptions == null) setChatStickerSetBSP.record(b00.size, bbSize0)
                    b00
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

    private val setMyShortDescriptionBSP = BufferSizePredictor(30, 1073741824, 60, 120)
    override suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyShortDescription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMyShortDescriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (shortDescription != null) putStringUnsafe("short_description", shortDescription)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMyShortDescriptionBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun uploadStickerFile(
        userId: Long,
        sticker: TelegramFile,
        stickerFormat: String
    ): TResult<File> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.uploadStickerFile, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("user_id", userId.toString())
        sticker.asVertx().executeCustom(mpb, "sticker")
        mpb.writeNormalPart("sticker_format", stickerFormat)
        mpb.finish()
        val result1 = resultPre1.endAndSend()
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<File>(
                json.decodeFromString(Ok.serializer(File.serializer()), strResult).result
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
        createsJoinRequest: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editChatInviteLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editChatInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("invite_link", inviteLink)
                    if (name != null) putStringUnsafe("name", name)
                    if (expireDate != null) putNumberUnsafe("expire_date", expireDate)
                    if (memberLimit != null) putNumberUnsafe("member_limit", memberLimit)
                    if (createsJoinRequest != null) putBoolUnsafe("creates_join_request", createsJoinRequest)
                    val b00 = toByteArray()
                    if (requestOptions == null) editChatInviteLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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

    override suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: String,
        thumbnail: TelegramFile?
    ): TResult<Boolean> = withContext(dispatcher) {
        val boundary44 = createBoundary()
        val resultPre1 = client.run { createMPRequest(tPathC.setStickerSetThumbnail, mpContentType(boundary44)) }
        val mpb = CustomMPB(resultPre1, boundary44)
        mpb.writeNormalPart("name", name)
        mpb.writeNormalPart("user_id", userId.toString())
        mpb.writeNormalPart("format", format)
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
            JsonByteBuffer(256).run {
                if (forChannels != null) putBoolUnsafe("for_channels", forChannels)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatAdministratorRights>(
                json.decodeFromString(Ok.serializer(ChatAdministratorRights.serializer()), strResult).result
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
                json.decodeFromString(Ok.serializer(User.serializer()), strResult).result
            )
        } else {
            TResultFailure<User>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerPositionInSetBSP = BufferSizePredictor(15, 1073741824, 30, 60)
    override suspend fun setStickerPositionInSet(
        sticker: String,
        position: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerPositionInSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setStickerPositionInSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("sticker", sticker)
                    putNumberUnsafe("position", position)
                    val b00 = toByteArray()
                    if (requestOptions == null) setStickerPositionInSetBSP.record(b00.size, bbSize0)
                    b00
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
        customEmojiId: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setCustomEmojiStickerSetThumbnail,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setCustomEmojiStickerSetThumbnailBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("name", name)
                    if (customEmojiId != null) putStringUnsafe("custom_emoji_id", customEmojiId)
                    val b00 = toByteArray()
                    if (requestOptions == null) setCustomEmojiStickerSetThumbnailBSP.record(b00.size, bbSize0)
                    b00
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
        disableNotification: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.pinChatMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: pinChatMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_id", messageId)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    val b00 = toByteArray()
                    if (requestOptions == null) pinChatMessageBSP.record(b00.size, bbSize0)
                    b00
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
                    putListOfStringUnsafe("custom_emoji_ids", customEmojiIds)
                    val b00 = toByteArray()
                    if (requestOptions == null) getCustomEmojiStickersBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<Sticker>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Sticker.serializer())), strResult).result
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
        starCount: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.upgradeGift,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: upgradeGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putStringUnsafe("owned_gift_id", ownedGiftId)
                    if (keepOriginalDetails != null) putBoolUnsafe("keep_original_details", keepOriginalDetails)
                    if (starCount != null) putNumberUnsafe("star_count", starCount)
                    val b00 = toByteArray()
                    if (requestOptions == null) upgradeGiftBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("inline_query_id", inlineQueryId)
                    putListOfJsonObjects("results", InlineQueryResult.serializer(), json, results)
                    if (cacheTime != null) putNumberUnsafe("cache_time", cacheTime)
                    if (isPersonal != null) putBoolUnsafe("is_personal", isPersonal)
                    if (nextOffset != null) putStringUnsafe("next_offset", nextOffset)
                    if (button != null) putJsonObject("button", InlineQueryResultsButton.serializer(), json, button)
                    val b00 = toByteArray()
                    if (requestOptions == null) answerInlineQueryBSP.record(b00.size, bbSize0)
                    b00
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
        inviteLink: String,
        requestOptions: RequestOptions?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.revokeChatInviteLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: revokeChatInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("invite_link", inviteLink)
                    val b00 = toByteArray()
                    if (requestOptions == null) revokeChatInviteLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val transferBusinessAccountStarsBSP = BufferSizePredictor(32, 1073741824, 64, 128)
    override suspend fun transferBusinessAccountStars(
        businessConnectionId: String,
        starCount: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.transferBusinessAccountStars,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: transferBusinessAccountStarsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("star_count", starCount)
                    val b00 = toByteArray()
                    if (requestOptions == null) transferBusinessAccountStarsBSP.record(b00.size, bbSize0)
                    b00
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
        errorMessage: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerPreCheckoutQuery,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: answerPreCheckoutQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("pre_checkout_query_id", preCheckoutQueryId)
                    putBoolUnsafe("ok", ok)
                    if (errorMessage != null) putStringUnsafe("error_message", errorMessage)
                    val b00 = toByteArray()
                    if (requestOptions == null) answerPreCheckoutQueryBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("text", text)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (entities != null) putListOfJsonObjects("entities", MessageEntity.serializer(), json, entities)
                    if (linkPreviewOptions != null) putJsonObject("link_preview_options", LinkPreviewOptions.serializer(), json, linkPreviewOptions)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendMessageBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
        messageThreadId: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendChatAction,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendChatActionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putJsonObject("action", ChatAction.serializer(), json, action)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendChatActionBSP.record(b00.size, bbSize0)
                    b00
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

    private val createChatSubscriptionInviteLinkBSP = BufferSizePredictor(48, 1073741824, 96, 192)
    override suspend fun createChatSubscriptionInviteLink(
        chatId: ChatId,
        subscriptionPeriod: Long,
        subscriptionPrice: Long,
        name: String?,
        requestOptions: RequestOptions?
    ): TResult<ChatInviteLink> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createChatSubscriptionInviteLink,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: createChatSubscriptionInviteLinkBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("subscription_period", subscriptionPeriod)
                    putNumberUnsafe("subscription_price", subscriptionPrice)
                    if (name != null) putStringUnsafe("name", name)
                    val b00 = toByteArray()
                    if (requestOptions == null) createChatSubscriptionInviteLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteMessageBSP = BufferSizePredictor(17, 1073741824, 34, 68)
    override suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_id", messageId)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteMessageBSP.record(b00.size, bbSize0)
                    b00
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

    private val getMyShortDescriptionBSP = BufferSizePredictor(13, 1073741824, 26, 52)
    override suspend fun getMyShortDescription(
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<BotShortDescription> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyShortDescription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getMyShortDescriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) getMyShortDescriptionBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotShortDescription>(
                json.decodeFromString(Ok.serializer(BotShortDescription.serializer()), strResult).result
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
                    putStringUnsafe("shipping_query_id", shippingQueryId)
                    putBoolUnsafe("ok", ok)
                    if (shippingOptions != null) putListOfJsonObjects("shipping_options", ShippingOption.serializer(), json, shippingOptions)
                    if (errorMessage != null) putStringUnsafe("error_message", errorMessage)
                    val b00 = toByteArray()
                    if (requestOptions == null) answerShippingQueryBSP.record(b00.size, bbSize0)
                    b00
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
        ownedGiftId: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.convertGiftToStars,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: convertGiftToStarsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putStringUnsafe("owned_gift_id", ownedGiftId)
                    val b00 = toByteArray()
                    if (requestOptions == null) convertGiftToStarsBSP.record(b00.size, bbSize0)
                    b00
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
                    putNumberUnsafe("user_id", userId)
                    putJsonObject("result", InlineQueryResult.serializer(), json, result)
                    if (allowUserChats != null) putBoolUnsafe("allow_user_chats", allowUserChats)
                    if (allowBotChats != null) putBoolUnsafe("allow_bot_chats", allowBotChats)
                    if (allowGroupChats != null) putBoolUnsafe("allow_group_chats", allowGroupChats)
                    if (allowChannelChats != null) putBoolUnsafe("allow_channel_chats", allowChannelChats)
                    val b00 = toByteArray()
                    if (requestOptions == null) savePreparedInlineMessageBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<PreparedInlineMessage>(
                json.decodeFromString(Ok.serializer(PreparedInlineMessage.serializer()), strResult).result
            )
        } else {
            TResultFailure<PreparedInlineMessage>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteStoryBSP = BufferSizePredictor(30, 1073741824, 60, 120)
    override suspend fun deleteStory(
        businessConnectionId: String,
        storyId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStory,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteStoryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("story_id", storyId)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteStoryBSP.record(b00.size, bbSize0)
                    b00
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
        textParseMode: String?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.giftPremiumSubscription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: giftPremiumSubscriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putNumberUnsafe("month_count", monthCount)
                    putNumberUnsafe("star_count", starCount)
                    if (text != null) putStringUnsafe("text", text)
                    if (textParseMode != null) putStringUnsafe("text_parse_mode", textParseMode)
                    if (textEntities != null) putListOfJsonObjects("text_entities", MessageEntity.serializer(), json, textEntities)
                    val b00 = toByteArray()
                    if (requestOptions == null) giftPremiumSubscriptionBSP.record(b00.size, bbSize0)
                    b00
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
        iconCustomEmojiId: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_thread_id", messageThreadId)
                    if (name != null) putStringUnsafe("name", name)
                    if (iconCustomEmojiId != null) putStringUnsafe("icon_custom_emoji_id", iconCustomEmojiId)
                    val b00 = toByteArray()
                    if (requestOptions == null) editForumTopicBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("phone_number", phoneNumber)
                    putStringUnsafe("first_name", firstName)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (lastName != null) putStringUnsafe("last_name", lastName)
                    if (vcard != null) putStringUnsafe("vcard", vcard)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendContactBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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
        untilDate: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.restrictChatMember,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: restrictChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    putJsonObject("permissions", ChatPermissions.serializer(), json, permissions)
                    if (useIndependentChatPermissions != null) putBoolUnsafe("use_independent_chat_permissions", useIndependentChatPermissions)
                    if (untilDate != null) putNumberUnsafe("until_date", untilDate)
                    val b00 = toByteArray()
                    if (requestOptions == null) restrictChatMemberBSP.record(b00.size, bbSize0)
                    b00
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

    private val setBusinessAccountProfilePhotoBSP = BufferSizePredictor(36, 1073741824, 72, 144)
    override suspend fun setBusinessAccountProfilePhoto(
        businessConnectionId: String,
        photo: InputProfilePhoto,
        isPublic: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountProfilePhoto,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setBusinessAccountProfilePhotoBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putJsonObject("photo", InputProfilePhoto.serializer(), json, photo)
                    if (isPublic != null) putBoolUnsafe("is_public", isPublic)
                    val b00 = toByteArray()
                    if (requestOptions == null) setBusinessAccountProfilePhotoBSP.record(b00.size, bbSize0)
                    b00
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

    private val getBusinessAccountGiftsBSP = BufferSizePredictor(120, 1073741824, 240, 480)
    override suspend fun getBusinessAccountGifts(
        businessConnectionId: String,
        excludeUnsaved: Boolean?,
        excludeSaved: Boolean?,
        excludeUnlimited: Boolean?,
        excludeLimited: Boolean?,
        excludeUnique: Boolean?,
        sortByPrice: Boolean?,
        offset: String?,
        limit: Long?,
        requestOptions: RequestOptions?
    ): TResult<OwnedGifts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessAccountGifts,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getBusinessAccountGiftsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    if (excludeUnsaved != null) putBoolUnsafe("exclude_unsaved", excludeUnsaved)
                    if (excludeSaved != null) putBoolUnsafe("exclude_saved", excludeSaved)
                    if (excludeUnlimited != null) putBoolUnsafe("exclude_unlimited", excludeUnlimited)
                    if (excludeLimited != null) putBoolUnsafe("exclude_limited", excludeLimited)
                    if (excludeUnique != null) putBoolUnsafe("exclude_unique", excludeUnique)
                    if (sortByPrice != null) putBoolUnsafe("sort_by_price", sortByPrice)
                    if (offset != null) putStringUnsafe("offset", offset)
                    if (limit != null) putNumberUnsafe("limit", limit)
                    val b00 = toByteArray()
                    if (requestOptions == null) getBusinessAccountGiftsBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<OwnedGifts>(
                json.decodeFromString(Ok.serializer(OwnedGifts.serializer()), strResult).result
            )
        } else {
            TResultFailure<OwnedGifts>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val forwardMessageBSP = BufferSizePredictor(151, 1073741824, 302, 604)
    override suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        videoStartTimestamp: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        suggestedPostParameters: SuggestedPostParameters?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.forwardMessage,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: forwardMessageBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("from_chat_id", fromChatId.value)
                    putNumberUnsafe("message_id", messageId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (videoStartTimestamp != null) putNumberUnsafe("video_start_timestamp", videoStartTimestamp)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    val b00 = toByteArray()
                    if (requestOptions == null) forwardMessageBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val editUserStarSubscriptionBSP = BufferSizePredictor(44, 1073741824, 88, 176)
    override suspend fun editUserStarSubscription(
        userId: Long,
        telegramPaymentChargeId: String,
        isCanceled: Boolean,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editUserStarSubscription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editUserStarSubscriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("telegram_payment_charge_id", telegramPaymentChargeId)
                    putBoolUnsafe("is_canceled", isCanceled)
                    val b00 = toByteArray()
                    if (requestOptions == null) editUserStarSubscriptionBSP.record(b00.size, bbSize0)
                    b00
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
                json.decodeFromString(Ok.serializer(ListSerializer(Sticker.serializer())), strResult).result
            )
        } else {
            TResultFailure<List<Sticker>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setBusinessAccountGiftSettingsBSP = BufferSizePredictor(57, 1073741824, 114, 228)
    override suspend fun setBusinessAccountGiftSettings(
        businessConnectionId: String,
        showGiftButton: Boolean,
        acceptedGiftTypes: AcceptedGiftTypes,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountGiftSettings,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setBusinessAccountGiftSettingsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putBoolUnsafe("show_gift_button", showGiftButton)
                    putJsonObject("accepted_gift_types", AcceptedGiftTypes.serializer(), json, acceptedGiftTypes)
                    val b00 = toByteArray()
                    if (requestOptions == null) setBusinessAccountGiftSettingsBSP.record(b00.size, bbSize0)
                    b00
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
        bio: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountBio,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setBusinessAccountBioBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    if (bio != null) putStringUnsafe("bio", bio)
                    val b00 = toByteArray()
                    if (requestOptions == null) setBusinessAccountBioBSP.record(b00.size, bbSize0)
                    b00
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

    private val unbanChatSenderChatBSP = BufferSizePredictor(21, 1073741824, 42, 84)
    override suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unbanChatSenderChat,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: unbanChatSenderChatBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("sender_chat_id", senderChatId)
                    val b00 = toByteArray()
                    if (requestOptions == null) unbanChatSenderChatBSP.record(b00.size, bbSize0)
                    b00
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

    private val getMyCommandsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<List<BotCommand>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyCommands,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getMyCommandsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (scope != null) putJsonObject("scope", BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) getMyCommandsBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<BotCommand>>(
                json.decodeFromString(Ok.serializer(ListSerializer(BotCommand.serializer())), strResult).result
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
        if (allowedUpdates != null) mpb.writeJsonPart("allowed_updates", ListSerializer(String.serializer()), allowedUpdates, json)
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

    private val setStickerMaskPositionBSP = BufferSizePredictor(20, 1073741824, 40, 80)
    override suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerMaskPosition,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setStickerMaskPositionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("sticker", sticker)
                    if (maskPosition != null) putJsonObject("mask_position", MaskPosition.serializer(), json, maskPosition)
                    val b00 = toByteArray()
                    if (requestOptions == null) setStickerMaskPositionBSP.record(b00.size, bbSize0)
                    b00
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

    private val verifyUserBSP = BufferSizePredictor(25, 1073741824, 50, 100)
    override suspend fun verifyUser(
        userId: Long,
        customDescription: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.verifyUser,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: verifyUserBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    if (customDescription != null) putStringUnsafe("custom_description", customDescription)
                    val b00 = toByteArray()
                    if (requestOptions == null) verifyUserBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteWebhook,
            JsonByteBuffer(256).run {
                if (dropPendingUpdates != null) putBoolUnsafe("drop_pending_updates", dropPendingUpdates)
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
        textParseMode: String?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendGift,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("gift_id", giftId)
                    if (userId != null) putNumberUnsafe("user_id", userId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (payForUpgrade != null) putBoolUnsafe("pay_for_upgrade", payForUpgrade)
                    if (text != null) putStringUnsafe("text", text)
                    if (textParseMode != null) putStringUnsafe("text_parse_mode", textParseMode)
                    if (textEntities != null) putListOfJsonObjects("text_entities", MessageEntity.serializer(), json, textEntities)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendGiftBSP.record(b00.size, bbSize0)
                    b00
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

    private val postStoryBSP = BufferSizePredictor(112, 1073741824, 224, 448)
    override suspend fun postStory(
        businessConnectionId: String,
        content: InputStoryContent,
        activePeriod: Long,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        areas: List<StoryArea>?,
        postToChatPage: Boolean?,
        protectContent: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Story> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.postStory,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: postStoryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putJsonObject("content", InputStoryContent.serializer(), json, content)
                    putNumberUnsafe("active_period", activePeriod)
                    if (caption != null) putStringUnsafe("caption", caption)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (captionEntities != null) putListOfJsonObjects("caption_entities", MessageEntity.serializer(), json, captionEntities)
                    if (areas != null) putListOfJsonObjects("areas", StoryArea.serializer(), json, areas)
                    if (postToChatPage != null) putBoolUnsafe("post_to_chat_page", postToChatPage)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    val b00 = toByteArray()
                    if (requestOptions == null) postStoryBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Story>(
                json.decodeFromString(Ok.serializer(Story.serializer()), strResult).result
            )
        } else {
            TResultFailure<Story>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getFileBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun getFile(
        fileId: String,
        requestOptions: RequestOptions?
    ): TResult<File> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getFile,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getFileBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("file_id", fileId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getFileBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<File>(
                json.decodeFromString(Ok.serializer(File.serializer()), strResult).result
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
        starCount: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.transferGift,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: transferGiftBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putStringUnsafe("owned_gift_id", ownedGiftId)
                    putNumberUnsafe("new_owner_chat_id", newOwnerChatId)
                    if (starCount != null) putNumberUnsafe("star_count", starCount)
                    val b00 = toByteArray()
                    if (requestOptions == null) transferGiftBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("sticker", sticker)
                    if (keywords != null) putListOfStringUnsafe("keywords", keywords)
                    val b00 = toByteArray()
                    if (requestOptions == null) setStickerKeywordsBSP.record(b00.size, bbSize0)
                    b00
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

    private val getUserChatBoostsBSP = BufferSizePredictor(14, 1073741824, 28, 56)
    override suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<UserChatBoosts> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getUserChatBoosts,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getUserChatBoostsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getUserChatBoostsBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<UserChatBoosts>(
                json.decodeFromString(Ok.serializer(UserChatBoosts.serializer()), strResult).result
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
        inlineMessageId: String?,
        requestOptions: RequestOptions?
    ): TResult<List<GameHighScore>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getGameHighScores,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getGameHighScoresBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    if (chatId != null) putNumberUnsafe("chat_id", chatId)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getGameHighScoresBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<GameHighScore>>(
                json.decodeFromString(Ok.serializer(ListSerializer(GameHighScore.serializer())), strResult).result
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
                    putStringUnsafe("title", title)
                    putStringUnsafe("description", description)
                    putStringUnsafe("payload", payload)
                    putStringUnsafe("currency", currency.value)
                    putListOfJsonObjects("prices", LabeledPrice.serializer(), json, prices)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (providerToken != null) putStringUnsafe("provider_token", providerToken)
                    if (subscriptionPeriod != null) putNumberUnsafe("subscription_period", subscriptionPeriod)
                    if (maxTipAmount != null) putNumberUnsafe("max_tip_amount", maxTipAmount)
                    if (suggestedTipAmounts != null) putListOfNumberUnsafe("suggested_tip_amounts", suggestedTipAmounts)
                    if (providerData != null) putStringUnsafe("provider_data", providerData)
                    if (photoUrl != null) putStringUnsafe("photo_url", photoUrl)
                    if (photoSize != null) putNumberUnsafe("photo_size", photoSize)
                    if (photoWidth != null) putNumberUnsafe("photo_width", photoWidth)
                    if (photoHeight != null) putNumberUnsafe("photo_height", photoHeight)
                    if (needName != null) putBoolUnsafe("need_name", needName)
                    if (needPhoneNumber != null) putBoolUnsafe("need_phone_number", needPhoneNumber)
                    if (needEmail != null) putBoolUnsafe("need_email", needEmail)
                    if (needShippingAddress != null) putBoolUnsafe("need_shipping_address", needShippingAddress)
                    if (sendPhoneNumberToProvider != null) putBoolUnsafe("send_phone_number_to_provider", sendPhoneNumberToProvider)
                    if (sendEmailToProvider != null) putBoolUnsafe("send_email_to_provider", sendEmailToProvider)
                    if (isFlexible != null) putBoolUnsafe("is_flexible", isFlexible)
                    val b00 = toByteArray()
                    if (requestOptions == null) createInvoiceLinkBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(Ok.serializer(String.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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

    private val editGeneralForumTopicBSP = BufferSizePredictor(11, 1073741824, 22, 44)
    override suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.editGeneralForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: editGeneralForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("name", name)
                    val b00 = toByteArray()
                    if (requestOptions == null) editGeneralForumTopicBSP.record(b00.size, bbSize0)
                    b00
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val banChatSenderChatBSP = BufferSizePredictor(21, 1073741824, 42, 84)
    override suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.banChatSenderChat,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: banChatSenderChatBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("sender_chat_id", senderChatId)
                    val b00 = toByteArray()
                    if (requestOptions == null) banChatSenderChatBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun getWebhookInfo(): TResult<WebhookInfo> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(tPathC.getWebhookInfo, "{}".toByteArray(Charsets.UTF_8), false)
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<WebhookInfo>(
                json.decodeFromString(Ok.serializer(WebhookInfo.serializer()), strResult).result
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
                    putListOfJsonObjects("commands", BotCommand.serializer(), json, commands)
                    if (scope != null) putJsonObject("scope", BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMyCommandsBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("text", text)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (entities != null) putListOfJsonObjects("entities", MessageEntity.serializer(), json, entities)
                    if (linkPreviewOptions != null) putJsonObject("link_preview_options", LinkPreviewOptions.serializer(), json, linkPreviewOptions)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) editMessageTextBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("title", title)
                    putStringUnsafe("description", description)
                    putStringUnsafe("payload", payload)
                    putStringUnsafe("currency", currency.value)
                    putListOfJsonObjects("prices", LabeledPrice.serializer(), json, prices)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (providerToken != null) putStringUnsafe("provider_token", providerToken)
                    if (maxTipAmount != null) putNumberUnsafe("max_tip_amount", maxTipAmount)
                    if (suggestedTipAmounts != null) putListOfNumberUnsafe("suggested_tip_amounts", suggestedTipAmounts)
                    if (startParameter != null) putStringUnsafe("start_parameter", startParameter)
                    if (providerData != null) putStringUnsafe("provider_data", providerData)
                    if (photoUrl != null) putStringUnsafe("photo_url", photoUrl)
                    if (photoSize != null) putNumberUnsafe("photo_size", photoSize)
                    if (photoWidth != null) putNumberUnsafe("photo_width", photoWidth)
                    if (photoHeight != null) putNumberUnsafe("photo_height", photoHeight)
                    if (needName != null) putBoolUnsafe("need_name", needName)
                    if (needPhoneNumber != null) putBoolUnsafe("need_phone_number", needPhoneNumber)
                    if (needEmail != null) putBoolUnsafe("need_email", needEmail)
                    if (needShippingAddress != null) putBoolUnsafe("need_shipping_address", needShippingAddress)
                    if (sendPhoneNumberToProvider != null) putBoolUnsafe("send_phone_number_to_provider", sendPhoneNumberToProvider)
                    if (sendEmailToProvider != null) putBoolUnsafe("send_email_to_provider", sendEmailToProvider)
                    if (isFlexible != null) putBoolUnsafe("is_flexible", isFlexible)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendInvoiceBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getMyNameBSP = BufferSizePredictor(13, 1073741824, 26, 52)
    override suspend fun getMyName(
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<BotName> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getMyName,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getMyNameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) getMyNameBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BotName>(
                json.decodeFromString(Ok.serializer(BotName.serializer()), strResult).result
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
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_id", messageId)
                    if (reaction != null) putListOfJsonObjects("reaction", ReactionType.serializer(), json, reaction)
                    if (isBig != null) putBoolUnsafe("is_big", isBig)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMessageReactionBSP.record(b00.size, bbSize0)
                    b00
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

    private val unbanChatMemberBSP = BufferSizePredictor(28, 1073741824, 56, 112)
    override suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unbanChatMember,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: unbanChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    if (onlyIfBanned != null) putBoolUnsafe("only_if_banned", onlyIfBanned)
                    val b00 = toByteArray()
                    if (requestOptions == null) unbanChatMemberBSP.record(b00.size, bbSize0)
                    b00
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setMyDefaultAdministratorRightsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyDefaultAdministratorRights,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMyDefaultAdministratorRightsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (rights != null) putJsonObject("rights", ChatAdministratorRights.serializer(), json, rights)
                    if (forChannels != null) putBoolUnsafe("for_channels", forChannels)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMyDefaultAdministratorRightsBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getChat,
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ChatFullInfo>(
                json.decodeFromString(Ok.serializer(ChatFullInfo.serializer()), strResult).result
            )
        } else {
            TResultFailure<ChatFullInfo>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteMyCommandsBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteMyCommands,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteMyCommandsBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (scope != null) putJsonObject("scope", BotCommandScope.serializer(), json, scope)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteMyCommandsBSP.record(b00.size, bbSize0)
                    b00
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
        iconCustomEmojiId: String?,
        requestOptions: RequestOptions?
    ): TResult<ForumTopic> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: createForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("name", name)
                    if (iconColor != null) putNumberUnsafe("icon_color", iconColor)
                    if (iconCustomEmojiId != null) putStringUnsafe("icon_custom_emoji_id", iconCustomEmojiId)
                    val b00 = toByteArray()
                    if (requestOptions == null) createForumTopicBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<ForumTopic>(
                json.decodeFromString(Ok.serializer(ForumTopic.serializer()), strResult).result
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
                json.decodeFromString(Ok.serializer(ListSerializer(Message.serializer())), strResult).result
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val promoteChatMemberBSP = BufferSizePredictor(295, 1073741824, 590, 1180)
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
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.promoteChatMember,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: promoteChatMemberBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    if (isAnonymous != null) putBoolUnsafe("is_anonymous", isAnonymous)
                    if (canManageChat != null) putBoolUnsafe("can_manage_chat", canManageChat)
                    if (canDeleteMessages != null) putBoolUnsafe("can_delete_messages", canDeleteMessages)
                    if (canManageVideoChats != null) putBoolUnsafe("can_manage_video_chats", canManageVideoChats)
                    if (canRestrictMembers != null) putBoolUnsafe("can_restrict_members", canRestrictMembers)
                    if (canPromoteMembers != null) putBoolUnsafe("can_promote_members", canPromoteMembers)
                    if (canChangeInfo != null) putBoolUnsafe("can_change_info", canChangeInfo)
                    if (canInviteUsers != null) putBoolUnsafe("can_invite_users", canInviteUsers)
                    if (canPostStories != null) putBoolUnsafe("can_post_stories", canPostStories)
                    if (canEditStories != null) putBoolUnsafe("can_edit_stories", canEditStories)
                    if (canDeleteStories != null) putBoolUnsafe("can_delete_stories", canDeleteStories)
                    if (canPostMessages != null) putBoolUnsafe("can_post_messages", canPostMessages)
                    if (canEditMessages != null) putBoolUnsafe("can_edit_messages", canEditMessages)
                    if (canPinMessages != null) putBoolUnsafe("can_pin_messages", canPinMessages)
                    if (canManageTopics != null) putBoolUnsafe("can_manage_topics", canManageTopics)
                    if (canManageDirectMessages != null) putBoolUnsafe("can_manage_direct_messages", canManageDirectMessages)
                    val b00 = toByteArray()
                    if (requestOptions == null) promoteChatMemberBSP.record(b00.size, bbSize0)
                    b00
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

    private val unpinAllForumTopicMessagesBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.unpinAllForumTopicMessages,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: unpinAllForumTopicMessagesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_thread_id", messageThreadId)
                    val b00 = toByteArray()
                    if (requestOptions == null) unpinAllForumTopicMessagesBSP.record(b00.size, bbSize0)
                    b00
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

    private val setBusinessAccountUsernameBSP = BufferSizePredictor(30, 1073741824, 60, 120)
    override suspend fun setBusinessAccountUsername(
        businessConnectionId: String,
        username: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setBusinessAccountUsername,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setBusinessAccountUsernameBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    if (username != null) putStringUnsafe("username", username)
                    val b00 = toByteArray()
                    if (requestOptions == null) setBusinessAccountUsernameBSP.record(b00.size, bbSize0)
                    b00
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
        inlineMessageId: String?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setGameScore,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setGameScoreBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putNumberUnsafe("score", score)
                    if (force != null) putBoolUnsafe("force", force)
                    if (disableEditMessage != null) putBoolUnsafe("disable_edit_message", disableEditMessage)
                    if (chatId != null) putNumberUnsafe("chat_id", chatId)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    val b00 = toByteArray()
                    if (requestOptions == null) setGameScoreBSP.record(b00.size, bbSize0)
                    b00
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

    private val sendPaidMediaBSP = BufferSizePredictor(257, 1073741824, 514, 1028)
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
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.sendPaidMedia,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: sendPaidMediaBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("star_count", starCount)
                    putListOfJsonObjects("media", InputPaidMedia.serializer(), json, media)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (payload != null) putStringUnsafe("payload", payload)
                    if (caption != null) putStringUnsafe("caption", caption)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (captionEntities != null) putListOfJsonObjects("caption_entities", MessageEntity.serializer(), json, captionEntities)
                    if (showCaptionAboveMedia != null) putBoolUnsafe("show_caption_above_media", showCaptionAboveMedia)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendPaidMediaBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
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
        businessConnectionId: String,
        requestOptions: RequestOptions?
    ): TResult<BusinessConnection> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getBusinessConnection,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getBusinessConnectionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    val b00 = toByteArray()
                    if (requestOptions == null) getBusinessConnectionBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<BusinessConnection>(
                json.decodeFromString(Ok.serializer(BusinessConnection.serializer()), strResult).result
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
                    putNumberUnsafe("chat_id", chatId)
                    putStringUnsafe("game_short_name", gameShortName)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendGameBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val declineChatJoinRequestBSP = BufferSizePredictor(14, 1073741824, 28, 56)
    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.declineChatJoinRequest,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: declineChatJoinRequestBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    val b00 = toByteArray()
                    if (requestOptions == null) declineChatJoinRequestBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("latitude", latitude)
                    putNumberUnsafe("longitude", longitude)
                    putStringUnsafe("title", title)
                    putStringUnsafe("address", address)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (foursquareId != null) putStringUnsafe("foursquare_id", foursquareId)
                    if (foursquareType != null) putStringUnsafe("foursquare_type", foursquareType)
                    if (googlePlaceId != null) putStringUnsafe("google_place_id", googlePlaceId)
                    if (googlePlaceType != null) putStringUnsafe("google_place_type", googlePlaceType)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (allowPaidBroadcast != null) putBoolUnsafe("allow_paid_broadcast", allowPaidBroadcast)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (suggestedPostParameters != null) putJsonObject("suggested_post_parameters", SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", ReplyMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendVenueBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_id", messageId)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) stopPollBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Poll>(
                json.decodeFromString(Ok.serializer(Poll.serializer()), strResult).result
            )
        } else {
            TResultFailure<Poll>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val approveChatJoinRequestBSP = BufferSizePredictor(14, 1073741824, 28, 56)
    override suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.approveChatJoinRequest,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: approveChatJoinRequestBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("user_id", userId)
                    val b00 = toByteArray()
                    if (requestOptions == null) approveChatJoinRequestBSP.record(b00.size, bbSize0)
                    b00
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
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
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putNumberUnsafe("chat_id", chatId)
                    putJsonObject("checklist", InputChecklist.serializer(), json, checklist)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    if (messageEffectId != null) putStringUnsafe("message_effect_id", messageEffectId)
                    if (replyParameters != null) putJsonObject("reply_parameters", ReplyParameters.serializer(), json, replyParameters)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) sendChecklistBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val declineSuggestedPostBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun declineSuggestedPost(
        chatId: Long,
        messageId: Long,
        comment: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.declineSuggestedPost,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: declineSuggestedPostBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("chat_id", chatId)
                    putNumberUnsafe("message_id", messageId)
                    if (comment != null) putStringUnsafe("comment", comment)
                    val b00 = toByteArray()
                    if (requestOptions == null) declineSuggestedPostBSP.record(b00.size, bbSize0)
                    b00
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

    private val forwardMessagesBSP = BufferSizePredictor(106, 1073741824, 212, 424)
    override suspend fun forwardMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<List<MessageId>> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.forwardMessages,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: forwardMessagesBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putStringUnsafe("from_chat_id", fromChatId.value)
                    putListOfNumberUnsafe("message_ids", messageIds)
                    if (messageThreadId != null) putNumberUnsafe("message_thread_id", messageThreadId)
                    if (directMessagesTopicId != null) putNumberUnsafe("direct_messages_topic_id", directMessagesTopicId)
                    if (disableNotification != null) putBoolUnsafe("disable_notification", disableNotification)
                    if (protectContent != null) putBoolUnsafe("protect_content", protectContent)
                    val b00 = toByteArray()
                    if (requestOptions == null) forwardMessagesBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<List<MessageId>>(
                json.decodeFromString(Ok.serializer(ListSerializer(MessageId.serializer())), strResult).result
            )
        } else {
            TResultFailure<List<MessageId>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val getStickerSetBSP = BufferSizePredictor(4, 1073741824, 8, 16)
    override suspend fun getStickerSet(
        name: String,
        requestOptions: RequestOptions?
    ): TResult<StickerSet> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.getStickerSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: getStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("name", name)
                    val b00 = toByteArray()
                    if (requestOptions == null) getStickerSetBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<StickerSet>(
                json.decodeFromString(Ok.serializer(StickerSet.serializer()), strResult).result
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
                    putNumberUnsafe("latitude", latitude)
                    putNumberUnsafe("longitude", longitude)
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    if (livePeriod != null) putNumberUnsafe("live_period", livePeriod)
                    if (horizontalAccuracy != null) putNumberUnsafe("horizontal_accuracy", horizontalAccuracy)
                    if (heading != null) putNumberUnsafe("heading", heading)
                    if (proximityAlertRadius != null) putNumberUnsafe("proximity_alert_radius", proximityAlertRadius)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) editMessageLiveLocationBSP.record(b00.size, bbSize0)
                    b00
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
        if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
        if (captionEntities != null) mpb.writeJsonPart("caption_entities", ListSerializer(MessageEntity.serializer()), captionEntities, json)
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
                json.decodeFromString(Ok.serializer(Message.serializer()), strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val deleteStickerFromSetBSP = BufferSizePredictor(7, 1073741824, 14, 28)
    override suspend fun deleteStickerFromSet(
        sticker: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStickerFromSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteStickerFromSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("sticker", sticker)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteStickerFromSetBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("business_connection_id", businessConnectionId)
                    putListOfNumberUnsafe("message_ids", messageIds)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteBusinessMessagesBSP.record(b00.size, bbSize0)
                    b00
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
        name: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.deleteStickerSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: deleteStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("name", name)
                    val b00 = toByteArray()
                    if (requestOptions == null) deleteStickerSetBSP.record(b00.size, bbSize0)
                    b00
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
                    putStringUnsafe("web_app_query_id", webAppQueryId)
                    putJsonObject("result", InlineQueryResult.serializer(), json, result)
                    val b00 = toByteArray()
                    if (requestOptions == null) answerWebAppQueryBSP.record(b00.size, bbSize0)
                    b00
                }
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<SentWebAppMessage>(
                json.decodeFromString(Ok.serializer(SentWebAppMessage.serializer()), strResult).result
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
                json.decodeFromString(Ok.serializer(StarAmount.serializer()), strResult).result
            )
        } else {
            TResultFailure<StarAmount>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val setStickerSetTitleBSP = BufferSizePredictor(9, 1073741824, 18, 36)
    override suspend fun setStickerSetTitle(
        name: String,
        title: String,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setStickerSetTitle,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setStickerSetTitleBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("name", name)
                    putStringUnsafe("title", title)
                    val b00 = toByteArray()
                    if (requestOptions == null) setStickerSetTitleBSP.record(b00.size, bbSize0)
                    b00
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
            JsonByteBuffer(256).run {
                putStringUnsafe("chat_id", chatId.value)
                toByteArray()
            },
            false
        )
        val strResult = result1.body().toString(Charsets.UTF_8)
        return@withContext if (result1.statusCode() in 200..299) {
            TResult<String>(
                json.decodeFromString(Ok.serializer(String.serializer()), strResult).result
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

    private val closeForumTopicBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.closeForumTopic,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: closeForumTopicBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("chat_id", chatId.value)
                    putNumberUnsafe("message_thread_id", messageThreadId)
                    val b00 = toByteArray()
                    if (requestOptions == null) closeForumTopicBSP.record(b00.size, bbSize0)
                    b00
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

    private val replaceStickerInSetBSP = BufferSizePredictor(29, 1073741824, 58, 116)
    override suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.replaceStickerInSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: replaceStickerInSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("name", name)
                    putStringUnsafe("old_sticker", oldSticker)
                    putJsonObject("sticker", InputSticker.serializer(), json, sticker)
                    val b00 = toByteArray()
                    if (requestOptions == null) replaceStickerInSetBSP.record(b00.size, bbSize0)
                    b00
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

    private val answerCallbackQueryBSP = BufferSizePredictor(44, 1073741824, 88, 176)
    override suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.answerCallbackQuery,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: answerCallbackQueryBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putStringUnsafe("callback_query_id", callbackQueryId)
                    if (text != null) putStringUnsafe("text", text)
                    if (showAlert != null) putBoolUnsafe("show_alert", showAlert)
                    if (url != null) putStringUnsafe("url", url)
                    if (cacheTime != null) putNumberUnsafe("cache_time", cacheTime)
                    val b00 = toByteArray()
                    if (requestOptions == null) answerCallbackQueryBSP.record(b00.size, bbSize0)
                    b00
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

    private val setMyDescriptionBSP = BufferSizePredictor(24, 1073741824, 48, 96)
    override suspend fun setMyDescription(
        description: String?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.setMyDescription,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: setMyDescriptionBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    if (description != null) putStringUnsafe("description", description)
                    if (languageCode != null) putStringUnsafe("language_code", languageCode)
                    val b00 = toByteArray()
                    if (requestOptions == null) setMyDescriptionBSP.record(b00.size, bbSize0)
                    b00
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

    override suspend fun approveSuggestedPost(
        chatId: Long,
        messageId: Long,
        sendDate: Long?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.approveSuggestedPost,
            JsonByteBuffer(256).run {
                putNumberUnsafe("chat_id", chatId)
                putNumberUnsafe("message_id", messageId)
                if (sendDate != null) putNumberUnsafe("send_date", sendDate)
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

    private val addStickerToSetBSP = BufferSizePredictor(18, 1073741824, 36, 72)
    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.addStickerToSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: addStickerToSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("name", name)
                    putJsonObject("sticker", InputSticker.serializer(), json, sticker)
                    val b00 = toByteArray()
                    if (requestOptions == null) addStickerToSetBSP.record(b00.size, bbSize0)
                    b00
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

    private val createNewStickerSetBSP = BufferSizePredictor(52, 1073741824, 104, 208)
    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: String?,
        needsRepainting: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> = withContext(dispatcher) {
        val result1 = client.sendJSONRequest(
            tPathC.createNewStickerSet,
            run {
                val bbSize0 = requestOptions?.bufferSize ?: createNewStickerSetBSP.decideCapacity()
                JsonByteBuffer(bbSize0).run {
                    putNumberUnsafe("user_id", userId)
                    putStringUnsafe("name", name)
                    putStringUnsafe("title", title)
                    putListOfJsonObjects("stickers", InputSticker.serializer(), json, stickers)
                    if (stickerType != null) putStringUnsafe("sticker_type", stickerType)
                    if (needsRepainting != null) putBoolUnsafe("needs_repainting", needsRepainting)
                    val b00 = toByteArray()
                    if (requestOptions == null) createNewStickerSetBSP.record(b00.size, bbSize0)
                    b00
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
                    if (businessConnectionId != null) putStringUnsafe("business_connection_id", businessConnectionId)
                    if (chatId != null) putStringUnsafe("chat_id", chatId.value)
                    if (messageId != null) putNumberUnsafe("message_id", messageId)
                    if (inlineMessageId != null) putStringUnsafe("inline_message_id", inlineMessageId)
                    if (caption != null) putStringUnsafe("caption", caption)
                    if (parseMode != null) putJsonObject("parse_mode", ParseMode.serializer(), json, parseMode)
                    if (captionEntities != null) putListOfJsonObjects("caption_entities", MessageEntity.serializer(), json, captionEntities)
                    if (showCaptionAboveMedia != null) putBoolUnsafe("show_caption_above_media", showCaptionAboveMedia)
                    if (replyMarkup != null) putJsonObject("reply_markup", InlineKeyboardMarkup.serializer(), json, replyMarkup)
                    val b00 = toByteArray()
                    if (requestOptions == null) editMessageCaptionBSP.record(b00.size, bbSize0)
                    b00
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

