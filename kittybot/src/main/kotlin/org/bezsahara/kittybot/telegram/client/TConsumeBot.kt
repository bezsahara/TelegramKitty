package org.bezsahara.kittybot.telegram.client

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.keyboard.PreparedKeyboardButton
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.values.StickerType
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.values.ChatAction
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.values.DiceEmoji
import org.bezsahara.kittybot.telegram.classes.core.MessageId
import org.bezsahara.kittybot.telegram.classes.inline.PreparedInlineMessage
import org.bezsahara.kittybot.telegram.classes.payments.ShippingOption
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
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
import org.bezsahara.kittybot.telegram.classes.input.InputChecklist
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.classes.bot.BotShortDescription
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementError
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.chat.ChatPermissions
import org.bezsahara.kittybot.telegram.classes.chat.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.user.UserProfileAudios
import org.bezsahara.kittybot.telegram.classes.games.GameHighScore
import org.bezsahara.kittybot.telegram.classes.payments.StarAmount
import org.bezsahara.kittybot.telegram.classes.chat.boosts.UserChatBoosts
import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import org.bezsahara.kittybot.telegram.classes.input.MediaGroupAccepted
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGifts
import org.bezsahara.kittybot.telegram.classes.bot.BotName
import org.bezsahara.kittybot.telegram.classes.chat.ForumTopic
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.classes.gifts.Gifts
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostParameters
import org.bezsahara.kittybot.telegram.classes.input.InputStoryContent
import org.bezsahara.kittybot.telegram.values.PollType
import org.bezsahara.kittybot.telegram.classes.inline.SentWebAppMessage
import org.bezsahara.kittybot.telegram.classes.keyboard.KeyboardButton
import org.bezsahara.kittybot.telegram.classes.payments.StarTransactions
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.chat.ChatAdministratorRights
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import org.bezsahara.kittybot.telegram.classes.bot.BotDescription
import org.bezsahara.kittybot.telegram.classes.media.stickers.StickerSet
import org.bezsahara.kittybot.telegram.values.StickerFormat
import org.bezsahara.kittybot.telegram.classes.gifts.AcceptedGiftTypes
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.utils.TResult.Either
import org.bezsahara.kittybot.telegram.classes.media.stickers.MaskPosition


class TConsumeBot(
    val delegate: KittyBot
) : KittyBot() {
    override suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>
    ): TResult<Boolean> {
        val result = delegate.deleteMessages(chatId, messageIds)
        result.consume()
        return result
    }

    override suspend fun setChatPhoto(
        chatId: ChatId,
        photo: TelegramFile
    ): TResult<Boolean> {
        val result = delegate.setChatPhoto(chatId, photo)
        result.consume()
        return result
    }

    override suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.unpinAllGeneralForumTopicMessages(chatId)
        result.consume()
        return result
    }

    override suspend fun editChatSubscriptionInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?
    ): TResult<ChatInviteLink> {
        val result = delegate.editChatSubscriptionInviteLink(chatId, inviteLink, name)
        result.consume()
        return result
    }

    override suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result = delegate.deleteForumTopic(chatId, messageThreadId)
        result.consume()
        return result
    }

    override suspend fun readBusinessMessage(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long
    ): TResult<Boolean> {
        val result = delegate.readBusinessMessage(businessConnectionId, chatId, messageId)
        result.consume()
        return result
    }

    override suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?
    ): TResult<Boolean> {
        val result = delegate.setChatPermissions(chatId, permissions, useIndependentChatPermissions)
        result.consume()
        return result
    }

    override suspend fun setMyProfilePhoto(
        photo: InputProfilePhoto
    ): TResult<Boolean> {
        val result = delegate.setMyProfilePhoto(photo)
        result.consume()
        return result
    }

    override suspend fun editMessageReplyMarkup(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> {
        val result = delegate.editMessageReplyMarkup(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?
    ): TResult<Boolean> {
        val result = delegate.banChatMember(chatId, userId, untilDate, revokeMessages)
        result.consume()
        return result
    }

    override suspend fun getBusinessAccountStarBalance(
        businessConnectionId: String
    ): TResult<StarAmount> {
        val result = delegate.getBusinessAccountStarBalance(businessConnectionId)
        result.consume()
        return result
    }

    override suspend fun unhideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.unhideGeneralForumTopic(chatId)
        result.consume()
        return result
    }

    override suspend fun verifyChat(
        chatId: ChatId,
        customDescription: String?
    ): TResult<Boolean> {
        val result = delegate.verifyChat(chatId, customDescription)
        result.consume()
        return result
    }

    override suspend fun editStory(
        businessConnectionId: String,
        storyId: Long,
        content: InputStoryContent,
        caption: String?,
        parseMode: ParseMode?,
        captionEntities: List<MessageEntity>?,
        areas: List<StoryArea>?
    ): TResult<Story> {
        val result = delegate.editStory(businessConnectionId, storyId, content, caption, parseMode, captionEntities, areas)
        result.consume()
        return result
    }

    override suspend fun deleteChatPhoto(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.deleteChatPhoto(chatId)
        result.consume()
        return result
    }

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
    ): TResult<MessageId> {
        val result = delegate.copyMessage(chatId, fromChatId, messageId, messageThreadId, directMessagesTopicId, videoStartTimestamp, caption, parseMode, captionEntities, showCaptionAboveMedia, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result = delegate.reopenForumTopic(chatId, messageThreadId)
        result.consume()
        return result
    }

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
    ): TResult<Message> {
        val result = delegate.sendDice(chatId, businessConnectionId, messageThreadId, directMessagesTopicId, emoji, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun removeMyProfilePhoto(): TResult<Boolean> {
        val result = delegate.removeMyProfilePhoto()
        result.consume()
        return result
    }

    override suspend fun refundStarPayment(
        userId: Long,
        telegramPaymentChargeId: String
    ): TResult<Boolean> {
        val result = delegate.refundStarPayment(userId, telegramPaymentChargeId)
        result.consume()
        return result
    }

    override suspend fun getAvailableGifts(): TResult<Gifts> {
        val result = delegate.getAvailableGifts()
        result.consume()
        return result
    }

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
        val result = delegate.sendLocation(chatId, latitude, longitude, businessConnectionId, messageThreadId, directMessagesTopicId, horizontalAccuracy, livePeriod, heading, proximityAlertRadius, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): TResult<Boolean> {
        val result = delegate.setChatAdministratorCustomTitle(chatId, userId, customTitle)
        result.consume()
        return result
    }

    override suspend fun setUserEmojiStatus(
        userId: Long,
        emojiStatusCustomEmojiId: String?,
        emojiStatusExpirationDate: Long?
    ): TResult<Boolean> {
        val result = delegate.setUserEmojiStatus(userId, emojiStatusCustomEmojiId, emojiStatusExpirationDate)
        result.consume()
        return result
    }

    override suspend fun setChatTitle(
        chatId: ChatId,
        title: String
    ): TResult<Boolean> {
        val result = delegate.setChatTitle(chatId, title)
        result.consume()
        return result
    }

    override suspend fun setChatDescription(
        chatId: ChatId,
        description: String?
    ): TResult<Boolean> {
        val result = delegate.setChatDescription(chatId, description)
        result.consume()
        return result
    }

    override suspend fun getChatAdministrators(
        chatId: ChatId
    ): TResult<List<ChatMember>> {
        val result = delegate.getChatAdministrators(chatId)
        result.consume()
        return result
    }

    override suspend fun getManagedBotToken(
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<String> {
        val result = delegate.getManagedBotToken(userId, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> {
        val result = delegate.getChatMemberCount(chatId)
        result.consume()
        return result
    }

    override suspend fun removeUserVerification(
        userId: Long
    ): TResult<Boolean> {
        val result = delegate.removeUserVerification(userId)
        result.consume()
        return result
    }

    override suspend fun removeBusinessAccountProfilePhoto(
        businessConnectionId: String,
        isPublic: Boolean?
    ): TResult<Boolean> {
        val result = delegate.removeBusinessAccountProfilePhoto(businessConnectionId, isPublic)
        result.consume()
        return result
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
        val result = delegate.sendPhoto(chatId, photo, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
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
        val result = delegate.sendSticker(chatId, sticker, businessConnectionId, messageThreadId, directMessagesTopicId, emoji, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun getUserProfileAudios(
        userId: Long,
        offset: Long?,
        limit: Long?,
        requestOptions: RequestOptions?
    ): TResult<UserProfileAudios> {
        val result = delegate.getUserProfileAudios(userId, offset, limit, requestOptions)
        result.consume()
        return result
    }

    override suspend fun editMessageChecklist(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long,
        checklist: InputChecklist,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> {
        val result = delegate.editMessageChecklist(businessConnectionId, chatId, messageId, checklist, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setPassportDataErrors(userId, errors, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setChatMenuButton(
        chatId: Long?,
        menuButton: MenuButton?
    ): TResult<Boolean> {
        val result = delegate.setChatMenuButton(chatId, menuButton)
        result.consume()
        return result
    }

    override suspend fun hideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.hideGeneralForumTopic(chatId)
        result.consume()
        return result
    }

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
    ): TResult<Message> {
        val result = delegate.sendPoll(chatId, question, options, businessConnectionId, messageThreadId, questionParseMode, questionEntities, isAnonymous, type, allowsMultipleAnswers, allowsRevoting, shuffleOptions, allowAddingOptions, hideResultsUntilCloses, correctOptionIds, explanation, explanationParseMode, explanationEntities, openPeriod, closeDate, isClosed, description, descriptionParseMode, descriptionEntities, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getChatMenuButton(
        chatId: Long?
    ): TResult<MenuButton> {
        val result = delegate.getChatMenuButton(chatId)
        result.consume()
        return result
    }

    override suspend fun getUpdates(
        offset: Long?,
        limit: Long?,
        timeout: Long?,
        allowedUpdates: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<List<Update>> {
        val result = delegate.getUpdates(offset, limit, timeout, allowedUpdates, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setMyName(
        name: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result = delegate.setMyName(name, languageCode)
        result.consume()
        return result
    }

    override suspend fun setBusinessAccountName(
        businessConnectionId: String,
        firstName: String,
        lastName: String?
    ): TResult<Boolean> {
        val result = delegate.setBusinessAccountName(businessConnectionId, firstName, lastName)
        result.consume()
        return result
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
        val result = delegate.copyMessages(chatId, fromChatId, messageIds, messageThreadId, directMessagesTopicId, disableNotification, protectContent, removeCaption)
        result.consume()
        return result
    }

    override suspend fun unpinChatMessage(
        chatId: ChatId,
        businessConnectionId: String?,
        messageId: Long?
    ): TResult<Boolean> {
        val result = delegate.unpinChatMessage(chatId, businessConnectionId, messageId)
        result.consume()
        return result
    }

    override suspend fun stopMessageLiveLocation(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> {
        val result = delegate.stopMessageLiveLocation(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setStickerEmojiList(
        sticker: String,
        emojiList: List<String>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setStickerEmojiList(sticker, emojiList, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getMyDescription(
        languageCode: String?
    ): TResult<BotDescription> {
        val result = delegate.getMyDescription(languageCode)
        result.consume()
        return result
    }

    override suspend fun getChatMember(
        chatId: ChatId,
        userId: Long
    ): TResult<ChatMember> {
        val result = delegate.getChatMember(chatId, userId)
        result.consume()
        return result
    }

    override suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): TResult<UserProfilePhotos> {
        val result = delegate.getUserProfilePhotos(userId, offset, limit)
        result.consume()
        return result
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
        val result = delegate.sendDocument(chatId, document, businessConnectionId, messageThreadId, directMessagesTopicId, thumbnail, caption, parseMode, captionEntities, disableContentTypeDetection, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        val result = delegate.createChatInviteLink(chatId, name, expireDate, memberLimit, createsJoinRequest)
        result.consume()
        return result
    }

    override suspend fun getStarTransactions(
        offset: Long?,
        limit: Long?
    ): TResult<StarTransactions> {
        val result = delegate.getStarTransactions(offset, limit)
        result.consume()
        return result
    }

    override suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String
    ): TResult<Boolean> {
        val result = delegate.setChatStickerSet(chatId, stickerSetName)
        result.consume()
        return result
    }

    override suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result = delegate.setMyShortDescription(shortDescription, languageCode)
        result.consume()
        return result
    }

    override suspend fun uploadStickerFile(
        userId: Long,
        sticker: TelegramFile,
        stickerFormat: StickerFormat
    ): TResult<File> {
        val result = delegate.uploadStickerFile(userId, sticker, stickerFormat)
        result.consume()
        return result
    }

    override suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        val result = delegate.editChatInviteLink(chatId, inviteLink, name, expireDate, memberLimit, createsJoinRequest)
        result.consume()
        return result
    }

    override suspend fun leaveChat(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.leaveChat(chatId)
        result.consume()
        return result
    }

    override suspend fun closeGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.closeGeneralForumTopic(chatId)
        result.consume()
        return result
    }

    override suspend fun savePreparedKeyboardButton(
        userId: Long,
        button: KeyboardButton,
        requestOptions: RequestOptions?
    ): TResult<PreparedKeyboardButton> {
        val result = delegate.savePreparedKeyboardButton(userId, button, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: StickerFormat,
        thumbnail: TelegramFile?
    ): TResult<Boolean> {
        val result = delegate.setStickerSetThumbnail(name, userId, format, thumbnail)
        result.consume()
        return result
    }

    override suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ): TResult<ChatAdministratorRights> {
        val result = delegate.getMyDefaultAdministratorRights(forChannels)
        result.consume()
        return result
    }

    override suspend fun getMe(): TResult<User> {
        val result = delegate.getMe()
        result.consume()
        return result
    }

    override suspend fun setStickerPositionInSet(
        sticker: String,
        position: Long
    ): TResult<Boolean> {
        val result = delegate.setStickerPositionInSet(sticker, position)
        result.consume()
        return result
    }

    override suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String?
    ): TResult<Boolean> {
        val result = delegate.setCustomEmojiStickerSetThumbnail(name, customEmojiId)
        result.consume()
        return result
    }

    override suspend fun close(): TResult<Boolean> {
        val result = delegate.close()
        result.consume()
        return result
    }

    override suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        disableNotification: Boolean?
    ): TResult<Boolean> {
        val result = delegate.pinChatMessage(chatId, messageId, businessConnectionId, disableNotification)
        result.consume()
        return result
    }

    override suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>,
        requestOptions: RequestOptions?
    ): TResult<List<Sticker>> {
        val result = delegate.getCustomEmojiStickers(customEmojiIds, requestOptions)
        result.consume()
        return result
    }

    override suspend fun upgradeGift(
        businessConnectionId: String,
        ownedGiftId: String,
        keepOriginalDetails: Boolean?,
        starCount: Long?
    ): TResult<Boolean> {
        val result = delegate.upgradeGift(businessConnectionId, ownedGiftId, keepOriginalDetails, starCount)
        result.consume()
        return result
    }

    override suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Long?,
        isPersonal: Boolean?,
        nextOffset: String?,
        button: InlineQueryResultsButton?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.answerInlineQuery(inlineQueryId, results, cacheTime, isPersonal, nextOffset, button, requestOptions)
        result.consume()
        return result
    }

    override suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): TResult<ChatInviteLink> {
        val result = delegate.revokeChatInviteLink(chatId, inviteLink)
        result.consume()
        return result
    }

    override suspend fun transferBusinessAccountStars(
        businessConnectionId: String,
        starCount: Long
    ): TResult<Boolean> {
        val result = delegate.transferBusinessAccountStars(businessConnectionId, starCount)
        result.consume()
        return result
    }

    override suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): TResult<Boolean> {
        val result = delegate.answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)
        result.consume()
        return result
    }

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
        val result = delegate.sendMessage(chatId, text, businessConnectionId, messageThreadId, directMessagesTopicId, parseMode, entities, linkPreviewOptions, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun sendChatAction(
        chatId: ChatId,
        action: ChatAction,
        businessConnectionId: String?,
        messageThreadId: Long?
    ): TResult<Boolean> {
        val result = delegate.sendChatAction(chatId, action, businessConnectionId, messageThreadId)
        result.consume()
        return result
    }

    override suspend fun createChatSubscriptionInviteLink(
        chatId: ChatId,
        subscriptionPeriod: Long,
        subscriptionPrice: Long,
        name: String?
    ): TResult<ChatInviteLink> {
        val result = delegate.createChatSubscriptionInviteLink(chatId, subscriptionPeriod, subscriptionPrice, name)
        result.consume()
        return result
    }

    override suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long
    ): TResult<Boolean> {
        val result = delegate.deleteMessage(chatId, messageId)
        result.consume()
        return result
    }

    override suspend fun getMyShortDescription(
        languageCode: String?
    ): TResult<BotShortDescription> {
        val result = delegate.getMyShortDescription(languageCode)
        result.consume()
        return result
    }

    override suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.answerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage, requestOptions)
        result.consume()
        return result
    }

    override suspend fun convertGiftToStars(
        businessConnectionId: String,
        ownedGiftId: String
    ): TResult<Boolean> {
        val result = delegate.convertGiftToStars(businessConnectionId, ownedGiftId)
        result.consume()
        return result
    }

    override suspend fun savePreparedInlineMessage(
        userId: Long,
        result: InlineQueryResult,
        allowUserChats: Boolean?,
        allowBotChats: Boolean?,
        allowGroupChats: Boolean?,
        allowChannelChats: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<PreparedInlineMessage> {
        val result = delegate.savePreparedInlineMessage(userId, result, allowUserChats, allowBotChats, allowGroupChats, allowChannelChats, requestOptions)
        result.consume()
        return result
    }

    override suspend fun deleteStory(
        businessConnectionId: String,
        storyId: Long
    ): TResult<Boolean> {
        val result = delegate.deleteStory(businessConnectionId, storyId)
        result.consume()
        return result
    }

    override suspend fun giftPremiumSubscription(
        userId: Long,
        monthCount: Long,
        starCount: Long,
        text: String?,
        textParseMode: ParseMode?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.giftPremiumSubscription(userId, monthCount, starCount, text, textParseMode, textEntities, requestOptions)
        result.consume()
        return result
    }

    override suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String?,
        iconCustomEmojiId: String?
    ): TResult<Boolean> {
        val result = delegate.editForumTopic(chatId, messageThreadId, name, iconCustomEmojiId)
        result.consume()
        return result
    }

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
        val result = delegate.sendContact(chatId, phoneNumber, firstName, businessConnectionId, messageThreadId, directMessagesTopicId, lastName, vcard, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun unpinAllChatMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.unpinAllChatMessages(chatId)
        result.consume()
        return result
    }

    override suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?
    ): TResult<Boolean> {
        val result = delegate.restrictChatMember(chatId, userId, permissions, useIndependentChatPermissions, untilDate)
        result.consume()
        return result
    }

    override suspend fun setBusinessAccountProfilePhoto(
        businessConnectionId: String,
        photo: InputProfilePhoto,
        isPublic: Boolean?
    ): TResult<Boolean> {
        val result = delegate.setBusinessAccountProfilePhoto(businessConnectionId, photo, isPublic)
        result.consume()
        return result
    }

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
    ): TResult<OwnedGifts> {
        val result = delegate.getBusinessAccountGifts(businessConnectionId, excludeUnsaved, excludeSaved, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeUnique, excludeFromBlockchain, sortByPrice, offset, limit)
        result.consume()
        return result
    }

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
    ): TResult<Message> {
        val result = delegate.forwardMessage(chatId, fromChatId, messageId, messageThreadId, directMessagesTopicId, videoStartTimestamp, disableNotification, protectContent, messageEffectId, suggestedPostParameters)
        result.consume()
        return result
    }

    override suspend fun editUserStarSubscription(
        userId: Long,
        telegramPaymentChargeId: String,
        isCanceled: Boolean
    ): TResult<Boolean> {
        val result = delegate.editUserStarSubscription(userId, telegramPaymentChargeId, isCanceled)
        result.consume()
        return result
    }

    override suspend fun getForumTopicIconStickers(): TResult<List<Sticker>> {
        val result = delegate.getForumTopicIconStickers()
        result.consume()
        return result
    }

    override suspend fun setBusinessAccountGiftSettings(
        businessConnectionId: String,
        showGiftButton: Boolean,
        acceptedGiftTypes: AcceptedGiftTypes
    ): TResult<Boolean> {
        val result = delegate.setBusinessAccountGiftSettings(businessConnectionId, showGiftButton, acceptedGiftTypes)
        result.consume()
        return result
    }

    override suspend fun setBusinessAccountBio(
        businessConnectionId: String,
        bio: String?
    ): TResult<Boolean> {
        val result = delegate.setBusinessAccountBio(businessConnectionId, bio)
        result.consume()
        return result
    }

    override suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        val result = delegate.unbanChatSenderChat(chatId, senderChatId)
        result.consume()
        return result
    }

    override suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<List<BotCommand>> {
        val result = delegate.getMyCommands(scope, languageCode)
        result.consume()
        return result
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
        val result = delegate.setWebhook(url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken)
        result.consume()
        return result
    }

    override suspend fun setChatMemberTag(
        chatId: ChatId,
        userId: Long,
        tag: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setChatMemberTag(chatId, userId, tag, requestOptions)
        result.consume()
        return result
    }

    override suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?
    ): TResult<Boolean> {
        val result = delegate.setStickerMaskPosition(sticker, maskPosition)
        result.consume()
        return result
    }

    override suspend fun verifyUser(
        userId: Long,
        customDescription: String?
    ): TResult<Boolean> {
        val result = delegate.verifyUser(userId, customDescription)
        result.consume()
        return result
    }

    override suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> {
        val result = delegate.deleteWebhook(dropPendingUpdates)
        result.consume()
        return result
    }

    override suspend fun sendGift(
        giftId: String,
        userId: Long?,
        chatId: ChatId?,
        payForUpgrade: Boolean?,
        text: String?,
        textParseMode: ParseMode?,
        textEntities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.sendGift(giftId, userId, chatId, payForUpgrade, text, textParseMode, textEntities, requestOptions)
        result.consume()
        return result
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
    ): TResult<Story> {
        val result = delegate.postStory(businessConnectionId, content, activePeriod, caption, parseMode, captionEntities, areas, postToChatPage, protectContent)
        result.consume()
        return result
    }

    override suspend fun getFile(
        fileId: String
    ): TResult<File> {
        val result = delegate.getFile(fileId)
        result.consume()
        return result
    }

    override suspend fun transferGift(
        businessConnectionId: String,
        ownedGiftId: String,
        newOwnerChatId: Long,
        starCount: Long?
    ): TResult<Boolean> {
        val result = delegate.transferGift(businessConnectionId, ownedGiftId, newOwnerChatId, starCount)
        result.consume()
        return result
    }

    override suspend fun setStickerKeywords(
        sticker: String,
        keywords: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setStickerKeywords(sticker, keywords, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: Long
    ): TResult<UserChatBoosts> {
        val result = delegate.getUserChatBoosts(chatId, userId)
        result.consume()
        return result
    }

    override suspend fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult<List<GameHighScore>> {
        val result = delegate.getGameHighScores(userId, chatId, messageId, inlineMessageId)
        result.consume()
        return result
    }

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
        val result = delegate.createInvoiceLink(title, description, payload, currency, prices, businessConnectionId, providerToken, subscriptionPeriod, maxTipAmount, suggestedTipAmounts, providerData, photoUrl, photoSize, photoWidth, photoHeight, needName, needPhoneNumber, needEmail, needShippingAddress, sendPhoneNumberToProvider, sendEmailToProvider, isFlexible, requestOptions)
        result.consume()
        return result
    }

    override suspend fun reopenGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.reopenGeneralForumTopic(chatId)
        result.consume()
        return result
    }

    override suspend fun deleteChatStickerSet(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.deleteChatStickerSet(chatId)
        result.consume()
        return result
    }

    override suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String
    ): TResult<Boolean> {
        val result = delegate.editGeneralForumTopic(chatId, name)
        result.consume()
        return result
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
        val result = delegate.sendVoice(chatId, voice, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, duration, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        val result = delegate.banChatSenderChat(chatId, senderChatId)
        result.consume()
        return result
    }

    override suspend fun getWebhookInfo(): TResult<WebhookInfo> {
        val result = delegate.getWebhookInfo()
        result.consume()
        return result
    }

    override suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setMyCommands(commands, scope, languageCode, requestOptions)
        result.consume()
        return result
    }

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
        val result = delegate.editMessageText(text, businessConnectionId, chatId, messageId, inlineMessageId, parseMode, entities, linkPreviewOptions, replyMarkup, requestOptions)
        result.consume()
        return result
    }

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
        val result = delegate.sendInvoice(chatId, title, description, payload, currency, prices, messageThreadId, directMessagesTopicId, providerToken, maxTipAmount, suggestedTipAmounts, startParameter, providerData, photoUrl, photoSize, photoWidth, photoHeight, needName, needPhoneNumber, needEmail, needShippingAddress, sendPhoneNumberToProvider, sendEmailToProvider, isFlexible, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getMyName(
        languageCode: String?
    ): TResult<BotName> {
        val result = delegate.getMyName(languageCode)
        result.consume()
        return result
    }

    override suspend fun setMessageReaction(
        chatId: ChatId,
        messageId: Long,
        reaction: List<ReactionType>?,
        isBig: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.setMessageReaction(chatId, messageId, reaction, isBig, requestOptions)
        result.consume()
        return result
    }

    override suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?
    ): TResult<Boolean> {
        val result = delegate.unbanChatMember(chatId, userId, onlyIfBanned)
        result.consume()
        return result
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
        val result = delegate.sendVideoNote(chatId, videoNote, businessConnectionId, messageThreadId, directMessagesTopicId, duration, length, thumbnail, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?
    ): TResult<Boolean> {
        val result = delegate.setMyDefaultAdministratorRights(rights, forChannels)
        result.consume()
        return result
    }

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
    ): TResult<OwnedGifts> {
        val result = delegate.getChatGifts(chatId, excludeUnsaved, excludeSaved, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeFromBlockchain, excludeUnique, sortByPrice, offset, limit, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> {
        val result = delegate.getChat(chatId)
        result.consume()
        return result
    }

    override suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<Boolean> {
        val result = delegate.deleteMyCommands(scope, languageCode)
        result.consume()
        return result
    }

    override suspend fun sendMessageDraft(
        chatId: Long,
        draftId: Long,
        text: String,
        messageThreadId: Long?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.sendMessageDraft(chatId, draftId, text, messageThreadId, parseMode, entities, requestOptions)
        result.consume()
        return result
    }

    override suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Long?,
        iconCustomEmojiId: String?
    ): TResult<ForumTopic> {
        val result = delegate.createForumTopic(chatId, name, iconColor, iconCustomEmojiId)
        result.consume()
        return result
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
        val result = delegate.sendMediaGroup(chatId, media, businessConnectionId, messageThreadId, directMessagesTopicId, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters)
        result.consume()
        return result
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
        val result = delegate.sendVideo(chatId, video, businessConnectionId, messageThreadId, directMessagesTopicId, duration, width, height, thumbnail, cover, startTimestamp, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, supportsStreaming, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun replaceManagedBotToken(
        userId: Long,
        requestOptions: RequestOptions?
    ): TResult<String> {
        val result = delegate.replaceManagedBotToken(userId, requestOptions)
        result.consume()
        return result
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
    ): TResult<Boolean> {
        val result = delegate.promoteChatMember(chatId, userId, isAnonymous, canManageChat, canDeleteMessages, canManageVideoChats, canRestrictMembers, canPromoteMembers, canChangeInfo, canInviteUsers, canPostStories, canEditStories, canDeleteStories, canPostMessages, canEditMessages, canPinMessages, canManageTopics, canManageDirectMessages, canManageTags)
        result.consume()
        return result
    }

    override suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result = delegate.unpinAllForumTopicMessages(chatId, messageThreadId)
        result.consume()
        return result
    }

    override suspend fun setBusinessAccountUsername(
        businessConnectionId: String,
        username: String?
    ): TResult<Boolean> {
        val result = delegate.setBusinessAccountUsername(businessConnectionId, username)
        result.consume()
        return result
    }

    override suspend fun setGameScore(
        userId: Long,
        score: Long,
        force: Boolean?,
        disableEditMessage: Boolean?,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult.Either<Message, Boolean> {
        val result = delegate.setGameScore(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
        result.consume()
        return result
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
    ): TResult<Message> {
        val result = delegate.sendPaidMedia(chatId, starCount, media, businessConnectionId, messageThreadId, directMessagesTopicId, payload, caption, parseMode, captionEntities, showCaptionAboveMedia, disableNotification, protectContent, allowPaidBroadcast, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun removeChatVerification(
        chatId: ChatId
    ): TResult<Boolean> {
        val result = delegate.removeChatVerification(chatId)
        result.consume()
        return result
    }

    override suspend fun getBusinessConnection(
        businessConnectionId: String
    ): TResult<BusinessConnection> {
        val result = delegate.getBusinessConnection(businessConnectionId)
        result.consume()
        return result
    }

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
        val result = delegate.sendGame(chatId, gameShortName, businessConnectionId, messageThreadId, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

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
    ): TResult<OwnedGifts> {
        val result = delegate.getUserGifts(userId, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeFromBlockchain, excludeUnique, sortByPrice, offset, limit, requestOptions)
        result.consume()
        return result
    }

    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        val result = delegate.declineChatJoinRequest(chatId, userId)
        result.consume()
        return result
    }

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
        val result = delegate.sendVenue(chatId, latitude, longitude, title, address, businessConnectionId, messageThreadId, directMessagesTopicId, foursquareId, foursquareType, googlePlaceId, googlePlaceType, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun stopPoll(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Poll> {
        val result = delegate.stopPoll(chatId, messageId, businessConnectionId, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        val result = delegate.approveChatJoinRequest(chatId, userId)
        result.consume()
        return result
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
        val result = delegate.sendAnimation(chatId, animation, businessConnectionId, messageThreadId, directMessagesTopicId, duration, width, height, thumbnail, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

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
        val result = delegate.sendChecklist(businessConnectionId, chatId, checklist, disableNotification, protectContent, messageEffectId, replyParameters, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun declineSuggestedPost(
        chatId: Long,
        messageId: Long,
        comment: String?
    ): TResult<Boolean> {
        val result = delegate.declineSuggestedPost(chatId, messageId, comment)
        result.consume()
        return result
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
        val result = delegate.forwardMessages(chatId, fromChatId, messageIds, messageThreadId, directMessagesTopicId, disableNotification, protectContent)
        result.consume()
        return result
    }

    override suspend fun getStickerSet(
        name: String
    ): TResult<StickerSet> {
        val result = delegate.getStickerSet(name)
        result.consume()
        return result
    }

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
        val result = delegate.editMessageLiveLocation(latitude, longitude, businessConnectionId, chatId, messageId, inlineMessageId, livePeriod, horizontalAccuracy, heading, proximityAlertRadius, replyMarkup, requestOptions)
        result.consume()
        return result
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
        val result = delegate.sendAudio(chatId, audio, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, duration, performer, title, thumbnail, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun deleteStickerFromSet(
        sticker: String
    ): TResult<Boolean> {
        val result = delegate.deleteStickerFromSet(sticker)
        result.consume()
        return result
    }

    override suspend fun deleteBusinessMessages(
        businessConnectionId: String,
        messageIds: List<Long>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        val result = delegate.deleteBusinessMessages(businessConnectionId, messageIds, requestOptions)
        result.consume()
        return result
    }

    override suspend fun deleteStickerSet(
        name: String
    ): TResult<Boolean> {
        val result = delegate.deleteStickerSet(name)
        result.consume()
        return result
    }

    override suspend fun repostStory(
        businessConnectionId: String,
        fromChatId: Long,
        fromStoryId: Long,
        activePeriod: Long,
        postToChatPage: Boolean?,
        protectContent: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Story> {
        val result = delegate.repostStory(businessConnectionId, fromChatId, fromStoryId, activePeriod, postToChatPage, protectContent, requestOptions)
        result.consume()
        return result
    }

    override suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult,
        requestOptions: RequestOptions?
    ): TResult<SentWebAppMessage> {
        val result = delegate.answerWebAppQuery(webAppQueryId, result, requestOptions)
        result.consume()
        return result
    }

    override suspend fun getMyStarBalance(): TResult<StarAmount> {
        val result = delegate.getMyStarBalance()
        result.consume()
        return result
    }

    override suspend fun setStickerSetTitle(
        name: String,
        title: String
    ): TResult<Boolean> {
        val result = delegate.setStickerSetTitle(name, title)
        result.consume()
        return result
    }

    override suspend fun exportChatInviteLink(
        chatId: ChatId
    ): TResult<String> {
        val result = delegate.exportChatInviteLink(chatId)
        result.consume()
        return result
    }

    override suspend fun editMessageMedia(
        media: InputMedia,
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        val result = delegate.editMessageMedia(media, businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup)
        result.consume()
        return result
    }

    override suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        val result = delegate.closeForumTopic(chatId, messageThreadId)
        result.consume()
        return result
    }

    override suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        val result = delegate.replaceStickerInSet(userId, name, oldSticker, sticker)
        result.consume()
        return result
    }

    override suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?
    ): TResult<Boolean> {
        val result = delegate.answerCallbackQuery(callbackQueryId, text, showAlert, url, cacheTime)
        result.consume()
        return result
    }

    override suspend fun setMyDescription(
        description: String?,
        languageCode: String?
    ): TResult<Boolean> {
        val result = delegate.setMyDescription(description, languageCode)
        result.consume()
        return result
    }

    override suspend fun approveSuggestedPost(
        chatId: Long,
        messageId: Long,
        sendDate: Long?
    ): TResult<Boolean> {
        val result = delegate.approveSuggestedPost(chatId, messageId, sendDate)
        result.consume()
        return result
    }

    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        val result = delegate.addStickerToSet(userId, name, sticker)
        result.consume()
        return result
    }

    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: StickerType?,
        needsRepainting: Boolean?
    ): TResult<Boolean> {
        val result = delegate.createNewStickerSet(userId, name, title, stickers, stickerType, needsRepainting)
        result.consume()
        return result
    }

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
        val result = delegate.editMessageCaption(businessConnectionId, chatId, messageId, inlineMessageId, caption, parseMode, captionEntities, showCaptionAboveMedia, replyMarkup, requestOptions)
        result.consume()
        return result
    }

    override suspend fun logOut(): TResult<Boolean> {
        val result = delegate.logOut()
        result.consume()
        return result
    }

}

