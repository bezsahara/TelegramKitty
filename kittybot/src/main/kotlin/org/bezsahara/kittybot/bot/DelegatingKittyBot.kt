package org.bezsahara.kittybot.bot

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import org.bezsahara.kittybot.telegram.classes.keyboard.PreparedKeyboardButton
import org.bezsahara.kittybot.telegram.classes.rich.InputRichMessage
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.values.StickerType
import org.bezsahara.kittybot.telegram.classes.input.InputMedia
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.values.ChatAction
import org.bezsahara.kittybot.telegram.values.ChatJoinRequestQueryResult
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.utils.TResult
import org.bezsahara.kittybot.telegram.values.DiceEmoji
import org.bezsahara.kittybot.telegram.classes.core.MessageId
import org.bezsahara.kittybot.telegram.classes.inline.PreparedInlineMessage
import org.bezsahara.kittybot.telegram.classes.payments.ShippingOption
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import org.bezsahara.kittybot.telegram.classes.inline.SentGuestMessage
import org.bezsahara.kittybot.telegram.classes.input.InputProfilePhoto
import org.bezsahara.kittybot.telegram.classes.core.WebhookInfo
import org.bezsahara.kittybot.telegram.classes.bot.BotAccessSettings
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
import org.bezsahara.kittybot.telegram.classes.input.InputPollMedia
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


open class DelegatingKittyBot(
    protected val delegate: KittyBot
) : KittyBot() {
    override suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Long>
    ): TResult<Boolean> {
        return delegate.deleteMessages(chatId, messageIds)
    }

    override suspend fun setChatPhoto(
        chatId: ChatId,
        photo: TelegramFile
    ): TResult<Boolean> {
        return delegate.setChatPhoto(chatId, photo)
    }

    override suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.unpinAllGeneralForumTopicMessages(chatId)
    }

    override suspend fun editChatSubscriptionInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?
    ): TResult<ChatInviteLink> {
        return delegate.editChatSubscriptionInviteLink(chatId, inviteLink, name)
    }

    override suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        return delegate.deleteForumTopic(chatId, messageThreadId)
    }

    override suspend fun readBusinessMessage(
        businessConnectionId: String,
        chatId: Long,
        messageId: Long
    ): TResult<Boolean> {
        return delegate.readBusinessMessage(businessConnectionId, chatId, messageId)
    }

    override suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?
    ): TResult<Boolean> {
        return delegate.setChatPermissions(chatId, permissions, useIndependentChatPermissions)
    }

    override suspend fun setMyProfilePhoto(
        photo: InputProfilePhoto
    ): TResult<Boolean> {
        return delegate.setMyProfilePhoto(photo)
    }

    override suspend fun editMessageReplyMarkup(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> {
        return delegate.editMessageReplyMarkup(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup, requestOptions)
    }

    override suspend fun banChatMember(
        chatId: ChatId,
        userId: Long,
        untilDate: Long?,
        revokeMessages: Boolean?
    ): TResult<Boolean> {
        return delegate.banChatMember(chatId, userId, untilDate, revokeMessages)
    }

    override suspend fun getBusinessAccountStarBalance(
        businessConnectionId: String
    ): TResult<StarAmount> {
        return delegate.getBusinessAccountStarBalance(businessConnectionId)
    }

    override suspend fun unhideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.unhideGeneralForumTopic(chatId)
    }

    override suspend fun verifyChat(
        chatId: ChatId,
        customDescription: String?
    ): TResult<Boolean> {
        return delegate.verifyChat(chatId, customDescription)
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
        return delegate.editStory(businessConnectionId, storyId, content, caption, parseMode, captionEntities, areas)
    }

    override suspend fun deleteChatPhoto(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.deleteChatPhoto(chatId)
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
        return delegate.copyMessage(chatId, fromChatId, messageId, messageThreadId, directMessagesTopicId, videoStartTimestamp, caption, parseMode, captionEntities, showCaptionAboveMedia, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        return delegate.reopenForumTopic(chatId, messageThreadId)
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
        return delegate.sendDice(chatId, businessConnectionId, messageThreadId, directMessagesTopicId, emoji, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun removeMyProfilePhoto(): TResult<Boolean> {
        return delegate.removeMyProfilePhoto()
    }

    override suspend fun refundStarPayment(
        userId: Long,
        telegramPaymentChargeId: String
    ): TResult<Boolean> {
        return delegate.refundStarPayment(userId, telegramPaymentChargeId)
    }

    override suspend fun getAvailableGifts(): TResult<Gifts> {
        return delegate.getAvailableGifts()
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
        return delegate.sendLocation(chatId, latitude, longitude, businessConnectionId, messageThreadId, directMessagesTopicId, horizontalAccuracy, livePeriod, heading, proximityAlertRadius, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: Long,
        customTitle: String
    ): TResult<Boolean> {
        return delegate.setChatAdministratorCustomTitle(chatId, userId, customTitle)
    }

    override suspend fun setUserEmojiStatus(
        userId: Long,
        emojiStatusCustomEmojiId: String?,
        emojiStatusExpirationDate: Long?
    ): TResult<Boolean> {
        return delegate.setUserEmojiStatus(userId, emojiStatusCustomEmojiId, emojiStatusExpirationDate)
    }

    override suspend fun setChatTitle(
        chatId: ChatId,
        title: String
    ): TResult<Boolean> {
        return delegate.setChatTitle(chatId, title)
    }

    override suspend fun sendRichMessageDraft(
        chatId: Long,
        draftId: Long,
        richMessage: InputRichMessage,
        messageThreadId: Long?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.sendRichMessageDraft(chatId, draftId, richMessage, messageThreadId, requestOptions)
    }

    override suspend fun setChatDescription(
        chatId: ChatId,
        description: String?
    ): TResult<Boolean> {
        return delegate.setChatDescription(chatId, description)
    }

    override suspend fun getChatAdministrators(
        chatId: ChatId,
        returnBots: Boolean?
    ): TResult<List<ChatMember>> {
        return delegate.getChatAdministrators(chatId, returnBots)
    }

    override suspend fun getManagedBotToken(
        userId: Long
    ): TResult<String> {
        return delegate.getManagedBotToken(userId)
    }

    override suspend fun getChatMemberCount(
        chatId: ChatId
    ): TResult<Long> {
        return delegate.getChatMemberCount(chatId)
    }

    override suspend fun removeUserVerification(
        userId: Long
    ): TResult<Boolean> {
        return delegate.removeUserVerification(userId)
    }

    override suspend fun removeBusinessAccountProfilePhoto(
        businessConnectionId: String,
        isPublic: Boolean?
    ): TResult<Boolean> {
        return delegate.removeBusinessAccountProfilePhoto(businessConnectionId, isPublic)
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
        return delegate.sendPhoto(chatId, photo, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun deleteMessageReaction(
        chatId: ChatId,
        messageId: Long,
        userId: Long?,
        actorChatId: Long?
    ): TResult<Boolean> {
        return delegate.deleteMessageReaction(chatId, messageId, userId, actorChatId)
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
        return delegate.sendSticker(chatId, sticker, businessConnectionId, messageThreadId, directMessagesTopicId, emoji, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun getUserProfileAudios(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): TResult<UserProfileAudios> {
        return delegate.getUserProfileAudios(userId, offset, limit)
    }

    override suspend fun editMessageChecklist(
        businessConnectionId: String,
        chatId: ChatId,
        messageId: Long,
        checklist: InputChecklist,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> {
        return delegate.editMessageChecklist(businessConnectionId, chatId, messageId, checklist, replyMarkup, requestOptions)
    }

    override suspend fun setPassportDataErrors(
        userId: Long,
        errors: List<PassportElementError>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.setPassportDataErrors(userId, errors, requestOptions)
    }

    override suspend fun setChatMenuButton(
        chatId: Long?,
        menuButton: MenuButton?
    ): TResult<Boolean> {
        return delegate.setChatMenuButton(chatId, menuButton)
    }

    override suspend fun hideGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.hideGeneralForumTopic(chatId)
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
        membersOnly: Boolean?,
        countryCodes: List<String>?,
        correctOptionIds: List<Long>?,
        explanation: String?,
        explanationParseMode: ParseMode?,
        explanationEntities: List<MessageEntity>?,
        explanationMedia: InputPollMedia?,
        openPeriod: Long?,
        closeDate: Long?,
        isClosed: Boolean?,
        description: String?,
        descriptionParseMode: ParseMode?,
        descriptionEntities: List<MessageEntity>?,
        media: InputPollMedia?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?
    ): TResult<Message> {
        return delegate.sendPoll(chatId, question, options, businessConnectionId, messageThreadId, questionParseMode, questionEntities, isAnonymous, type, allowsMultipleAnswers, allowsRevoting, shuffleOptions, allowAddingOptions, hideResultsUntilCloses, membersOnly, countryCodes, correctOptionIds, explanation, explanationParseMode, explanationEntities, explanationMedia, openPeriod, closeDate, isClosed, description, descriptionParseMode, descriptionEntities, media, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters, replyMarkup)
    }

    override suspend fun getChatMenuButton(
        chatId: Long?
    ): TResult<MenuButton> {
        return delegate.getChatMenuButton(chatId)
    }

    override suspend fun getUpdates(
        offset: Long?,
        limit: Long?,
        timeout: Long?,
        allowedUpdates: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<List<Update>> {
        return delegate.getUpdates(offset, limit, timeout, allowedUpdates, requestOptions)
    }

    override suspend fun sendChatJoinRequestWebApp(
        chatJoinRequestQueryId: String,
        webAppUrl: String
    ): TResult<Boolean> {
        return delegate.sendChatJoinRequestWebApp(chatJoinRequestQueryId, webAppUrl)
    }

    override suspend fun setMyName(
        name: String?,
        languageCode: String?
    ): TResult<Boolean> {
        return delegate.setMyName(name, languageCode)
    }

    override suspend fun setBusinessAccountName(
        businessConnectionId: String,
        firstName: String,
        lastName: String?
    ): TResult<Boolean> {
        return delegate.setBusinessAccountName(businessConnectionId, firstName, lastName)
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
        return delegate.copyMessages(chatId, fromChatId, messageIds, messageThreadId, directMessagesTopicId, disableNotification, protectContent, removeCaption)
    }

    override suspend fun unpinChatMessage(
        chatId: ChatId,
        businessConnectionId: String?,
        messageId: Long?
    ): TResult<Boolean> {
        return delegate.unpinChatMessage(chatId, businessConnectionId, messageId)
    }

    override suspend fun stopMessageLiveLocation(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> {
        return delegate.stopMessageLiveLocation(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup, requestOptions)
    }

    override suspend fun setStickerEmojiList(
        sticker: String,
        emojiList: List<String>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.setStickerEmojiList(sticker, emojiList, requestOptions)
    }

    override suspend fun getMyDescription(
        languageCode: String?
    ): TResult<BotDescription> {
        return delegate.getMyDescription(languageCode)
    }

    override suspend fun getChatMember(
        chatId: ChatId,
        userId: Long
    ): TResult<ChatMember> {
        return delegate.getChatMember(chatId, userId)
    }

    override suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Long?,
        limit: Long?
    ): TResult<UserProfilePhotos> {
        return delegate.getUserProfilePhotos(userId, offset, limit)
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
        return delegate.sendDocument(chatId, document, businessConnectionId, messageThreadId, directMessagesTopicId, thumbnail, caption, parseMode, captionEntities, disableContentTypeDetection, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        return delegate.createChatInviteLink(chatId, name, expireDate, memberLimit, createsJoinRequest)
    }

    override suspend fun getStarTransactions(
        offset: Long?,
        limit: Long?
    ): TResult<StarTransactions> {
        return delegate.getStarTransactions(offset, limit)
    }

    override suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String
    ): TResult<Boolean> {
        return delegate.setChatStickerSet(chatId, stickerSetName)
    }

    override suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?
    ): TResult<Boolean> {
        return delegate.setMyShortDescription(shortDescription, languageCode)
    }

    override suspend fun uploadStickerFile(
        userId: Long,
        sticker: TelegramFile,
        stickerFormat: StickerFormat
    ): TResult<File> {
        return delegate.uploadStickerFile(userId, sticker, stickerFormat)
    }

    override suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String?,
        expireDate: Long?,
        memberLimit: Long?,
        createsJoinRequest: Boolean?
    ): TResult<ChatInviteLink> {
        return delegate.editChatInviteLink(chatId, inviteLink, name, expireDate, memberLimit, createsJoinRequest)
    }

    override suspend fun leaveChat(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.leaveChat(chatId)
    }

    override suspend fun closeGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.closeGeneralForumTopic(chatId)
    }

    override suspend fun getManagedBotAccessSettings(
        userId: Long
    ): TResult<BotAccessSettings> {
        return delegate.getManagedBotAccessSettings(userId)
    }

    override suspend fun savePreparedKeyboardButton(
        userId: Long,
        button: KeyboardButton,
        requestOptions: RequestOptions?
    ): TResult<PreparedKeyboardButton> {
        return delegate.savePreparedKeyboardButton(userId, button, requestOptions)
    }

    override suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: StickerFormat,
        thumbnail: TelegramFile?
    ): TResult<Boolean> {
        return delegate.setStickerSetThumbnail(name, userId, format, thumbnail)
    }

    override suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean?
    ): TResult<ChatAdministratorRights> {
        return delegate.getMyDefaultAdministratorRights(forChannels)
    }

    override suspend fun getMe(): TResult<User> {
        return delegate.getMe()
    }

    override suspend fun setStickerPositionInSet(
        sticker: String,
        position: Long
    ): TResult<Boolean> {
        return delegate.setStickerPositionInSet(sticker, position)
    }

    override suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String?
    ): TResult<Boolean> {
        return delegate.setCustomEmojiStickerSetThumbnail(name, customEmojiId)
    }

    override suspend fun sendLivePhoto(
        chatId: ChatId,
        livePhoto: TelegramFile,
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
        return delegate.sendLivePhoto(chatId, livePhoto, photo, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun getUserPersonalChatMessages(
        userId: Long,
        limit: Long
    ): TResult<List<Message>> {
        return delegate.getUserPersonalChatMessages(userId, limit)
    }

    override suspend fun close(): TResult<Boolean> {
        return delegate.close()
    }

    override suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        disableNotification: Boolean?
    ): TResult<Boolean> {
        return delegate.pinChatMessage(chatId, messageId, businessConnectionId, disableNotification)
    }

    override suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>,
        requestOptions: RequestOptions?
    ): TResult<List<Sticker>> {
        return delegate.getCustomEmojiStickers(customEmojiIds, requestOptions)
    }

    override suspend fun upgradeGift(
        businessConnectionId: String,
        ownedGiftId: String,
        keepOriginalDetails: Boolean?,
        starCount: Long?
    ): TResult<Boolean> {
        return delegate.upgradeGift(businessConnectionId, ownedGiftId, keepOriginalDetails, starCount)
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
        return delegate.answerInlineQuery(inlineQueryId, results, cacheTime, isPersonal, nextOffset, button, requestOptions)
    }

    override suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String
    ): TResult<ChatInviteLink> {
        return delegate.revokeChatInviteLink(chatId, inviteLink)
    }

    override suspend fun transferBusinessAccountStars(
        businessConnectionId: String,
        starCount: Long
    ): TResult<Boolean> {
        return delegate.transferBusinessAccountStars(businessConnectionId, starCount)
    }

    override suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String?
    ): TResult<Boolean> {
        return delegate.answerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)
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
        return delegate.sendMessage(chatId, text, businessConnectionId, messageThreadId, directMessagesTopicId, parseMode, entities, linkPreviewOptions, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun sendChatAction(
        chatId: ChatId,
        action: ChatAction,
        businessConnectionId: String?,
        messageThreadId: Long?
    ): TResult<Boolean> {
        return delegate.sendChatAction(chatId, action, businessConnectionId, messageThreadId)
    }

    override suspend fun createChatSubscriptionInviteLink(
        chatId: ChatId,
        subscriptionPeriod: Long,
        subscriptionPrice: Long,
        name: String?
    ): TResult<ChatInviteLink> {
        return delegate.createChatSubscriptionInviteLink(chatId, subscriptionPeriod, subscriptionPrice, name)
    }

    override suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Long
    ): TResult<Boolean> {
        return delegate.deleteMessage(chatId, messageId)
    }

    override suspend fun getMyShortDescription(
        languageCode: String?
    ): TResult<BotShortDescription> {
        return delegate.getMyShortDescription(languageCode)
    }

    override suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.answerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage, requestOptions)
    }

    override suspend fun convertGiftToStars(
        businessConnectionId: String,
        ownedGiftId: String
    ): TResult<Boolean> {
        return delegate.convertGiftToStars(businessConnectionId, ownedGiftId)
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
        return delegate.savePreparedInlineMessage(userId, result, allowUserChats, allowBotChats, allowGroupChats, allowChannelChats, requestOptions)
    }

    override suspend fun deleteStory(
        businessConnectionId: String,
        storyId: Long
    ): TResult<Boolean> {
        return delegate.deleteStory(businessConnectionId, storyId)
    }

    override suspend fun setManagedBotAccessSettings(
        userId: Long,
        isAccessRestricted: Boolean,
        addedUserIds: List<Long>?
    ): TResult<Boolean> {
        return delegate.setManagedBotAccessSettings(userId, isAccessRestricted, addedUserIds)
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
        return delegate.giftPremiumSubscription(userId, monthCount, starCount, text, textParseMode, textEntities, requestOptions)
    }

    override suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Long,
        name: String?,
        iconCustomEmojiId: String?
    ): TResult<Boolean> {
        return delegate.editForumTopic(chatId, messageThreadId, name, iconCustomEmojiId)
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
        return delegate.sendContact(chatId, phoneNumber, firstName, businessConnectionId, messageThreadId, directMessagesTopicId, lastName, vcard, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun unpinAllChatMessages(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.unpinAllChatMessages(chatId)
    }

    override suspend fun restrictChatMember(
        chatId: ChatId,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean?,
        untilDate: Long?
    ): TResult<Boolean> {
        return delegate.restrictChatMember(chatId, userId, permissions, useIndependentChatPermissions, untilDate)
    }

    override suspend fun setBusinessAccountProfilePhoto(
        businessConnectionId: String,
        photo: InputProfilePhoto,
        isPublic: Boolean?
    ): TResult<Boolean> {
        return delegate.setBusinessAccountProfilePhoto(businessConnectionId, photo, isPublic)
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
        return delegate.getBusinessAccountGifts(businessConnectionId, excludeUnsaved, excludeSaved, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeUnique, excludeFromBlockchain, sortByPrice, offset, limit)
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
        return delegate.forwardMessage(chatId, fromChatId, messageId, messageThreadId, directMessagesTopicId, videoStartTimestamp, disableNotification, protectContent, messageEffectId, suggestedPostParameters)
    }

    override suspend fun editUserStarSubscription(
        userId: Long,
        telegramPaymentChargeId: String,
        isCanceled: Boolean
    ): TResult<Boolean> {
        return delegate.editUserStarSubscription(userId, telegramPaymentChargeId, isCanceled)
    }

    override suspend fun getForumTopicIconStickers(): TResult<List<Sticker>> {
        return delegate.getForumTopicIconStickers()
    }

    override suspend fun setBusinessAccountGiftSettings(
        businessConnectionId: String,
        showGiftButton: Boolean,
        acceptedGiftTypes: AcceptedGiftTypes
    ): TResult<Boolean> {
        return delegate.setBusinessAccountGiftSettings(businessConnectionId, showGiftButton, acceptedGiftTypes)
    }

    override suspend fun setBusinessAccountBio(
        businessConnectionId: String,
        bio: String?
    ): TResult<Boolean> {
        return delegate.setBusinessAccountBio(businessConnectionId, bio)
    }

    override suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        return delegate.unbanChatSenderChat(chatId, senderChatId)
    }

    override suspend fun getMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<List<BotCommand>> {
        return delegate.getMyCommands(scope, languageCode)
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
        return delegate.setWebhook(url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken)
    }

    override suspend fun setChatMemberTag(
        chatId: ChatId,
        userId: Long,
        tag: String?
    ): TResult<Boolean> {
        return delegate.setChatMemberTag(chatId, userId, tag)
    }

    override suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?
    ): TResult<Boolean> {
        return delegate.setStickerMaskPosition(sticker, maskPosition)
    }

    override suspend fun verifyUser(
        userId: Long,
        customDescription: String?
    ): TResult<Boolean> {
        return delegate.verifyUser(userId, customDescription)
    }

    override suspend fun deleteWebhook(
        dropPendingUpdates: Boolean?
    ): TResult<Boolean> {
        return delegate.deleteWebhook(dropPendingUpdates)
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
        return delegate.sendGift(giftId, userId, chatId, payForUpgrade, text, textParseMode, textEntities, requestOptions)
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
        return delegate.postStory(businessConnectionId, content, activePeriod, caption, parseMode, captionEntities, areas, postToChatPage, protectContent)
    }

    override suspend fun getFile(
        fileId: String
    ): TResult<File> {
        return delegate.getFile(fileId)
    }

    override suspend fun transferGift(
        businessConnectionId: String,
        ownedGiftId: String,
        newOwnerChatId: Long,
        starCount: Long?
    ): TResult<Boolean> {
        return delegate.transferGift(businessConnectionId, ownedGiftId, newOwnerChatId, starCount)
    }

    override suspend fun setStickerKeywords(
        sticker: String,
        keywords: List<String>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.setStickerKeywords(sticker, keywords, requestOptions)
    }

    override suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: Long
    ): TResult<UserChatBoosts> {
        return delegate.getUserChatBoosts(chatId, userId)
    }

    override suspend fun getGameHighScores(
        userId: Long,
        chatId: Long?,
        messageId: Long?,
        inlineMessageId: String?
    ): TResult<List<GameHighScore>> {
        return delegate.getGameHighScores(userId, chatId, messageId, inlineMessageId)
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
        return delegate.createInvoiceLink(title, description, payload, currency, prices, businessConnectionId, providerToken, subscriptionPeriod, maxTipAmount, suggestedTipAmounts, providerData, photoUrl, photoSize, photoWidth, photoHeight, needName, needPhoneNumber, needEmail, needShippingAddress, sendPhoneNumberToProvider, sendEmailToProvider, isFlexible, requestOptions)
    }

    override suspend fun reopenGeneralForumTopic(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.reopenGeneralForumTopic(chatId)
    }

    override suspend fun deleteChatStickerSet(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.deleteChatStickerSet(chatId)
    }

    override suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String
    ): TResult<Boolean> {
        return delegate.editGeneralForumTopic(chatId, name)
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
        return delegate.sendVoice(chatId, voice, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, duration, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: Long
    ): TResult<Boolean> {
        return delegate.banChatSenderChat(chatId, senderChatId)
    }

    override suspend fun getWebhookInfo(): TResult<WebhookInfo> {
        return delegate.getWebhookInfo()
    }

    override suspend fun answerGuestQuery(
        guestQueryId: String,
        result: InlineQueryResult,
        requestOptions: RequestOptions?
    ): TResult<SentGuestMessage> {
        return delegate.answerGuestQuery(guestQueryId, result, requestOptions)
    }

    override suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope?,
        languageCode: String?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.setMyCommands(commands, scope, languageCode, requestOptions)
    }

    override suspend fun editMessageText(
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        text: String?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        linkPreviewOptions: LinkPreviewOptions?,
        richMessage: InputRichMessage?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult.Either<Message, Boolean> {
        return delegate.editMessageText(businessConnectionId, chatId, messageId, inlineMessageId, text, parseMode, entities, linkPreviewOptions, richMessage, replyMarkup, requestOptions)
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
        return delegate.sendInvoice(chatId, title, description, payload, currency, prices, messageThreadId, directMessagesTopicId, providerToken, maxTipAmount, suggestedTipAmounts, startParameter, providerData, photoUrl, photoSize, photoWidth, photoHeight, needName, needPhoneNumber, needEmail, needShippingAddress, sendPhoneNumberToProvider, sendEmailToProvider, isFlexible, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun getMyName(
        languageCode: String?
    ): TResult<BotName> {
        return delegate.getMyName(languageCode)
    }

    override suspend fun setMessageReaction(
        chatId: ChatId,
        messageId: Long,
        reaction: List<ReactionType>?,
        isBig: Boolean?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.setMessageReaction(chatId, messageId, reaction, isBig, requestOptions)
    }

    override suspend fun unbanChatMember(
        chatId: ChatId,
        userId: Long,
        onlyIfBanned: Boolean?
    ): TResult<Boolean> {
        return delegate.unbanChatMember(chatId, userId, onlyIfBanned)
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
        return delegate.sendVideoNote(chatId, videoNote, businessConnectionId, messageThreadId, directMessagesTopicId, duration, length, thumbnail, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights?,
        forChannels: Boolean?
    ): TResult<Boolean> {
        return delegate.setMyDefaultAdministratorRights(rights, forChannels)
    }

    override suspend fun deleteAllMessageReactions(
        chatId: ChatId,
        userId: Long?,
        actorChatId: Long?
    ): TResult<Boolean> {
        return delegate.deleteAllMessageReactions(chatId, userId, actorChatId)
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
        limit: Long?
    ): TResult<OwnedGifts> {
        return delegate.getChatGifts(chatId, excludeUnsaved, excludeSaved, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeFromBlockchain, excludeUnique, sortByPrice, offset, limit)
    }

    override suspend fun getChat(
        chatId: ChatId
    ): TResult<ChatFullInfo> {
        return delegate.getChat(chatId)
    }

    override suspend fun deleteMyCommands(
        scope: BotCommandScope?,
        languageCode: String?
    ): TResult<Boolean> {
        return delegate.deleteMyCommands(scope, languageCode)
    }

    override suspend fun sendMessageDraft(
        chatId: Long,
        draftId: Long,
        messageThreadId: Long?,
        text: String?,
        parseMode: ParseMode?,
        entities: List<MessageEntity>?,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.sendMessageDraft(chatId, draftId, messageThreadId, text, parseMode, entities, requestOptions)
    }

    override suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: Long?,
        iconCustomEmojiId: String?
    ): TResult<ForumTopic> {
        return delegate.createForumTopic(chatId, name, iconColor, iconCustomEmojiId)
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
        return delegate.sendMediaGroup(chatId, media, businessConnectionId, messageThreadId, directMessagesTopicId, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters)
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
        return delegate.sendVideo(chatId, video, businessConnectionId, messageThreadId, directMessagesTopicId, duration, width, height, thumbnail, cover, startTimestamp, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, supportsStreaming, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun replaceManagedBotToken(
        userId: Long
    ): TResult<String> {
        return delegate.replaceManagedBotToken(userId)
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
        return delegate.promoteChatMember(chatId, userId, isAnonymous, canManageChat, canDeleteMessages, canManageVideoChats, canRestrictMembers, canPromoteMembers, canChangeInfo, canInviteUsers, canPostStories, canEditStories, canDeleteStories, canPostMessages, canEditMessages, canPinMessages, canManageTopics, canManageDirectMessages, canManageTags)
    }

    override suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        return delegate.unpinAllForumTopicMessages(chatId, messageThreadId)
    }

    override suspend fun setBusinessAccountUsername(
        businessConnectionId: String,
        username: String?
    ): TResult<Boolean> {
        return delegate.setBusinessAccountUsername(businessConnectionId, username)
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
        return delegate.setGameScore(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
    }

    override suspend fun answerChatJoinRequestQuery(
        chatJoinRequestQueryId: String,
        result: ChatJoinRequestQueryResult
    ): TResult<Boolean> {
        return delegate.answerChatJoinRequestQuery(chatJoinRequestQueryId, result)
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
        return delegate.sendPaidMedia(chatId, starCount, media, businessConnectionId, messageThreadId, directMessagesTopicId, payload, caption, parseMode, captionEntities, showCaptionAboveMedia, disableNotification, protectContent, allowPaidBroadcast, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun removeChatVerification(
        chatId: ChatId
    ): TResult<Boolean> {
        return delegate.removeChatVerification(chatId)
    }

    override suspend fun getBusinessConnection(
        businessConnectionId: String
    ): TResult<BusinessConnection> {
        return delegate.getBusinessConnection(businessConnectionId)
    }

    override suspend fun sendGame(
        chatId: ChatId,
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
        return delegate.sendGame(chatId, gameShortName, businessConnectionId, messageThreadId, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, replyParameters, replyMarkup, requestOptions)
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
        limit: Long?
    ): TResult<OwnedGifts> {
        return delegate.getUserGifts(userId, excludeUnlimited, excludeLimitedUpgradable, excludeLimitedNonUpgradable, excludeFromBlockchain, excludeUnique, sortByPrice, offset, limit)
    }

    override suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        return delegate.declineChatJoinRequest(chatId, userId)
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
        return delegate.sendVenue(chatId, latitude, longitude, title, address, businessConnectionId, messageThreadId, directMessagesTopicId, foursquareId, foursquareType, googlePlaceId, googlePlaceType, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun stopPoll(
        chatId: ChatId,
        messageId: Long,
        businessConnectionId: String?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Poll> {
        return delegate.stopPoll(chatId, messageId, businessConnectionId, replyMarkup, requestOptions)
    }

    override suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: Long
    ): TResult<Boolean> {
        return delegate.approveChatJoinRequest(chatId, userId)
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
        return delegate.sendAnimation(chatId, animation, businessConnectionId, messageThreadId, directMessagesTopicId, duration, width, height, thumbnail, caption, parseMode, captionEntities, showCaptionAboveMedia, hasSpoiler, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun sendChecklist(
        businessConnectionId: String,
        chatId: ChatId,
        checklist: InputChecklist,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        messageEffectId: String?,
        replyParameters: ReplyParameters?,
        replyMarkup: InlineKeyboardMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> {
        return delegate.sendChecklist(businessConnectionId, chatId, checklist, disableNotification, protectContent, messageEffectId, replyParameters, replyMarkup, requestOptions)
    }

    override suspend fun declineSuggestedPost(
        chatId: Long,
        messageId: Long,
        comment: String?
    ): TResult<Boolean> {
        return delegate.declineSuggestedPost(chatId, messageId, comment)
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
        return delegate.forwardMessages(chatId, fromChatId, messageIds, messageThreadId, directMessagesTopicId, disableNotification, protectContent)
    }

    override suspend fun getStickerSet(
        name: String
    ): TResult<StickerSet> {
        return delegate.getStickerSet(name)
    }

    override suspend fun sendRichMessage(
        chatId: ChatId,
        richMessage: InputRichMessage,
        businessConnectionId: String?,
        messageThreadId: Long?,
        directMessagesTopicId: Long?,
        disableNotification: Boolean?,
        protectContent: Boolean?,
        allowPaidBroadcast: Boolean?,
        messageEffectId: String?,
        suggestedPostParameters: SuggestedPostParameters?,
        replyParameters: ReplyParameters?,
        replyMarkup: ReplyMarkup?,
        requestOptions: RequestOptions?
    ): TResult<Message> {
        return delegate.sendRichMessage(chatId, richMessage, businessConnectionId, messageThreadId, directMessagesTopicId, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup, requestOptions)
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
        return delegate.editMessageLiveLocation(latitude, longitude, businessConnectionId, chatId, messageId, inlineMessageId, livePeriod, horizontalAccuracy, heading, proximityAlertRadius, replyMarkup, requestOptions)
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
        return delegate.sendAudio(chatId, audio, businessConnectionId, messageThreadId, directMessagesTopicId, caption, parseMode, captionEntities, duration, performer, title, thumbnail, disableNotification, protectContent, allowPaidBroadcast, messageEffectId, suggestedPostParameters, replyParameters, replyMarkup)
    }

    override suspend fun deleteStickerFromSet(
        sticker: String
    ): TResult<Boolean> {
        return delegate.deleteStickerFromSet(sticker)
    }

    override suspend fun deleteBusinessMessages(
        businessConnectionId: String,
        messageIds: List<Long>,
        requestOptions: RequestOptions?
    ): TResult<Boolean> {
        return delegate.deleteBusinessMessages(businessConnectionId, messageIds, requestOptions)
    }

    override suspend fun deleteStickerSet(
        name: String
    ): TResult<Boolean> {
        return delegate.deleteStickerSet(name)
    }

    override suspend fun repostStory(
        businessConnectionId: String,
        fromChatId: Long,
        fromStoryId: Long,
        activePeriod: Long,
        postToChatPage: Boolean?,
        protectContent: Boolean?
    ): TResult<Story> {
        return delegate.repostStory(businessConnectionId, fromChatId, fromStoryId, activePeriod, postToChatPage, protectContent)
    }

    override suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult,
        requestOptions: RequestOptions?
    ): TResult<SentWebAppMessage> {
        return delegate.answerWebAppQuery(webAppQueryId, result, requestOptions)
    }

    override suspend fun getMyStarBalance(): TResult<StarAmount> {
        return delegate.getMyStarBalance()
    }

    override suspend fun setStickerSetTitle(
        name: String,
        title: String
    ): TResult<Boolean> {
        return delegate.setStickerSetTitle(name, title)
    }

    override suspend fun exportChatInviteLink(
        chatId: ChatId
    ): TResult<String> {
        return delegate.exportChatInviteLink(chatId)
    }

    override suspend fun editMessageMedia(
        media: InputMedia,
        businessConnectionId: String?,
        chatId: ChatId?,
        messageId: Long?,
        inlineMessageId: String?,
        replyMarkup: InlineKeyboardMarkup?
    ): TResult.Either<Message, Boolean> {
        return delegate.editMessageMedia(media, businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup)
    }

    override suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Long
    ): TResult<Boolean> {
        return delegate.closeForumTopic(chatId, messageThreadId)
    }

    override suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        return delegate.replaceStickerInSet(userId, name, oldSticker, sticker)
    }

    override suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String?,
        showAlert: Boolean?,
        url: String?,
        cacheTime: Long?
    ): TResult<Boolean> {
        return delegate.answerCallbackQuery(callbackQueryId, text, showAlert, url, cacheTime)
    }

    override suspend fun setMyDescription(
        description: String?,
        languageCode: String?
    ): TResult<Boolean> {
        return delegate.setMyDescription(description, languageCode)
    }

    override suspend fun approveSuggestedPost(
        chatId: Long,
        messageId: Long,
        sendDate: Long?
    ): TResult<Boolean> {
        return delegate.approveSuggestedPost(chatId, messageId, sendDate)
    }

    override suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker
    ): TResult<Boolean> {
        return delegate.addStickerToSet(userId, name, sticker)
    }

    override suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: StickerType?,
        needsRepainting: Boolean?
    ): TResult<Boolean> {
        return delegate.createNewStickerSet(userId, name, title, stickers, stickerType, needsRepainting)
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
        return delegate.editMessageCaption(businessConnectionId, chatId, messageId, inlineMessageId, caption, parseMode, captionEntities, showCaptionAboveMedia, replyMarkup, requestOptions)
    }

    override suspend fun logOut(): TResult<Boolean> {
        return delegate.logOut()
    }

}

