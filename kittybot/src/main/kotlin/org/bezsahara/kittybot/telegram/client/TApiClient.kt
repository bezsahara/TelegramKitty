package org.bezsahara.kittybot.telegram.client


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import org.bezsahara.kittybot.bot.notNull
import org.bezsahara.kittybot.telegram.classes.bots.BotDescription
import org.bezsahara.kittybot.telegram.classes.bots.BotName
import org.bezsahara.kittybot.telegram.classes.bots.BotShortDescription
import org.bezsahara.kittybot.telegram.classes.bots.commands.BotCommand
import org.bezsahara.kittybot.telegram.classes.bots.commands.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.chats.ChatFullInfo
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.classes.chats.admin.ChatAdministratorRights
import org.bezsahara.kittybot.telegram.classes.chats.boosts.UserChatBoosts
import org.bezsahara.kittybot.telegram.classes.chats.links.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.chats.members.ChatMember
import org.bezsahara.kittybot.telegram.classes.files.File
import org.bezsahara.kittybot.telegram.classes.forums.ForumTopic
import org.bezsahara.kittybot.telegram.classes.games.scores.GameHighScore
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboards.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.media.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.media.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.classes.menus.MenuButton
import org.bezsahara.kittybot.telegram.classes.messages.MessageId
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.classes.passport.errors.PassportElementError
import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.payments.ShippingOption
import org.bezsahara.kittybot.telegram.classes.permissions.ChatPermissions
import org.bezsahara.kittybot.telegram.classes.polls.InputPollOption
import org.bezsahara.kittybot.telegram.classes.polls.Poll
import org.bezsahara.kittybot.telegram.classes.previews.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.queries.InlineQueryResultsButton
import org.bezsahara.kittybot.telegram.classes.queries.results.InlineQueryResult
import org.bezsahara.kittybot.telegram.classes.reactions.ReactionType
import org.bezsahara.kittybot.telegram.classes.replies.ReplyParameters
import org.bezsahara.kittybot.telegram.classes.stickers.InputSticker
import org.bezsahara.kittybot.telegram.classes.stickers.MaskPosition
import org.bezsahara.kittybot.telegram.classes.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.stickers.StickerSet
import org.bezsahara.kittybot.telegram.classes.updates.Update
import org.bezsahara.kittybot.telegram.classes.users.User
import org.bezsahara.kittybot.telegram.classes.users.photos.UserProfilePhotos
import org.bezsahara.kittybot.telegram.classes.webapps.messages.SentWebAppMessage
import org.bezsahara.kittybot.telegram.classes.webhooks.WebhookInfo
import org.bezsahara.kittybot.telegram.utils.ChatAction
import org.bezsahara.kittybot.telegram.utils.CommonTypeInfo
import org.bezsahara.kittybot.telegram.utils.ParseMode
import org.bezsahara.kittybot.telegram.utils.TResult


@OptIn(InternalAPI::class, ExperimentalSerializationApi::class)
class TApiClient(
    @JvmField internal val json: Json,
    @JvmField internal val client: HttpClient,
    @JvmField internal val token: String
) {



    suspend fun getUpdates(
        offset: Long?,
        limit: Long?,
        timeout: Long?,
        allowedUpdates: List<String>?
    ): TResult<List<Update>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getUpdates")
            timeout { requestTimeoutMillis = (timeout?.times(1000))?.plus(1000) }
            body = TextContent(buildJsonObject {
                if (offset != null) { put("offset", JsonPrimitive(offset)) }
                if (limit != null) { put("limit", JsonPrimitive(limit)) }
                if (timeout != null) { put("timeout", JsonPrimitive(timeout)) }
                if (allowedUpdates != null) { put("allowed_updates", JsonUnquotedLiteral(json.encodeToString(ListSerializer(String.serializer()), allowedUpdates))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<Update>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Update.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setWebhook(
        url: String,
        certificate: TelegramFile?,
        ipAddress: String?,
        maxConnections: Long?,
        allowedUpdates: List<String>?,
        dropPendingUpdates: Boolean?,
        secretToken: String?
    ): TResult<Boolean> {
        val listSize = notNull(certificate)+notNull(ipAddress)+notNull(maxConnections)+notNull(allowedUpdates)+notNull(dropPendingUpdates)+notNull(secretToken)+1
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("setWebhook")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(url, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=url")))))
                    certificate?.run { appendFile("certificate") }
                    if (ipAddress != null) { add(PartData.FormItem(ipAddress, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=ip_address"))))) }
                    if (maxConnections != null) { add(PartData.FormItem(maxConnections.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=max_connections"))))) }
                    if (allowedUpdates != null) { add(PartData.FormItem(allowedUpdates.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=allowed_updates"))))) }
                    if (dropPendingUpdates != null) { add(PartData.FormItem(dropPendingUpdates.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=drop_pending_updates"))))) }
                    if (secretToken != null) { add(PartData.FormItem(secretToken, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=secret_token"))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteWebhook")
            body = TextContent(buildJsonObject {
                if (dropPendingUpdates != null) { put("drop_pending_updates", JsonPrimitive(dropPendingUpdates)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getWebhookInfo(): TResult<WebhookInfo> {
        val result1 = client.request {
            method = HttpMethod.Get
            url("getWebhookInfo")
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<WebhookInfo>(
                json.decodeFromString(Ok.serializer(WebhookInfo.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMe(): TResult<User> {
        val result1 = client.request {
            method = HttpMethod.Get
            url("getMe")
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<User>(
                json.decodeFromString(Ok.serializer(User.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun logOut(): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            url("logOut")
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun close(): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            url("close")
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendMessage(
        chatId: ChatId,
        text: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        linkPreviewOptions: LinkPreviewOptions?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("text", JsonPrimitive(text))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (parseMode != null) { put("parse_mode", JsonUnquotedLiteral(json.encodeToString(ParseMode.serializer(), parseMode))) }
                if (entities != null) { put("entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), entities))) }
                if (linkPreviewOptions != null) { put("link_preview_options", JsonUnquotedLiteral(json.encodeToString(LinkPreviewOptions.serializer(), linkPreviewOptions))) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun forwardMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("forwardMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("from_chat_id", JsonPrimitive(fromChatId.value))
                put("message_id", JsonPrimitive(messageId))
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun forwardMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?
    ): TResult<List<MessageId>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("forwardMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("from_chat_id", JsonPrimitive(fromChatId.value))
                put("message_ids", JsonUnquotedLiteral(json.encodeToString(ListSerializer(Long.serializer()), messageIds)))
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<MessageId>>(
                json.decodeFromString(Ok.serializer(ListSerializer(MessageId.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun copyMessage(
        chatId: ChatId,
        fromChatId: ChatId,
        messageId: Long,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<MessageId> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("copyMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("from_chat_id", JsonPrimitive(fromChatId.value))
                put("message_id", JsonPrimitive(messageId))
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (caption != null) { put("caption", JsonPrimitive(caption)) }
                if (parseMode != null) { put("parse_mode", JsonUnquotedLiteral(json.encodeToString(ParseMode.serializer(), parseMode))) }
                if (captionEntities != null) { put("caption_entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities))) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<MessageId>(
                json.decodeFromString(Ok.serializer(MessageId.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun copyMessages(
        chatId: ChatId,
        fromChatId: ChatId,
        messageIds: List<Long>,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        removeCaption: Boolean?
    ): TResult<List<MessageId>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("copyMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("from_chat_id", JsonPrimitive(fromChatId.value))
                put("message_ids", JsonUnquotedLiteral(json.encodeToString(ListSerializer(Long.serializer()), messageIds)))
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (removeCaption != null) { put("remove_caption", JsonPrimitive(removeCaption)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<MessageId>>(
                json.decodeFromString(Ok.serializer(ListSerializer(MessageId.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendPhoto(
        chatId: ChatId,
        photo: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(hasSpoiler)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendPhoto")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    photo.run { appendFile("photo") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (hasSpoiler != null) { add(PartData.FormItem(hasSpoiler.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=has_spoiler"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendAudio(
        chatId: ChatId,
        audio: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        performer: String?,
        title: String?,
        thumbnail: TelegramFile?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(duration)+notNull(performer)+notNull(title)+notNull(thumbnail)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendAudio")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    audio.run { appendFile("audio") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (duration != null) { add(PartData.FormItem(duration.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=duration"))))) }
                    if (performer != null) { add(PartData.FormItem(performer, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=performer"))))) }
                    if (title != null) { add(PartData.FormItem(title, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=title"))))) }
                    thumbnail?.run { appendFile("thumbnail") }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendDocument(
        chatId: ChatId,
        document: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        thumbnail: TelegramFile?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        disableContentTypeDetection: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(thumbnail)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(disableContentTypeDetection)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendDocument")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    document.run { appendFile("document") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    thumbnail?.run { appendFile("thumbnail") }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (disableContentTypeDetection != null) { add(PartData.FormItem(disableContentTypeDetection.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_content_type_detection"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendVideo(
        chatId: ChatId,
        video: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: TelegramFile?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        supportsStreaming: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(duration)+notNull(width)+notNull(height)+notNull(thumbnail)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(hasSpoiler)+notNull(supportsStreaming)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendVideo")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    video.run { appendFile("video") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (duration != null) { add(PartData.FormItem(duration.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=duration"))))) }
                    if (width != null) { add(PartData.FormItem(width.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=width"))))) }
                    if (height != null) { add(PartData.FormItem(height.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=height"))))) }
                    thumbnail?.run { appendFile("thumbnail") }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (hasSpoiler != null) { add(PartData.FormItem(hasSpoiler.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=has_spoiler"))))) }
                    if (supportsStreaming != null) { add(PartData.FormItem(supportsStreaming.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=supports_streaming"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendAnimation(
        chatId: ChatId,
        animation: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        duration: Long?,
        width: Long?,
        height: Long?,
        thumbnail: TelegramFile?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        hasSpoiler: Boolean?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(duration)+notNull(width)+notNull(height)+notNull(thumbnail)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(hasSpoiler)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendAnimation")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    animation.run { appendFile("animation") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (duration != null) { add(PartData.FormItem(duration.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=duration"))))) }
                    if (width != null) { add(PartData.FormItem(width.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=width"))))) }
                    if (height != null) { add(PartData.FormItem(height.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=height"))))) }
                    thumbnail?.run { appendFile("thumbnail") }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (hasSpoiler != null) { add(PartData.FormItem(hasSpoiler.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=has_spoiler"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendVoice(
        chatId: ChatId,
        voice: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        duration: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(caption)+notNull(parseMode)+notNull(captionEntities)+notNull(duration)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendVoice")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    voice.run { appendFile("voice") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (caption != null) { add(PartData.FormItem(caption, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption"))))) }
                    if (parseMode != null) { add(PartData.FormItem(Json.encodeToString(ParseMode.serializer(), parseMode), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=parse_mode"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (captionEntities != null) { add(PartData.FormItem(Json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=caption_entities"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (duration != null) { add(PartData.FormItem(duration.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=duration"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendVideoNote(
        chatId: ChatId,
        videoNote: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        duration: Long?,
        length: Long?,
        thumbnail: TelegramFile?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(duration)+notNull(length)+notNull(thumbnail)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendVideoNote")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    videoNote.run { appendFile("video_note") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (duration != null) { add(PartData.FormItem(duration.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=duration"))))) }
                    if (length != null) { add(PartData.FormItem(length.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=length"))))) }
                    thumbnail?.run { appendFile("thumbnail") }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendMediaGroup(
        chatId: ChatId,
        media: List<List<MediaGroupAccepted>>,
        businessConnectionId: String?,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?
    ): TResult<List<Message>> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendMediaGroup")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    add(PartData.FormItem(Json.encodeToString(ListSerializer(ListSerializer(MediaGroupAccepted.serializer())), media), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=media"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                        media.forEach { inner -> inner.forEach { f -> (f as InputMedia).run { appendFiles() } } }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<Message>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Message.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendLocation(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        businessConnectionId: String?,
        messageThreadId: Long?,
        horizontalAccuracy: Float?,
        livePeriod: Long?,
        heading: Long?,
        proximityAlertRadius: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendLocation")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("latitude", JsonPrimitive(latitude))
                put("longitude", JsonPrimitive(longitude))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (horizontalAccuracy != null) { put("horizontal_accuracy", JsonPrimitive(horizontalAccuracy)) }
                if (livePeriod != null) { put("live_period", JsonPrimitive(livePeriod)) }
                if (heading != null) { put("heading", JsonPrimitive(heading)) }
                if (proximityAlertRadius != null) { put("proximity_alert_radius", JsonPrimitive(proximityAlertRadius)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendVenue(
        chatId: ChatId,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        foursquareId: String?,
        foursquareType: String?,
        googlePlaceId: String?,
        googlePlaceType: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendVenue")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("latitude", JsonPrimitive(latitude))
                put("longitude", JsonPrimitive(longitude))
                put("title", JsonPrimitive(title))
                put("address", JsonPrimitive(address))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (foursquareId != null) { put("foursquare_id", JsonPrimitive(foursquareId)) }
                if (foursquareType != null) { put("foursquare_type", JsonPrimitive(foursquareType)) }
                if (googlePlaceId != null) { put("google_place_id", JsonPrimitive(googlePlaceId)) }
                if (googlePlaceType != null) { put("google_place_type", JsonPrimitive(googlePlaceType)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendContact(
        chatId: ChatId,
        phoneNumber: String,
        firstName: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendContact")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("phone_number", JsonPrimitive(phoneNumber))
                put("first_name", JsonPrimitive(firstName))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (lastName != null) { put("last_name", JsonPrimitive(lastName)) }
                if (vcard != null) { put("vcard", JsonPrimitive(vcard)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendPoll(
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
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendPoll")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("question", JsonPrimitive(question))
                put("options", JsonUnquotedLiteral(json.encodeToString(ListSerializer(InputPollOption.serializer()), options)))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (questionParseMode != null) { put("question_parse_mode", JsonPrimitive(questionParseMode)) }
                if (questionEntities != null) { put("question_entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), questionEntities))) }
                if (isAnonymous != null) { put("is_anonymous", JsonPrimitive(isAnonymous)) }
                if (type != null) { put("type", JsonPrimitive(type)) }
                if (allowsMultipleAnswers != null) { put("allows_multiple_answers", JsonPrimitive(allowsMultipleAnswers)) }
                if (correctOptionId != null) { put("correct_option_id", JsonPrimitive(correctOptionId)) }
                if (explanation != null) { put("explanation", JsonPrimitive(explanation)) }
                if (explanationParseMode != null) { put("explanation_parse_mode", JsonPrimitive(explanationParseMode)) }
                if (explanationEntities != null) { put("explanation_entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), explanationEntities))) }
                if (openPeriod != null) { put("open_period", JsonPrimitive(openPeriod)) }
                if (closeDate != null) { put("close_date", JsonPrimitive(closeDate)) }
                if (isClosed != null) { put("is_closed", JsonPrimitive(isClosed)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendDice(
        chatId: ChatId,
        businessConnectionId: String?,
        messageThreadId: Long?,
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendDice")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (emoji != null) { put("emoji", JsonPrimitive(emoji)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(ReplyMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendChatAction(
        chatId: ChatId,
        action: ChatAction,
        businessConnectionId: String?,
        messageThreadId: Long?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendChatAction")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("action", JsonUnquotedLiteral(json.encodeToString(ChatAction.serializer(), action)))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMessageReaction(
        chatId: ChatId,
        messageId: Long,
        reaction: List<ReactionType>?,
        isBig: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMessageReaction")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_id", JsonPrimitive(messageId))
                if (reaction != null) { put("reaction", JsonUnquotedLiteral(json.encodeToString(ListSerializer(ReactionType.serializer()), reaction))) }
                if (isBig != null) { put("is_big", JsonPrimitive(isBig)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): TResult<UserProfilePhotos> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getUserProfilePhotos")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                if (offset != null) { put("offset", JsonPrimitive(offset)) }
                if (limit != null) { put("limit", JsonPrimitive(limit)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<UserProfilePhotos>(
                json.decodeFromString(Ok.serializer(UserProfilePhotos.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getFile(
        fileId: String
    ): TResult<File> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getFile")
            body = TextContent(buildJsonObject {
                put("file_id", JsonPrimitive(fileId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<File>(
                json.decodeFromString(Ok.serializer(File.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("banChatMember")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
                if (untilDate != null) { put("until_date", JsonPrimitive(untilDate)) }
                if (revokeMessages != null) { put("revoke_messages", JsonPrimitive(revokeMessages)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unbanChatMember")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
                if (onlyIfBanned != null) { put("only_if_banned", JsonPrimitive(onlyIfBanned)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("restrictChatMember")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
                put("permissions", JsonUnquotedLiteral(json.encodeToString(ChatPermissions.serializer(), permissions)))
                if (useIndependentChatPermissions != null) { put("use_independent_chat_permissions", JsonPrimitive(useIndependentChatPermissions)) }
                if (untilDate != null) { put("until_date", JsonPrimitive(untilDate)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun promoteChatMember(
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
        canManageTopics: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("promoteChatMember")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
                if (isAnonymous != null) { put("is_anonymous", JsonPrimitive(isAnonymous)) }
                if (canManageChat != null) { put("can_manage_chat", JsonPrimitive(canManageChat)) }
                if (canDeleteMessages != null) { put("can_delete_messages", JsonPrimitive(canDeleteMessages)) }
                if (canManageVideoChats != null) { put("can_manage_video_chats", JsonPrimitive(canManageVideoChats)) }
                if (canRestrictMembers != null) { put("can_restrict_members", JsonPrimitive(canRestrictMembers)) }
                if (canPromoteMembers != null) { put("can_promote_members", JsonPrimitive(canPromoteMembers)) }
                if (canChangeInfo != null) { put("can_change_info", JsonPrimitive(canChangeInfo)) }
                if (canInviteUsers != null) { put("can_invite_users", JsonPrimitive(canInviteUsers)) }
                if (canPostStories != null) { put("can_post_stories", JsonPrimitive(canPostStories)) }
                if (canEditStories != null) { put("can_edit_stories", JsonPrimitive(canEditStories)) }
                if (canDeleteStories != null) { put("can_delete_stories", JsonPrimitive(canDeleteStories)) }
                if (canPostMessages != null) { put("can_post_messages", JsonPrimitive(canPostMessages)) }
                if (canEditMessages != null) { put("can_edit_messages", JsonPrimitive(canEditMessages)) }
                if (canPinMessages != null) { put("can_pin_messages", JsonPrimitive(canPinMessages)) }
                if (canManageTopics != null) { put("can_manage_topics", JsonPrimitive(canManageTopics)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatAdministratorCustomTitle")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
                put("custom_title", JsonPrimitive(customTitle))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("banChatSenderChat")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("sender_chat_id", JsonPrimitive(senderChatId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unbanChatSenderChat")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("sender_chat_id", JsonPrimitive(senderChatId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatPermissions")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("permissions", JsonUnquotedLiteral(json.encodeToString(ChatPermissions.serializer(), permissions)))
                if (useIndependentChatPermissions != null) { put("use_independent_chat_permissions", JsonPrimitive(useIndependentChatPermissions)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun exportChatInviteLink(
        chatId: ChatId
    ): TResult<String> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("exportChatInviteLink")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<String>(
                json.decodeFromString(Ok.serializer(String.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("createChatInviteLink")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                if (name != null) { put("name", JsonPrimitive(name)) }
                if (expireDate != null) { put("expire_date", JsonPrimitive(expireDate)) }
                if (memberLimit != null) { put("member_limit", JsonPrimitive(memberLimit)) }
                if (createsJoinRequest != null) { put("creates_join_request", JsonPrimitive(createsJoinRequest)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editChatInviteLink")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("invite_link", JsonPrimitive(inviteLink))
                if (name != null) { put("name", JsonPrimitive(name)) }
                if (expireDate != null) { put("expire_date", JsonPrimitive(expireDate)) }
                if (memberLimit != null) { put("member_limit", JsonPrimitive(memberLimit)) }
                if (createsJoinRequest != null) { put("creates_join_request", JsonPrimitive(createsJoinRequest)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): TResult<ChatInviteLink> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("revokeChatInviteLink")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("invite_link", JsonPrimitive(inviteLink))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatInviteLink>(
                json.decodeFromString(Ok.serializer(ChatInviteLink.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("approveChatJoinRequest")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("declineChatJoinRequest")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatPhoto(
        chatId: ChatId,
        photo: TelegramFile
    ): TResult<Boolean> {
        val listSize = +2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("setChatPhoto")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    photo.run { appendFile("photo") }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteChatPhoto(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteChatPhoto")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatTitle(
        chatId: ChatId,
        title: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatTitle")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("title", JsonPrimitive(title))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatDescription(
        chatId: ChatId,
        description: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatDescription")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                if (description != null) { put("description", JsonPrimitive(description)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        disableNotification: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("pinChatMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_id", JsonPrimitive(messageId))
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unpinChatMessage(
        chatId: ChatId,
        messageId: Long?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unpinChatMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unpinAllChatMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unpinAllChatMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun leaveChat(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("leaveChat")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getChat")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatFullInfo>(
                json.decodeFromString(Ok.serializer(ChatFullInfo.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getChatAdministrators(
        chatId: ChatId
    ): TResult<List<ChatMember>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getChatAdministrators")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<ChatMember>>(
                json.decodeFromString(Ok.serializer(ListSerializer(ChatMember.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getChatMemberCount")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Long>(
                json.decodeFromString(Ok.serializer(Long.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getChatMember(
        chatId: ChatId,
        userId: Long
    ): TResult<ChatMember> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getChatMember")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatMember>(
                json.decodeFromString(Ok.serializer(ChatMember.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatStickerSet")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("sticker_set_name", JsonPrimitive(stickerSetName))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteChatStickerSet(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteChatStickerSet")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getForumTopicIconStickers(): TResult<List<Sticker>> {
        val result1 = client.request {
            method = HttpMethod.Get
            url("getForumTopicIconStickers")
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<Sticker>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Sticker.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Long?,
        iconCustomEmojiId: String?
    ): TResult<ForumTopic> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("createForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("name", JsonPrimitive(name))
                if (iconColor != null) { put("icon_color", JsonPrimitive(iconColor)) }
                if (iconCustomEmojiId != null) { put("icon_custom_emoji_id", JsonPrimitive(iconCustomEmojiId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ForumTopic>(
                json.decodeFromString(Ok.serializer(ForumTopic.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String?,
        iconCustomEmojiId: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_thread_id", JsonPrimitive(messageThreadId))
                if (name != null) { put("name", JsonPrimitive(name)) }
                if (iconCustomEmojiId != null) { put("icon_custom_emoji_id", JsonPrimitive(iconCustomEmojiId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("closeForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_thread_id", JsonPrimitive(messageThreadId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("reopenForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_thread_id", JsonPrimitive(messageThreadId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_thread_id", JsonPrimitive(messageThreadId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unpinAllForumTopicMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_thread_id", JsonPrimitive(messageThreadId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editGeneralForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("name", JsonPrimitive(name))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun closeGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("closeGeneralForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun reopenGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("reopenGeneralForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun hideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("hideGeneralForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unhideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unhideGeneralForumTopic")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("unpinAllGeneralForumTopicMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("answerCallbackQuery")
            body = TextContent(buildJsonObject {
                put("callback_query_id", JsonPrimitive(callbackQueryId))
                if (text != null) { put("text", JsonPrimitive(text)) }
                if (showAlert != null) { put("show_alert", JsonPrimitive(showAlert)) }
                if (url != null) { put("url", JsonPrimitive(url)) }
                if (cacheTime != null) { put("cache_time", JsonPrimitive(cacheTime)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: Long
    ): TResult<UserChatBoosts> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getUserChatBoosts")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("user_id", JsonPrimitive(userId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<UserChatBoosts>(
                json.decodeFromString(Ok.serializer(UserChatBoosts.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getBusinessConnection(
        businessConnectionId: String
    ): TResult<BusinessConnection> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getBusinessConnection")
            body = TextContent(buildJsonObject {
                put("business_connection_id", JsonPrimitive(businessConnectionId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<BusinessConnection>(
                json.decodeFromString(Ok.serializer(BusinessConnection.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMyCommands")
            body = TextContent(buildJsonObject {
                put("commands", JsonUnquotedLiteral(json.encodeToString(ListSerializer(BotCommand.serializer()), commands)))
                if (scope != null) { put("scope", JsonUnquotedLiteral(json.encodeToString(BotCommandScope.serializer(), scope))) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteMyCommands")
            body = TextContent(buildJsonObject {
                if (scope != null) { put("scope", JsonUnquotedLiteral(json.encodeToString(BotCommandScope.serializer(), scope))) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<List<BotCommand>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getMyCommands")
            body = TextContent(buildJsonObject {
                if (scope != null) { put("scope", JsonUnquotedLiteral(json.encodeToString(BotCommandScope.serializer(), scope))) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<BotCommand>>(
                json.decodeFromString(Ok.serializer(ListSerializer(BotCommand.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMyName(
        name: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMyName")
            body = TextContent(buildJsonObject {
                if (name != null) { put("name", JsonPrimitive(name)) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMyName(
        languageCode: String?
    ): TResult<BotName> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getMyName")
            body = TextContent(buildJsonObject {
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<BotName>(
                json.decodeFromString(Ok.serializer(BotName.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMyDescription(
        description: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMyDescription")
            body = TextContent(buildJsonObject {
                if (description != null) { put("description", JsonPrimitive(description)) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMyDescription(
        languageCode: String?
    ): TResult<BotDescription> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getMyDescription")
            body = TextContent(buildJsonObject {
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<BotDescription>(
                json.decodeFromString(Ok.serializer(BotDescription.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMyShortDescription")
            body = TextContent(buildJsonObject {
                if (shortDescription != null) { put("short_description", JsonPrimitive(shortDescription)) }
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMyShortDescription(
        languageCode: String?
    ): TResult<BotShortDescription> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getMyShortDescription")
            body = TextContent(buildJsonObject {
                if (languageCode != null) { put("language_code", JsonPrimitive(languageCode)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<BotShortDescription>(
                json.decodeFromString(Ok.serializer(BotShortDescription.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setChatMenuButton(
        chatId: Long?,
        menuButton: MenuButton?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setChatMenuButton")
            body = TextContent(buildJsonObject {
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId)) }
                if (menuButton != null) { put("menu_button", JsonUnquotedLiteral(json.encodeToString(MenuButton.serializer(), menuButton))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getChatMenuButton(
        chatId: Long?
    ): TResult<MenuButton> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getChatMenuButton")
            body = TextContent(buildJsonObject {
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<MenuButton>(
                json.decodeFromString(Ok.serializer(MenuButton.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setMyDefaultAdministratorRights")
            body = TextContent(buildJsonObject {
                if (rights != null) { put("rights", JsonUnquotedLiteral(json.encodeToString(ChatAdministratorRights.serializer(), rights))) }
                if (forChannels != null) { put("for_channels", JsonPrimitive(forChannels)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ): TResult<ChatAdministratorRights> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getMyDefaultAdministratorRights")
            body = TextContent(buildJsonObject {
                if (forChannels != null) { put("for_channels", JsonPrimitive(forChannels)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<ChatAdministratorRights>(
                json.decodeFromString(Ok.serializer(ChatAdministratorRights.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun editMessageText(
        text: String,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        linkPreviewOptions: LinkPreviewOptions?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editMessageText")
            body = TextContent(buildJsonObject {
                put("text", JsonPrimitive(text))
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId.value)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
                if (parseMode != null) { put("parse_mode", JsonUnquotedLiteral(json.encodeToString(ParseMode.serializer(), parseMode))) }
                if (entities != null) { put("entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), entities))) }
                if (linkPreviewOptions != null) { put("link_preview_options", JsonUnquotedLiteral(json.encodeToString(LinkPreviewOptions.serializer(), linkPreviewOptions))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun editMessageCaption(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editMessageCaption")
            body = TextContent(buildJsonObject {
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId.value)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
                if (caption != null) { put("caption", JsonPrimitive(caption)) }
                if (parseMode != null) { put("parse_mode", JsonUnquotedLiteral(json.encodeToString(ParseMode.serializer(), parseMode))) }
                if (captionEntities != null) { put("caption_entities", JsonUnquotedLiteral(json.encodeToString(ListSerializer(MessageEntity.serializer()), captionEntities))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun editMessageMedia(
        media: InputMedia,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val listSize = notNull(chatId)+notNull(messageId)+notNull(inlineMessageId)+notNull(replyMarkup)+1
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("editMessageMedia")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(Json.encodeToString(InputMedia.serializer(), media), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=media"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    if (chatId != null) { add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (messageId != null) { add(PartData.FormItem(messageId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_id"))))) }
                    if (inlineMessageId != null) { add(PartData.FormItem(inlineMessageId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=inline_message_id"))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                        media.run { appendFiles() }
                }
            )
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun editMessageLiveLocation(
        latitude: Float,
        longitude: Float,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        livePeriod: Long?,
        horizontalAccuracy: Float?,
        heading: Long?,
        proximityAlertRadius: Long?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editMessageLiveLocation")
            body = TextContent(buildJsonObject {
                put("latitude", JsonPrimitive(latitude))
                put("longitude", JsonPrimitive(longitude))
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId.value)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
                if (livePeriod != null) { put("live_period", JsonPrimitive(livePeriod)) }
                if (horizontalAccuracy != null) { put("horizontal_accuracy", JsonPrimitive(horizontalAccuracy)) }
                if (heading != null) { put("heading", JsonPrimitive(heading)) }
                if (proximityAlertRadius != null) { put("proximity_alert_radius", JsonPrimitive(proximityAlertRadius)) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun stopMessageLiveLocation(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("stopMessageLiveLocation")
            body = TextContent(buildJsonObject {
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId.value)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun editMessageReplyMarkup(
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("editMessageReplyMarkup")
            body = TextContent(buildJsonObject {
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId.value)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun stopPoll(
        chatId: ChatId,
        messageId: Long,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult<Poll> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("stopPoll")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_id", JsonPrimitive(messageId))
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Poll>(
                json.decodeFromString(Ok.serializer(Poll.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteMessage")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_id", JsonPrimitive(messageId))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteMessages")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("message_ids", JsonUnquotedLiteral(json.encodeToString(ListSerializer(Long.serializer()), messageIds)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendSticker(
        chatId: ChatId,
        sticker: TelegramFile,
        businessConnectionId: String?,
        messageThreadId: Long?,
        emoji: String?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        val listSize = notNull(businessConnectionId)+notNull(messageThreadId)+notNull(emoji)+notNull(disableNotification)+notNull(protectContent)+notNull(replyParameters)+notNull(replyMarkup)+2
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("sendSticker")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(chatId.value, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=chat_id"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())))))
                    sticker.run { appendFile("sticker") }
                    if (businessConnectionId != null) { add(PartData.FormItem(businessConnectionId, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=business_connection_id"))))) }
                    if (messageThreadId != null) { add(PartData.FormItem(messageThreadId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=message_thread_id"))))) }
                    if (emoji != null) { add(PartData.FormItem(emoji, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=emoji"))))) }
                    if (disableNotification != null) { add(PartData.FormItem(disableNotification.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=disable_notification"))))) }
                    if (protectContent != null) { add(PartData.FormItem(protectContent.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=protect_content"))))) }
                    if (replyParameters != null) { add(PartData.FormItem(Json.encodeToString(ReplyParameters.serializer(), replyParameters), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_parameters"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                    if (replyMarkup != null) { add(PartData.FormItem(Json.encodeToString(ReplyMarkup.serializer(), replyMarkup), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=reply_markup"), HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))))) }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getStickerSet(
        name: String
    ): TResult<StickerSet> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getStickerSet")
            body = TextContent(buildJsonObject {
                put("name", JsonPrimitive(name))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<StickerSet>(
                json.decodeFromString(Ok.serializer(StickerSet.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>
    ): TResult<List<Sticker>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getCustomEmojiStickers")
            body = TextContent(buildJsonObject {
                put("custom_emoji_ids", JsonUnquotedLiteral(json.encodeToString(ListSerializer(String.serializer()), customEmojiIds)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<Sticker>>(
                json.decodeFromString(Ok.serializer(ListSerializer(Sticker.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun uploadStickerFile(
        userId: Long,
        sticker: TelegramFile,
        stickerFormat: String
    ): TResult<File> {
        val listSize = +3
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("uploadStickerFile")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(userId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=user_id")))))
                    sticker.run { appendFile("sticker") }
                    add(PartData.FormItem(stickerFormat, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=sticker_format")))))
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<File>(
                json.decodeFromString(Ok.serializer(File.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: String?,
        needsRepainting: Boolean?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("createNewStickerSet")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                put("name", JsonPrimitive(name))
                put("title", JsonPrimitive(title))
                put("stickers", JsonUnquotedLiteral(json.encodeToString(ListSerializer(InputSticker.serializer()), stickers)))
                if (stickerType != null) { put("sticker_type", JsonPrimitive(stickerType)) }
                if (needsRepainting != null) { put("needs_repainting", JsonPrimitive(needsRepainting)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("addStickerToSet")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                put("name", JsonPrimitive(name))
                put("sticker", JsonUnquotedLiteral(json.encodeToString(InputSticker.serializer(), sticker)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerPositionInSet(
        sticker: String,
        position: Long
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setStickerPositionInSet")
            body = TextContent(buildJsonObject {
                put("sticker", JsonPrimitive(sticker))
                put("position", JsonPrimitive(position))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteStickerFromSet(
        sticker: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteStickerFromSet")
            body = TextContent(buildJsonObject {
                put("sticker", JsonPrimitive(sticker))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("replaceStickerInSet")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                put("name", JsonPrimitive(name))
                put("old_sticker", JsonPrimitive(oldSticker))
                put("sticker", JsonUnquotedLiteral(json.encodeToString(InputSticker.serializer(), sticker)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerEmojiList(
        sticker: String,
        emojiList: List<String>
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setStickerEmojiList")
            body = TextContent(buildJsonObject {
                put("sticker", JsonPrimitive(sticker))
                put("emoji_list", JsonUnquotedLiteral(json.encodeToString(ListSerializer(String.serializer()), emojiList)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerKeywords(
        sticker: String,
        keywords: List<String>?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setStickerKeywords")
            body = TextContent(buildJsonObject {
                put("sticker", JsonPrimitive(sticker))
                if (keywords != null) { put("keywords", JsonUnquotedLiteral(json.encodeToString(ListSerializer(String.serializer()), keywords))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setStickerMaskPosition")
            body = TextContent(buildJsonObject {
                put("sticker", JsonPrimitive(sticker))
                if (maskPosition != null) { put("mask_position", JsonUnquotedLiteral(json.encodeToString(MaskPosition.serializer(), maskPosition))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerSetTitle(
        name: String,
        title: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setStickerSetTitle")
            body = TextContent(buildJsonObject {
                put("name", JsonPrimitive(name))
                put("title", JsonPrimitive(title))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: String,
        thumbnail: TelegramFile?
    ): TResult<Boolean> {
        val listSize = notNull(thumbnail)+3
        val result1 = client.request {
            method = HttpMethod.Post
            this.url.encodedPathSegments = listOf("setStickerSetThumbnail")
            bodyType = null
            body = MultiPartFormDataContent(
                (ArrayList<PartData>(listSize) as MutableList<PartData>).apply {
                    add(PartData.FormItem(name, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=name")))))
                    add(PartData.FormItem(userId.toString(), {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=user_id")))))
                    add(PartData.FormItem(format, {}, HeadersImpl(mapOf(HttpHeaders.ContentDisposition to listOf("form-data; name=format")))))
                    thumbnail?.run { appendFile("thumbnail") }
                }
            )
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setCustomEmojiStickerSetThumbnail")
            body = TextContent(buildJsonObject {
                put("name", JsonPrimitive(name))
                if (customEmojiId != null) { put("custom_emoji_id", JsonPrimitive(customEmojiId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun deleteStickerSet(
        name: String
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("deleteStickerSet")
            body = TextContent(buildJsonObject {
                put("name", JsonPrimitive(name))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Long?,
        isPersonal: Boolean?,
        nextOffset: String?,
        button: InlineQueryResultsButton?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("answerInlineQuery")
            body = TextContent(buildJsonObject {
                put("inline_query_id", JsonPrimitive(inlineQueryId))
                put("results", JsonUnquotedLiteral(json.encodeToString(ListSerializer(InlineQueryResult.serializer()), results)))
                if (cacheTime != null) { put("cache_time", JsonPrimitive(cacheTime)) }
                if (isPersonal != null) { put("is_personal", JsonPrimitive(isPersonal)) }
                if (nextOffset != null) { put("next_offset", JsonPrimitive(nextOffset)) }
                if (button != null) { put("button", JsonUnquotedLiteral(json.encodeToString(InlineQueryResultsButton.serializer(), button))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult
    ): TResult<SentWebAppMessage> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("answerWebAppQuery")
            body = TextContent(buildJsonObject {
                put("web_app_query_id", JsonPrimitive(webAppQueryId))
                put("result", JsonUnquotedLiteral(json.encodeToString(InlineQueryResult.serializer(), result)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<SentWebAppMessage>(
                json.decodeFromString(Ok.serializer(SentWebAppMessage.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendInvoice(
        chatId: ChatId,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
        messageThreadId: Long?,
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
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendInvoice")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId.value))
                put("title", JsonPrimitive(title))
                put("description", JsonPrimitive(description))
                put("payload", JsonPrimitive(payload))
                put("provider_token", JsonPrimitive(providerToken))
                put("currency", JsonPrimitive(currency))
                put("prices", JsonUnquotedLiteral(json.encodeToString(ListSerializer(LabeledPrice.serializer()), prices)))
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (maxTipAmount != null) { put("max_tip_amount", JsonPrimitive(maxTipAmount)) }
                if (suggestedTipAmounts != null) { put("suggested_tip_amounts", JsonUnquotedLiteral(json.encodeToString(ListSerializer(Long.serializer()), suggestedTipAmounts))) }
                if (startParameter != null) { put("start_parameter", JsonPrimitive(startParameter)) }
                if (providerData != null) { put("provider_data", JsonPrimitive(providerData)) }
                if (photoUrl != null) { put("photo_url", JsonPrimitive(photoUrl)) }
                if (photoSize != null) { put("photo_size", JsonPrimitive(photoSize)) }
                if (photoWidth != null) { put("photo_width", JsonPrimitive(photoWidth)) }
                if (photoHeight != null) { put("photo_height", JsonPrimitive(photoHeight)) }
                if (needName != null) { put("need_name", JsonPrimitive(needName)) }
                if (needPhoneNumber != null) { put("need_phone_number", JsonPrimitive(needPhoneNumber)) }
                if (needEmail != null) { put("need_email", JsonPrimitive(needEmail)) }
                if (needShippingAddress != null) { put("need_shipping_address", JsonPrimitive(needShippingAddress)) }
                if (sendPhoneNumberToProvider != null) { put("send_phone_number_to_provider", JsonPrimitive(sendPhoneNumberToProvider)) }
                if (sendEmailToProvider != null) { put("send_email_to_provider", JsonPrimitive(sendEmailToProvider)) }
                if (isFlexible != null) { put("is_flexible", JsonPrimitive(isFlexible)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: List<LabeledPrice>,
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
        isFlexible: Boolean?
    ): TResult<String> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("createInvoiceLink")
            body = TextContent(buildJsonObject {
                put("title", JsonPrimitive(title))
                put("description", JsonPrimitive(description))
                put("payload", JsonPrimitive(payload))
                put("provider_token", JsonPrimitive(providerToken))
                put("currency", JsonPrimitive(currency))
                put("prices", JsonUnquotedLiteral(json.encodeToString(ListSerializer(LabeledPrice.serializer()), prices)))
                if (maxTipAmount != null) { put("max_tip_amount", JsonPrimitive(maxTipAmount)) }
                if (suggestedTipAmounts != null) { put("suggested_tip_amounts", JsonUnquotedLiteral(json.encodeToString(ListSerializer(Long.serializer()), suggestedTipAmounts))) }
                if (providerData != null) { put("provider_data", JsonPrimitive(providerData)) }
                if (photoUrl != null) { put("photo_url", JsonPrimitive(photoUrl)) }
                if (photoSize != null) { put("photo_size", JsonPrimitive(photoSize)) }
                if (photoWidth != null) { put("photo_width", JsonPrimitive(photoWidth)) }
                if (photoHeight != null) { put("photo_height", JsonPrimitive(photoHeight)) }
                if (needName != null) { put("need_name", JsonPrimitive(needName)) }
                if (needPhoneNumber != null) { put("need_phone_number", JsonPrimitive(needPhoneNumber)) }
                if (needEmail != null) { put("need_email", JsonPrimitive(needEmail)) }
                if (needShippingAddress != null) { put("need_shipping_address", JsonPrimitive(needShippingAddress)) }
                if (sendPhoneNumberToProvider != null) { put("send_phone_number_to_provider", JsonPrimitive(sendPhoneNumberToProvider)) }
                if (sendEmailToProvider != null) { put("send_email_to_provider", JsonPrimitive(sendEmailToProvider)) }
                if (isFlexible != null) { put("is_flexible", JsonPrimitive(isFlexible)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<String>(
                json.decodeFromString(Ok.serializer(String.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("answerShippingQuery")
            body = TextContent(buildJsonObject {
                put("shipping_query_id", JsonPrimitive(shippingQueryId))
                put("ok", JsonPrimitive(ok))
                if (shippingOptions != null) { put("shipping_options", JsonUnquotedLiteral(json.encodeToString(ListSerializer(ShippingOption.serializer()), shippingOptions))) }
                if (errorMessage != null) { put("error_message", JsonPrimitive(errorMessage)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("answerPreCheckoutQuery")
            body = TextContent(buildJsonObject {
                put("pre_checkout_query_id", JsonPrimitive(preCheckoutQueryId))
                put("ok", JsonPrimitive(ok))
                if (errorMessage != null) { put("error_message", JsonPrimitive(errorMessage)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>
    ): TResult<Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setPassportDataErrors")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                put("errors", JsonUnquotedLiteral(json.encodeToString(ListSerializer(PassportElementError.serializer()), errors)))
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Boolean>(
                json.decodeFromString(Ok.serializer(Boolean.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        businessConnectionId: String?,
        messageThreadId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult<Message> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("sendGame")
            body = TextContent(buildJsonObject {
                put("chat_id", JsonPrimitive(chatId))
                put("game_short_name", JsonPrimitive(gameShortName))
                if (businessConnectionId != null) { put("business_connection_id", JsonPrimitive(businessConnectionId)) }
                if (messageThreadId != null) { put("message_thread_id", JsonPrimitive(messageThreadId)) }
                if (disableNotification != null) { put("disable_notification", JsonPrimitive(disableNotification)) }
                if (protectContent != null) { put("protect_content", JsonPrimitive(protectContent)) }
                if (replyParameters != null) { put("reply_parameters", JsonUnquotedLiteral(json.encodeToString(ReplyParameters.serializer(), replyParameters))) }
                if (replyMarkup != null) { put("reply_markup", JsonUnquotedLiteral(json.encodeToString(InlineKeyboardMarkup.serializer(), replyMarkup))) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<Message>(
                json.decodeFromString(Ok.serializer(Message.serializer()), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }

    suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult.Either<Message, Boolean> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("setGameScore")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                put("score", JsonPrimitive(score))
                if (force != null) { put("force", JsonPrimitive(force)) }
                if (disableEditMessage != null) { put("disable_edit_message", JsonPrimitive(disableEditMessage)) }
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        return if (result1.status.isSuccess()) {
            val response = json.decodeFromString(JsonElement.serializer(), result1.body<String>(CommonTypeInfo.str))
            val resultObj = response.jsonObject["result"]
            if (resultObj == null) {
                TResult.Success.Second<Boolean>(response.jsonPrimitive.boolean)
            } else {
                TResult.Success.First<Message>(json.decodeFromJsonElement(Message.serializer(), resultObj))
            }
        } else {
            TResult.Failure(result1.body(CommonTypeInfo.str))
        }
    }

    suspend fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult<List<GameHighScore>> {
        val result1 = client.request {
            method = HttpMethod.Get
            headers[HttpHeaders.ContentType] = "application/json"
            this.url.encodedPathSegments = listOf("getGameHighScores")
            body = TextContent(buildJsonObject {
                put("user_id", JsonPrimitive(userId))
                if (chatId != null) { put("chat_id", JsonPrimitive(chatId)) }
                if (messageId != null) { put("message_id", JsonPrimitive(messageId)) }
                if (inlineMessageId != null) { put("inline_message_id", JsonPrimitive(inlineMessageId)) }
            }.toString(), ContentType.Application.Json)
            bodyType = null
        }
        
        return if (result1.status.isSuccess()) {
            TResult.Success<List<GameHighScore>>(
                json.decodeFromString(Ok.serializer(ListSerializer(GameHighScore.serializer())), result1.body(CommonTypeInfo.str)).result
            )
        } else {
            TResult.Failure(json.decodeFromString(TelegramError.serializer(), result1.body<String>(CommonTypeInfo.str)))
        }
    }
}

