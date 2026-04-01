package org.bezsahara.kittybot.telegram.client

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.utils.ParseMode
import org.bezsahara.kittybot.telegram.utils.TBytesInfo
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.classes.core.MessageId
import org.bezsahara.kittybot.telegram.classes.inline.PreparedInlineMessage
import org.bezsahara.kittybot.telegram.classes.payments.ShippingOption
import io.vertx.core.http.HttpClientResponse
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
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.classes.bot.BotShortDescription
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementError
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import kotlinx.serialization.builtins.serializer
import org.bezsahara.kittybot.telegram.classes.chat.ChatPermissions
import org.bezsahara.kittybot.telegram.utils.TSerials
import org.bezsahara.kittybot.telegram.classes.chat.ChatInviteLink
import io.vertx.core.Vertx
import org.bezsahara.kittybot.telegram.classes.games.GameHighScore
import org.bezsahara.kittybot.telegram.classes.payments.StarAmount
import org.bezsahara.kittybot.telegram.client.TPath
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.telegram.classes.chat.boosts.UserChatBoosts
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.json.JsonObject
import org.bezsahara.kittybot.telegram.classes.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGifts
import kotlinx.coroutines.CoroutineDispatcher
import org.bezsahara.kittybot.telegram.classes.bot.BotName
import org.bezsahara.kittybot.telegram.utils.TResultFailure
import org.bezsahara.kittybot.telegram.classes.chat.ForumTopic
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.classes.gifts.Gifts
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostParameters
import io.vertx.kotlin.coroutines.coAwait
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import org.bezsahara.kittybot.telegram.classes.inline.SentWebAppMessage
import org.bezsahara.kittybot.telegram.classes.payments.StarTransactions
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.chat.ChatAdministratorRights
import io.vertx.core.internal.buffer.BufferInternal
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlinx.serialization.builtins.ListSerializer
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import org.bezsahara.kittybot.telegram.client.CodeAndResult
import org.bezsahara.kittybot.telegram.classes.bot.BotDescription
import org.bezsahara.kittybot.telegram.utils.ChatAction
import org.bezsahara.kittybot.telegram.classes.media.stickers.StickerSet
import org.bezsahara.kittybot.telegram.classes.gifts.AcceptedGiftTypes
import org.bezsahara.kittybot.telegram.utils.TResultFailureEither
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import io.vertx.core.http.HttpClientRequest
import io.netty.buffer.Unpooled
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.client.opt.BufferSizePredictor
import java.util.function.Function
import io.vertx.kotlin.coroutines.dispatcher
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import kotlinx.coroutines.withContext
import org.bezsahara.kittybot.telegram.utils.TResult.Either
import org.bezsahara.kittybot.telegram.classes.media.stickers.MaskPosition


class TApiClient internal constructor(
    internal val vertx: Vertx,
    internal val json: Json,
    internal val ro00: TPath
) : KittyBot() {
    val client = vertx.createHttpClient()!!
    val dispatcher = vertx.dispatcher()
    override suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(2163).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.setChatPhoto).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            photo.asVertx().execute(mpb, "photo")
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unpinAllGeneralForumTopicMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatInviteLink> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editChatSubscriptionInviteLink)
                .compose(Function { vReq ->
                    val bbSize0 = editChatSubscriptionInviteLinkBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        editChatSubscriptionInviteLinkBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(88).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.readBusinessMessage)
                .compose(Function { vReq ->
                    val bbSize0 = readBusinessMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        readBusinessMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatPermissions)
                .compose(Function { vReq ->
                    val bbSize0 = setChatPermissionsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putJsonObject(TBytesInfo.permissions, ChatPermissions.serializer(), json, permissions)
                        if (useIndependentChatPermissions != null) putBoolUnsafe(TBytesInfo.use_independent_chat_permissions, useIndependentChatPermissions)
                        setChatPermissionsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editMessageReplyMarkup)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: editMessageReplyMarkupBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) editMessageReplyMarkupBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.banChatMember)
                .compose(Function { vReq ->
                    JsonByteBuffer(136).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (untilDate != null) putNumberUnsafe(TBytesInfo.until_date, untilDate)
                        if (revokeMessages != null) putBoolUnsafe(TBytesInfo.revoke_messages, revokeMessages)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<StarAmount> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getBusinessAccountStarBalance)
                .compose(Function { vReq ->
                    val bbSize0 = getBusinessAccountStarBalanceBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        getBusinessAccountStarBalanceBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<StarAmount>(
                json.decodeFromString(TSerials.sStarAmount, strResult).result
            )
        } else {
            TResultFailure<StarAmount>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unhideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unhideGeneralForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.verifyChat)
                .compose(Function { vReq ->
                    JsonByteBuffer(491).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (customDescription != null) putStringUnsafe(TBytesInfo.custom_description, customDescription)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Story> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editStory)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: editStoryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putNumberUnsafe(TBytesInfo.story_id, storyId)
                        putJsonObject(TBytesInfo.content, InputStoryContent.serializer(), json, content)
                        if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                        if (areas != null) putListOfJsonObjects(TBytesInfo.areas, StoryArea.serializer(), json, areas)
                        if (requestOptions == null) editStoryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Story>(
                json.decodeFromString(TSerials.sStory, strResult).result
            )
        } else {
            TResultFailure<Story>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteChatPhoto(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteChatPhoto)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<MessageId> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.copyMessage)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: copyMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (videoStartTimestamp != null) putNumberUnsafe(TBytesInfo.video_start_timestamp, videoStartTimestamp)
                        if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                        if (showCaptionAboveMedia != null) putBoolUnsafe(TBytesInfo.show_caption_above_media, showCaptionAboveMedia)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                        if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                        if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) copyMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.reopenForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(88).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendDice)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: sendDiceBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (emoji != null) putStringUnsafe(TBytesInfo.emoji, emoji)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                        if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                        if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                        if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) sendDiceBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    private val refundStarPaymentBSP = BufferSizePredictor(33, 1073741824, 66, 132)
    override suspend fun refundStarPayment(
        userId: Long,
        telegramPaymentChargeId: String
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.refundStarPayment)
                .compose(Function { vReq ->
                    val bbSize0 = refundStarPaymentBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.telegram_payment_charge_id, telegramPaymentChargeId)
                        refundStarPaymentBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getAvailableGifts(): TResult<Gifts> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getAvailableGifts)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendLocation)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatAdministratorCustomTitle)
                .compose(Function { vReq ->
                    JsonByteBuffer(192).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.custom_title, customTitle)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setUserEmojiStatus)
                .compose(Function { vReq ->
                    val bbSize0 = setUserEmojiStatusBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (emojiStatusCustomEmojiId != null) putStringUnsafe(TBytesInfo.emoji_status_custom_emoji_id, emojiStatusCustomEmojiId)
                        if (emojiStatusExpirationDate != null) putNumberUnsafe(TBytesInfo.emoji_status_expiration_date, emojiStatusExpirationDate)
                        setUserEmojiStatusBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatTitle)
                .compose(Function { vReq ->
                    JsonByteBuffer(826).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.title, title)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatDescription)
                .compose(Function { vReq ->
                    JsonByteBuffer(1594).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (description != null) putStringUnsafe(TBytesInfo.description, description)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatAdministrators(
        chatId: ChatId
    ): TResult<List<ChatMember>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getChatAdministrators)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<List<ChatMember>>(
                json.decodeFromString(TSerials.sListChatMember, strResult).result
            )
        } else {
            TResultFailure<List<ChatMember>>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getChatMemberCount)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Long>(
                json.decodeFromString(TSerials.sLong, strResult).result
            )
        } else {
            TResultFailure<Long>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun removeUserVerification(
        userId: Long
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.removeUserVerification)
                .compose(Function { vReq ->
                    JsonByteBuffer(32).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.removeBusinessAccountProfilePhoto)
                .compose(Function { vReq ->
                    val bbSize0 = removeBusinessAccountProfilePhotoBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (isPublic != null) putBoolUnsafe(TBytesInfo.is_public, isPublic)
                        removeBusinessAccountProfilePhotoBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendPhoto).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            photo.asVertx().execute(mpb, "photo")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendSticker).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            sticker.asVertx().execute(mpb, "sticker")
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editMessageChecklist)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: editMessageChecklistBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        putJsonObject(TBytesInfo.checklist, InputChecklist.serializer(), json, checklist)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) editMessageChecklistBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setPassportDataErrors)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: setPassportDataErrorsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putListOfJsonObjects(TBytesInfo.errors, PassportElementError.serializer(), json, errors)
                        if (requestOptions == null) setPassportDataErrorsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatMenuButton)
                .compose(Function { vReq ->
                    val bbSize0 = setChatMenuButtonBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        if (menuButton != null) putJsonObject(TBytesInfo.menu_button, MenuButton.serializer(), json, menuButton)
                        setChatMenuButtonBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun hideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.hideGeneralForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendPoll)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: sendPollBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.question, question)
                        putListOfJsonObjects(TBytesInfo.options, InputPollOption.serializer(), json, options)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (questionParseMode != null) putStringUnsafe(TBytesInfo.question_parse_mode, questionParseMode)
                        if (questionEntities != null) putListOfJsonObjects(TBytesInfo.question_entities, MessageEntity.serializer(), json, questionEntities)
                        if (isAnonymous != null) putBoolUnsafe(TBytesInfo.is_anonymous, isAnonymous)
                        if (type != null) putStringUnsafe(TBytesInfo.type, type)
                        if (allowsMultipleAnswers != null) putBoolUnsafe(TBytesInfo.allows_multiple_answers, allowsMultipleAnswers)
                        if (correctOptionId != null) putNumberUnsafe(TBytesInfo.correct_option_id, correctOptionId)
                        if (explanation != null) putStringUnsafe(TBytesInfo.explanation, explanation)
                        if (explanationParseMode != null) putStringUnsafe(TBytesInfo.explanation_parse_mode, explanationParseMode)
                        if (explanationEntities != null) putListOfJsonObjects(TBytesInfo.explanation_entities, MessageEntity.serializer(), json, explanationEntities)
                        if (openPeriod != null) putNumberUnsafe(TBytesInfo.open_period, openPeriod)
                        if (closeDate != null) putNumberUnsafe(TBytesInfo.close_date, closeDate)
                        if (isClosed != null) putBoolUnsafe(TBytesInfo.is_closed, isClosed)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                        if (messageEffectId != null) putStringUnsafe(TBytesInfo.message_effect_id, messageEffectId)
                        if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) sendPollBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChatMenuButton(
        chatId: Long?
    ): TResult<MenuButton> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getChatMenuButton)
                .compose(Function { vReq ->
                    JsonByteBuffer(32).run {
                        if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<Update>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getUpdates)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: getUpdatesBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                        if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                        if (timeout != null) putNumberUnsafe(TBytesInfo.timeout, timeout)
                        if (allowedUpdates != null) putListOfStringUnsafe(TBytesInfo.allowed_updates, allowedUpdates)
                        if (requestOptions == null) getUpdatesBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMyName)
                .compose(Function { vReq ->
                    JsonByteBuffer(416).run {
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setBusinessAccountName)
                .compose(Function { vReq ->
                    val bbSize0 = setBusinessAccountNameBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putStringUnsafe(TBytesInfo.first_name, firstName)
                        if (lastName != null) putStringUnsafe(TBytesInfo.last_name, lastName)
                        setBusinessAccountNameBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<MessageId>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.copyMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(2379).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                        putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (removeCaption != null) putBoolUnsafe(TBytesInfo.remove_caption, removeCaption)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unpinChatMessage)
                .compose(Function { vReq ->
                    val bbSize0 = unpinChatMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        unpinChatMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.stopMessageLiveLocation)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: stopMessageLiveLocationBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) stopMessageLiveLocationBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setStickerEmojiList)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: setStickerEmojiListBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.sticker, sticker)
                        putListOfStringUnsafe(TBytesInfo.emoji_list, emojiList)
                        if (requestOptions == null) setStickerEmojiListBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyDescription(
        languageCode: String?
    ): TResult<BotDescription> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyDescription)
                .compose(Function { vReq ->
                    JsonByteBuffer(22).run {
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatMember> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getChatMember)
                .compose(Function { vReq ->
                    JsonByteBuffer(78).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<UserProfilePhotos> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getUserProfilePhotos)
                .compose(Function { vReq ->
                    JsonByteBuffer(91).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                        if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendDocument).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            document.asVertx().execute(mpb, "document")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatInviteLink> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.createChatInviteLink)
                .compose(Function { vReq ->
                    JsonByteBuffer(349).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        if (expireDate != null) putNumberUnsafe(TBytesInfo.expire_date, expireDate)
                        if (memberLimit != null) putNumberUnsafe(TBytesInfo.member_limit, memberLimit)
                        if (createsJoinRequest != null) putBoolUnsafe(TBytesInfo.creates_join_request, createsJoinRequest)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<StarTransactions> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getStarTransactions)
                .compose(Function { vReq ->
                    JsonByteBuffer(60).run {
                        if (offset != null) putNumberUnsafe(TBytesInfo.offset, offset)
                        if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setChatStickerSet)
                .compose(Function { vReq ->
                    val bbSize0 = setChatStickerSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.sticker_set_name, stickerSetName)
                        setChatStickerSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMyShortDescription)
                .compose(Function { vReq ->
                    JsonByteBuffer(765).run {
                        if (shortDescription != null) putStringUnsafe(TBytesInfo.short_description, shortDescription)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<File> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.uploadStickerFile).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("user_id", userId.toString())
            sticker.asVertx().execute(mpb, "sticker")
            mpb.writeNormalPart("sticker_format", stickerFormat)
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatInviteLink> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editChatInviteLink)
                .compose(Function { vReq ->
                    val bbSize0 = editChatInviteLinkBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        if (expireDate != null) putNumberUnsafe(TBytesInfo.expire_date, expireDate)
                        if (memberLimit != null) putNumberUnsafe(TBytesInfo.member_limit, memberLimit)
                        if (createsJoinRequest != null) putBoolUnsafe(TBytesInfo.creates_join_request, createsJoinRequest)
                        editChatInviteLinkBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<ChatInviteLink>(
                json.decodeFromString(TSerials.sChatInviteLink, strResult).result
            )
        } else {
            TResultFailure<ChatInviteLink>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun leaveChat(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.leaveChat)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun closeGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.closeGeneralForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.setStickerSetThumbnail).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("name", name)
            mpb.writeNormalPart("user_id", userId.toString())
            mpb.writeNormalPart("format", format)
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ): TResult<ChatAdministratorRights> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyDefaultAdministratorRights)
                .compose(Function { vReq ->
                    JsonByteBuffer(22).run {
                        if (forChannels != null) putBoolUnsafe(TBytesInfo.for_channels, forChannels)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<ChatAdministratorRights>(
                json.decodeFromString(TSerials.sChatAdministratorRights, strResult).result
            )
        } else {
            TResultFailure<ChatAdministratorRights>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMe(): TResult<User> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMe)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setStickerPositionInSet)
                .compose(Function { vReq ->
                    val bbSize0 = setStickerPositionInSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.sticker, sticker)
                        putNumberUnsafe(TBytesInfo.position, position)
                        setStickerPositionInSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setCustomEmojiStickerSetThumbnail)
                .compose(Function { vReq ->
                    val bbSize0 = setCustomEmojiStickerSetThumbnailBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.name, name)
                        if (customEmojiId != null) putStringUnsafe(TBytesInfo.custom_emoji_id, customEmojiId)
                        setCustomEmojiStickerSetThumbnailBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun close(): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.close)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.pinChatMessage)
                .compose(Function { vReq ->
                    val bbSize0 = pinChatMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        pinChatMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<Sticker>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getCustomEmojiStickers)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: getCustomEmojiStickersBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putListOfStringUnsafe(TBytesInfo.custom_emoji_ids, customEmojiIds)
                        if (requestOptions == null) getCustomEmojiStickersBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.upgradeGift)
                .compose(Function { vReq ->
                    val bbSize0 = upgradeGiftBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                        if (keepOriginalDetails != null) putBoolUnsafe(TBytesInfo.keep_original_details, keepOriginalDetails)
                        if (starCount != null) putNumberUnsafe(TBytesInfo.star_count, starCount)
                        upgradeGiftBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.answerInlineQuery)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: answerInlineQueryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.inline_query_id, inlineQueryId)
                        putListOfJsonObjects(TBytesInfo.results, InlineQueryResult.serializer(), json, results)
                        if (cacheTime != null) putNumberUnsafe(TBytesInfo.cache_time, cacheTime)
                        if (isPersonal != null) putBoolUnsafe(TBytesInfo.is_personal, isPersonal)
                        if (nextOffset != null) putStringUnsafe(TBytesInfo.next_offset, nextOffset)
                        if (button != null) putJsonObject(TBytesInfo.button, InlineQueryResultsButton.serializer(), json, button)
                        if (requestOptions == null) answerInlineQueryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatInviteLink> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.revokeChatInviteLink)
                .compose(Function { vReq ->
                    val bbSize0 = revokeChatInviteLinkBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.invite_link, inviteLink)
                        revokeChatInviteLinkBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.transferBusinessAccountStars)
                .compose(Function { vReq ->
                    val bbSize0 = transferBusinessAccountStarsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putNumberUnsafe(TBytesInfo.star_count, starCount)
                        transferBusinessAccountStarsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.answerPreCheckoutQuery)
                .compose(Function { vReq ->
                    val bbSize0 = answerPreCheckoutQueryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.pre_checkout_query_id, preCheckoutQueryId)
                        putBoolUnsafe(TBytesInfo.ok, ok)
                        if (errorMessage != null) putStringUnsafe(TBytesInfo.error_message, errorMessage)
                        answerPreCheckoutQueryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendMessage)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: sendMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.text, text)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendChatAction)
                .compose(Function { vReq ->
                    val bbSize0 = sendChatActionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putJsonObject(TBytesInfo.action, ChatAction.serializer(), json, action)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        sendChatActionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ChatInviteLink> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.createChatSubscriptionInviteLink)
                .compose(Function { vReq ->
                    JsonByteBuffer(334).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.subscription_period, subscriptionPeriod)
                        putNumberUnsafe(TBytesInfo.subscription_price, subscriptionPrice)
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteMessage)
                .compose(Function { vReq ->
                    JsonByteBuffer(81).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyShortDescription(
        languageCode: String?
    ): TResult<BotShortDescription> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyShortDescription)
                .compose(Function { vReq ->
                    JsonByteBuffer(22).run {
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.answerShippingQuery)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: answerShippingQueryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.shipping_query_id, shippingQueryId)
                        putBoolUnsafe(TBytesInfo.ok, ok)
                        if (shippingOptions != null) putListOfJsonObjects(TBytesInfo.shipping_options, ShippingOption.serializer(), json, shippingOptions)
                        if (errorMessage != null) putStringUnsafe(TBytesInfo.error_message, errorMessage)
                        if (requestOptions == null) answerShippingQueryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.convertGiftToStars)
                .compose(Function { vReq ->
                    val bbSize0 = convertGiftToStarsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                        convertGiftToStarsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<PreparedInlineMessage> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.savePreparedInlineMessage)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: savePreparedInlineMessageBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putJsonObject(TBytesInfo.result, InlineQueryResult.serializer(), json, result)
                        if (allowUserChats != null) putBoolUnsafe(TBytesInfo.allow_user_chats, allowUserChats)
                        if (allowBotChats != null) putBoolUnsafe(TBytesInfo.allow_bot_chats, allowBotChats)
                        if (allowGroupChats != null) putBoolUnsafe(TBytesInfo.allow_group_chats, allowGroupChats)
                        if (allowChannelChats != null) putBoolUnsafe(TBytesInfo.allow_channel_chats, allowChannelChats)
                        if (requestOptions == null) savePreparedInlineMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteStory)
                .compose(Function { vReq ->
                    val bbSize0 = deleteStoryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putNumberUnsafe(TBytesInfo.story_id, storyId)
                        deleteStoryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.giftPremiumSubscription)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: giftPremiumSubscriptionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putNumberUnsafe(TBytesInfo.month_count, monthCount)
                        putNumberUnsafe(TBytesInfo.star_count, starCount)
                        if (text != null) putStringUnsafe(TBytesInfo.text, text)
                        if (textParseMode != null) putStringUnsafe(TBytesInfo.text_parse_mode, textParseMode)
                        if (textEntities != null) putListOfJsonObjects(TBytesInfo.text_entities, MessageEntity.serializer(), json, textEntities)
                        if (requestOptions == null) giftPremiumSubscriptionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editForumTopic)
                .compose(Function { vReq ->
                    val bbSize0 = editForumTopicBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (name != null) putStringUnsafe(TBytesInfo.name, name)
                        if (iconCustomEmojiId != null) putStringUnsafe(TBytesInfo.icon_custom_emoji_id, iconCustomEmojiId)
                        editForumTopicBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendContact)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun unpinAllChatMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unpinAllChatMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.restrictChatMember)
                .compose(Function { vReq ->
                    val bbSize0 = restrictChatMemberBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putJsonObject(TBytesInfo.permissions, ChatPermissions.serializer(), json, permissions)
                        if (useIndependentChatPermissions != null) putBoolUnsafe(TBytesInfo.use_independent_chat_permissions, useIndependentChatPermissions)
                        if (untilDate != null) putNumberUnsafe(TBytesInfo.until_date, untilDate)
                        restrictChatMemberBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
        isPublic: Boolean?
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setBusinessAccountProfilePhoto)
                .compose(Function { vReq ->
                    val bbSize0 = setBusinessAccountProfilePhotoBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putJsonObject(TBytesInfo.photo, InputProfilePhoto.serializer(), json, photo)
                        if (isPublic != null) putBoolUnsafe(TBytesInfo.is_public, isPublic)
                        setBusinessAccountProfilePhotoBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
        limit: Long?
    ): TResult<OwnedGifts> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getBusinessAccountGifts)
                .compose(Function { vReq ->
                    val bbSize0 = getBusinessAccountGiftsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (excludeUnsaved != null) putBoolUnsafe(TBytesInfo.exclude_unsaved, excludeUnsaved)
                        if (excludeSaved != null) putBoolUnsafe(TBytesInfo.exclude_saved, excludeSaved)
                        if (excludeUnlimited != null) putBoolUnsafe(TBytesInfo.exclude_unlimited, excludeUnlimited)
                        if (excludeLimited != null) putBoolUnsafe(TBytesInfo.exclude_limited, excludeLimited)
                        if (excludeUnique != null) putBoolUnsafe(TBytesInfo.exclude_unique, excludeUnique)
                        if (sortByPrice != null) putBoolUnsafe(TBytesInfo.sort_by_price, sortByPrice)
                        if (offset != null) putStringUnsafe(TBytesInfo.offset, offset)
                        if (limit != null) putNumberUnsafe(TBytesInfo.limit, limit)
                        getBusinessAccountGiftsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<OwnedGifts>(
                json.decodeFromString(TSerials.sOwnedGifts, strResult).result
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
        suggestedPostParameters: SuggestedPostParameters?
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.forwardMessage)
                .compose(Function { vReq ->
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
                        if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                        forwardMessageBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editUserStarSubscription)
                .compose(Function { vReq ->
                    val bbSize0 = editUserStarSubscriptionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.telegram_payment_charge_id, telegramPaymentChargeId)
                        putBoolUnsafe(TBytesInfo.is_canceled, isCanceled)
                        editUserStarSubscriptionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getForumTopicIconStickers(): TResult<List<Sticker>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getForumTopicIconStickers)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setBusinessAccountGiftSettings)
                .compose(Function { vReq ->
                    val bbSize0 = setBusinessAccountGiftSettingsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putBoolUnsafe(TBytesInfo.show_gift_button, showGiftButton)
                        putJsonObject(TBytesInfo.accepted_gift_types, AcceptedGiftTypes.serializer(), json, acceptedGiftTypes)
                        setBusinessAccountGiftSettingsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setBusinessAccountBio)
                .compose(Function { vReq ->
                    val bbSize0 = setBusinessAccountBioBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (bio != null) putStringUnsafe(TBytesInfo.bio, bio)
                        setBusinessAccountBioBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unbanChatSenderChat)
                .compose(Function { vReq ->
                    JsonByteBuffer(85).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.sender_chat_id, senderChatId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<BotCommand>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyCommands)
                .compose(Function { vReq ->
                    val bbSize0 = getMyCommandsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        getMyCommandsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.setWebhook).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("url", url)
            certificate?.asVertx()?.execute(mpb, "certificate")
            if (ipAddress != null) mpb.writeNormalPart("ip_address", ipAddress)
            if (maxConnections != null) mpb.writeNormalPart("max_connections", maxConnections.toString())
            if (allowedUpdates != null) mpb.writeJsonPart("allowed_updates", TSerials.aListString, allowedUpdates, json)
            if (dropPendingUpdates != null) mpb.writeNormalPart("drop_pending_updates", dropPendingUpdates.toString())
            if (secretToken != null) mpb.writeNormalPart("secret_token", secretToken)
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setStickerMaskPosition)
                .compose(Function { vReq ->
                    val bbSize0 = setStickerMaskPositionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.sticker, sticker)
                        if (maskPosition != null) putJsonObject(TBytesInfo.mask_position, MaskPosition.serializer(), json, maskPosition)
                        setStickerMaskPositionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.verifyUser)
                .compose(Function { vReq ->
                    JsonByteBuffer(476).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (customDescription != null) putStringUnsafe(TBytesInfo.custom_description, customDescription)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteWebhook)
                .compose(Function { vReq ->
                    JsonByteBuffer(30).run {
                        if (dropPendingUpdates != null) putBoolUnsafe(TBytesInfo.drop_pending_updates, dropPendingUpdates)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendGift)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: sendGiftBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.gift_id, giftId)
                        if (userId != null) putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (payForUpgrade != null) putBoolUnsafe(TBytesInfo.pay_for_upgrade, payForUpgrade)
                        if (text != null) putStringUnsafe(TBytesInfo.text, text)
                        if (textParseMode != null) putStringUnsafe(TBytesInfo.text_parse_mode, textParseMode)
                        if (textEntities != null) putListOfJsonObjects(TBytesInfo.text_entities, MessageEntity.serializer(), json, textEntities)
                        if (requestOptions == null) sendGiftBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Story> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.postStory)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: postStoryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putJsonObject(TBytesInfo.content, InputStoryContent.serializer(), json, content)
                        putNumberUnsafe(TBytesInfo.active_period, activePeriod)
                        if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                        if (areas != null) putListOfJsonObjects(TBytesInfo.areas, StoryArea.serializer(), json, areas)
                        if (postToChatPage != null) putBoolUnsafe(TBytesInfo.post_to_chat_page, postToChatPage)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (requestOptions == null) postStoryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<File> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getFile)
                .compose(Function { vReq ->
                    val bbSize0 = getFileBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.file_id, fileId)
                        getFileBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.transferGift)
                .compose(Function { vReq ->
                    val bbSize0 = transferGiftBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putStringUnsafe(TBytesInfo.owned_gift_id, ownedGiftId)
                        putNumberUnsafe(TBytesInfo.new_owner_chat_id, newOwnerChatId)
                        if (starCount != null) putNumberUnsafe(TBytesInfo.star_count, starCount)
                        transferGiftBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setStickerKeywords)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: setStickerKeywordsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.sticker, sticker)
                        if (keywords != null) putListOfStringUnsafe(TBytesInfo.keywords, keywords)
                        if (requestOptions == null) setStickerKeywordsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<UserChatBoosts> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getUserChatBoosts)
                .compose(Function { vReq ->
                    JsonByteBuffer(78).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<GameHighScore>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getGameHighScores)
                .compose(Function { vReq ->
                    val bbSize0 = getGameHighScoresBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (chatId != null) putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                        getGameHighScoresBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<String> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.createInvoiceLink)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<String>(
                json.decodeFromString(TSerials.sString, strResult).result
            )
        } else {
            TResultFailure<String>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun reopenGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.reopenGeneralForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun deleteChatStickerSet(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteChatStickerSet)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editGeneralForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(825).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.name, name)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendVoice).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            voice.asVertx().execute(mpb, "voice")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.banChatSenderChat)
                .compose(Function { vReq ->
                    JsonByteBuffer(85).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.sender_chat_id, senderChatId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getWebhookInfo(): TResult<WebhookInfo> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getWebhookInfo)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMyCommands)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: setMyCommandsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putListOfJsonObjects(TBytesInfo.commands, BotCommand.serializer(), json, commands)
                        if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        if (requestOptions == null) setMyCommandsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editMessageText)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: editMessageTextBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.text, text)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (entities != null) putListOfJsonObjects(TBytesInfo.entities, MessageEntity.serializer(), json, entities)
                        if (linkPreviewOptions != null) putJsonObject(TBytesInfo.link_preview_options, LinkPreviewOptions.serializer(), json, linkPreviewOptions)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) editMessageTextBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendInvoice)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyName(
        languageCode: String?
    ): TResult<BotName> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyName)
                .compose(Function { vReq ->
                    JsonByteBuffer(22).run {
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMessageReaction)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: setMessageReactionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (reaction != null) putListOfJsonObjects(TBytesInfo.reaction, ReactionType.serializer(), json, reaction)
                        if (isBig != null) putBoolUnsafe(TBytesInfo.is_big, isBig)
                        if (requestOptions == null) setMessageReactionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unbanChatMember)
                .compose(Function { vReq ->
                    JsonByteBuffer(101).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        if (onlyIfBanned != null) putBoolUnsafe(TBytesInfo.only_if_banned, onlyIfBanned)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendVideoNote).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            videoNote.asVertx().execute(mpb, "video_note")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (duration != null) mpb.writeNormalPart("duration", duration.toString())
            if (length != null) mpb.writeNormalPart("length", length.toString())
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
            if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
            if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
            if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
            if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
            if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
            if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMyDefaultAdministratorRights)
                .compose(Function { vReq ->
                    val bbSize0 = setMyDefaultAdministratorRightsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (rights != null) putJsonObject(TBytesInfo.rights, ChatAdministratorRights.serializer(), json, rights)
                        if (forChannels != null) putBoolUnsafe(TBytesInfo.for_channels, forChannels)
                        setMyDefaultAdministratorRightsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getChat)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteMyCommands)
                .compose(Function { vReq ->
                    val bbSize0 = deleteMyCommandsBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (scope != null) putJsonObject(TBytesInfo.scope, BotCommandScope.serializer(), json, scope)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        deleteMyCommandsBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<ForumTopic> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.createForumTopic)
                .compose(Function { vReq ->
                    val bbSize0 = createForumTopicBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.name, name)
                        if (iconColor != null) putNumberUnsafe(TBytesInfo.icon_color, iconColor)
                        if (iconCustomEmojiId != null) putStringUnsafe(TBytesInfo.icon_custom_emoji_id, iconCustomEmojiId)
                        createForumTopicBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<Message>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendMediaGroup).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendVideo).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            video.asVertx().execute(mpb, "video")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (duration != null) mpb.writeNormalPart("duration", duration.toString())
            if (width != null) mpb.writeNormalPart("width", width.toString())
            if (height != null) mpb.writeNormalPart("height", height.toString())
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            cover?.asVertx()?.execute(mpb, "cover")
            if (startTimestamp != null) mpb.writeNormalPart("start_timestamp", startTimestamp.toString())
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
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
        canManageDirectMessages: Boolean?
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.promoteChatMember)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.unpinAllForumTopicMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(88).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setBusinessAccountUsername)
                .compose(Function { vReq ->
                    val bbSize0 = setBusinessAccountUsernameBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (username != null) putStringUnsafe(TBytesInfo.username, username)
                        setBusinessAccountUsernameBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setGameScore)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendPaidMedia)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: sendPaidMediaBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.star_count, starCount)
                        putListOfJsonObjects(TBytesInfo.media, InputPaidMedia.serializer(), json, media)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (payload != null) putStringUnsafe(TBytesInfo.payload, payload)
                        if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                        if (showCaptionAboveMedia != null) putBoolUnsafe(TBytesInfo.show_caption_above_media, showCaptionAboveMedia)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        if (allowPaidBroadcast != null) putBoolUnsafe(TBytesInfo.allow_paid_broadcast, allowPaidBroadcast)
                        if (suggestedPostParameters != null) putJsonObject(TBytesInfo.suggested_post_parameters, SuggestedPostParameters.serializer(), json, suggestedPostParameters)
                        if (replyParameters != null) putJsonObject(TBytesInfo.reply_parameters, ReplyParameters.serializer(), json, replyParameters)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, ReplyMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) sendPaidMediaBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun removeChatVerification(
        chatId: ChatId
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.removeChatVerification)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<BusinessConnection> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getBusinessConnection)
                .compose(Function { vReq ->
                    val bbSize0 = getBusinessConnectionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        getBusinessConnectionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendGame)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Message>(
                json.decodeFromString(TSerials.sMessage, strResult).result
            )
        } else {
            TResultFailure<Message>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.declineChatJoinRequest)
                .compose(Function { vReq ->
                    JsonByteBuffer(78).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendVenue)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Poll> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.stopPoll)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: stopPollBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) stopPollBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.approveChatJoinRequest)
                .compose(Function { vReq ->
                    JsonByteBuffer(78).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendAnimation).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            animation.asVertx().execute(mpb, "animation")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (duration != null) mpb.writeNormalPart("duration", duration.toString())
            if (width != null) mpb.writeNormalPart("width", width.toString())
            if (height != null) mpb.writeNormalPart("height", height.toString())
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
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
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.sendChecklist)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.declineSuggestedPost)
                .compose(Function { vReq ->
                    JsonByteBuffer(847).run {
                        putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (comment != null) putStringUnsafe(TBytesInfo.comment, comment)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<List<MessageId>> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.forwardMessages)
                .compose(Function { vReq ->
                    JsonByteBuffer(2356).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putStringUnsafe(TBytesInfo.from_chat_id, fromChatId.value)
                        putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                        if (messageThreadId != null) putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        if (directMessagesTopicId != null) putNumberUnsafe(TBytesInfo.direct_messages_topic_id, directMessagesTopicId)
                        if (disableNotification != null) putBoolUnsafe(TBytesInfo.disable_notification, disableNotification)
                        if (protectContent != null) putBoolUnsafe(TBytesInfo.protect_content, protectContent)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<StickerSet> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getStickerSet)
                .compose(Function { vReq ->
                    val bbSize0 = getStickerSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.name, name)
                        getStickerSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editMessageLiveLocation)
                .compose(Function { vReq ->
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
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Message> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.sendAudio).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            mpb.writeNormalPart("chat_id", chatId.value)
            audio.asVertx().execute(mpb, "audio")
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (messageThreadId != null) mpb.writeNormalPart("message_thread_id", messageThreadId.toString())
            if (directMessagesTopicId != null) mpb.writeNormalPart("direct_messages_topic_id", directMessagesTopicId.toString())
            if (caption != null) mpb.writeNormalPart("caption", caption)
            if (parseMode != null) mpb.writeJsonPart("parse_mode", ParseMode.serializer(), parseMode, json)
            if (captionEntities != null) mpb.writeJsonPart("caption_entities", TSerials.aListMessageEntity, captionEntities, json)
            if (duration != null) mpb.writeNormalPart("duration", duration.toString())
            if (performer != null) mpb.writeNormalPart("performer", performer)
            if (title != null) mpb.writeNormalPart("title", title)
            thumbnail?.asVertx()?.execute(mpb, "thumbnail")
            if (disableNotification != null) mpb.writeNormalPart("disable_notification", disableNotification.toString())
            if (protectContent != null) mpb.writeNormalPart("protect_content", protectContent.toString())
            if (allowPaidBroadcast != null) mpb.writeNormalPart("allow_paid_broadcast", allowPaidBroadcast.toString())
            if (messageEffectId != null) mpb.writeNormalPart("message_effect_id", messageEffectId)
            if (suggestedPostParameters != null) mpb.writeJsonPart("suggested_post_parameters", SuggestedPostParameters.serializer(), suggestedPostParameters, json)
            if (replyParameters != null) mpb.writeJsonPart("reply_parameters", ReplyParameters.serializer(), replyParameters, json)
            if (replyMarkup != null) mpb.writeJsonPart("reply_markup", ReplyMarkup.serializer(), replyMarkup, json)
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteStickerFromSet)
                .compose(Function { vReq ->
                    val bbSize0 = deleteStickerFromSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.sticker, sticker)
                        deleteStickerFromSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteBusinessMessages)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: deleteBusinessMessagesBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        putListOfLongUnsafe(TBytesInfo.message_ids, messageIds)
                        if (requestOptions == null) deleteBusinessMessagesBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.deleteStickerSet)
                .compose(Function { vReq ->
                    val bbSize0 = deleteStickerSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.name, name)
                        deleteStickerSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<SentWebAppMessage> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.answerWebAppQuery)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: answerWebAppQueryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.web_app_query_id, webAppQueryId)
                        putJsonObject(TBytesInfo.result, InlineQueryResult.serializer(), json, result)
                        if (requestOptions == null) answerWebAppQueryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<SentWebAppMessage>(
                json.decodeFromString(TSerials.sSentWebAppMessage, strResult).result
            )
        } else {
            TResultFailure<SentWebAppMessage>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun getMyStarBalance(): TResult<StarAmount> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.getMyStarBalance)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setStickerSetTitle)
                .compose(Function { vReq ->
                    val bbSize0 = setStickerSetTitleBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.name, name)
                        putStringUnsafe(TBytesInfo.title, title)
                        setStickerSetTitleBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }

    override suspend fun exportChatInviteLink(
        chatId: ChatId
    ): TResult<String> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.exportChatInviteLink)
                .compose(Function { vReq ->
                    JsonByteBuffer(47).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val resultPre1 = client.request(ro00.editMessageMedia).coAwait()
            val mpb = MultiPartBuilder(resultPre1)
            resultPre1.isChunked = true
            media.executeAll(mpb)
            if (businessConnectionId != null) mpb.writeNormalPart("business_connection_id", businessConnectionId)
            if (chatId != null) mpb.writeNormalPart("chat_id", chatId.value)
            if (messageId != null) mpb.writeNormalPart("message_id", messageId.toString())
            if (inlineMessageId != null) mpb.writeNormalPart("inline_message_id", inlineMessageId)
            if (replyMarkup != null) mpb.writeJsonPart("reply_markup", InlineKeyboardMarkup.serializer(), replyMarkup, json)
            mpb.finish()
            val result1 = resultPre1.response().coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.closeForumTopic)
                .compose(Function { vReq ->
                    JsonByteBuffer(88).run {
                        putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        putNumberUnsafe(TBytesInfo.message_thread_id, messageThreadId)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.replaceStickerInSet)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: replaceStickerInSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.name, name)
                        putStringUnsafe(TBytesInfo.old_sticker, oldSticker)
                        putJsonObject(TBytesInfo.sticker, InputSticker.serializer(), json, sticker)
                        if (requestOptions == null) replaceStickerInSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.answerCallbackQuery)
                .compose(Function { vReq ->
                    val bbSize0 = answerCallbackQueryBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putStringUnsafe(TBytesInfo.callback_query_id, callbackQueryId)
                        if (text != null) putStringUnsafe(TBytesInfo.text, text)
                        if (showAlert != null) putBoolUnsafe(TBytesInfo.show_alert, showAlert)
                        if (url != null) putStringUnsafe(TBytesInfo.url, url)
                        if (cacheTime != null) putNumberUnsafe(TBytesInfo.cache_time, cacheTime)
                        answerCallbackQueryBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.setMyDescription)
                .compose(Function { vReq ->
                    JsonByteBuffer(3111).run {
                        if (description != null) putStringUnsafe(TBytesInfo.description, description)
                        if (languageCode != null) putStringUnsafe(TBytesInfo.language_code, languageCode)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.approveSuggestedPost)
                .compose(Function { vReq ->
                    JsonByteBuffer(99).run {
                        putNumberUnsafe(TBytesInfo.chat_id, chatId)
                        putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (sendDate != null) putNumberUnsafe(TBytesInfo.send_date, sendDate)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.addStickerToSet)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: addStickerToSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.name, name)
                        putJsonObject(TBytesInfo.sticker, InputSticker.serializer(), json, sticker)
                        if (requestOptions == null) addStickerToSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.createNewStickerSet)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: createNewStickerSetBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        putNumberUnsafe(TBytesInfo.user_id, userId)
                        putStringUnsafe(TBytesInfo.name, name)
                        putStringUnsafe(TBytesInfo.title, title)
                        putListOfJsonObjects(TBytesInfo.stickers, InputSticker.serializer(), json, stickers)
                        if (stickerType != null) putStringUnsafe(TBytesInfo.sticker_type, stickerType)
                        if (needsRepainting != null) putBoolUnsafe(TBytesInfo.needs_repainting, needsRepainting)
                        if (requestOptions == null) createNewStickerSetBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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
    ): TResult.Either<Message, Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.editMessageCaption)
                .compose(Function { vReq ->
                    val bbSize0 = requestOptions?.bufferSize ?: editMessageCaptionBSP.decideCapacity()
                    JsonByteBuffer(bbSize0).run {
                        if (businessConnectionId != null) putStringUnsafe(TBytesInfo.business_connection_id, businessConnectionId)
                        if (chatId != null) putStringUnsafe(TBytesInfo.chat_id, chatId.value)
                        if (messageId != null) putNumberUnsafe(TBytesInfo.message_id, messageId)
                        if (inlineMessageId != null) putStringUnsafe(TBytesInfo.inline_message_id, inlineMessageId)
                        if (caption != null) putStringUnsafe(TBytesInfo.caption, caption)
                        if (parseMode != null) putJsonObject(TBytesInfo.parse_mode, ParseMode.serializer(), json, parseMode)
                        if (captionEntities != null) putListOfJsonObjects(TBytesInfo.caption_entities, MessageEntity.serializer(), json, captionEntities)
                        if (showCaptionAboveMedia != null) putBoolUnsafe(TBytesInfo.show_caption_above_media, showCaptionAboveMedia)
                        if (replyMarkup != null) putJsonObject(TBytesInfo.reply_markup, InlineKeyboardMarkup.serializer(), json, replyMarkup)
                        if (requestOptions == null) editMessageCaptionBSP.record(size9, bbSize0)
                        vReq.send(toBuffer())
                    }
                })
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
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

    override suspend fun logOut(): TResult<Boolean> {
        val (statusCode0, strResult) = withContext(dispatcher) {
            val result1 = client.request(ro00.logOut)
                .compose(emptySendVertx)
                .coAwait()
            CodeAndResult(result1.statusCode(), result1.body().coAwait().toString(Charsets.UTF_8))
        }
        return if (statusCode0 in 200..299) {
            TResult<Boolean>(
                json.decodeFromString(OkBoolOpt.serializer(), strResult).result
            )
        } else {
            TResultFailure<Boolean>(json.decodeFromString(TelegramError.serializer(), strResult))
        }
    }


    companion object {
        private val el1: () -> Unit = {}
        val emptySendVertx = Function<HttpClientRequest, io.vertx.core.Future<HttpClientResponse>> { it.send() }
    }
}

