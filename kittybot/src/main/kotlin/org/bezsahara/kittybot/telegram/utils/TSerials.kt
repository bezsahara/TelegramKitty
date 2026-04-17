package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.telegram.classes.chat.member.ChatMember
import kotlinx.serialization.SerializationStrategy
import org.bezsahara.kittybot.telegram.classes.keyboard.PreparedKeyboardButton
import org.bezsahara.kittybot.telegram.classes.media.story.StoryArea
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGifts
import org.bezsahara.kittybot.telegram.classes.chat.ChatFullInfo
import org.bezsahara.kittybot.telegram.classes.bot.BotName
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.chat.ForumTopic
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.media.story.Story
import org.bezsahara.kittybot.telegram.classes.input.InputSticker
import org.bezsahara.kittybot.telegram.classes.core.MessageId
import org.bezsahara.kittybot.telegram.classes.inline.PreparedInlineMessage
import org.bezsahara.kittybot.telegram.classes.gifts.Gifts
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.input.InputPaidMedia
import org.bezsahara.kittybot.telegram.classes.inline.SentWebAppMessage
import org.bezsahara.kittybot.telegram.classes.core.WebhookInfo
import org.bezsahara.kittybot.telegram.classes.payments.StarTransactions
import org.bezsahara.kittybot.telegram.client.Ok
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.classes.user.UserProfilePhotos
import org.bezsahara.kittybot.telegram.classes.business.BusinessConnection
import org.bezsahara.kittybot.telegram.classes.chat.ChatAdministratorRights
import org.bezsahara.kittybot.telegram.classes.keyboard.MenuButton
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlinx.serialization.builtins.ListSerializer
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.core.File
import org.bezsahara.kittybot.telegram.classes.bot.BotShortDescription
import org.bezsahara.kittybot.telegram.classes.bot.BotDescription
import org.bezsahara.kittybot.telegram.classes.media.stickers.StickerSet
import kotlinx.serialization.builtins.serializer
import org.bezsahara.kittybot.telegram.classes.chat.ChatInviteLink
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.user.UserProfileAudios
import org.bezsahara.kittybot.telegram.classes.games.GameHighScore
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.payments.StarAmount
import org.bezsahara.kittybot.telegram.classes.chat.boosts.UserChatBoosts


object TSerials {
    @JvmField val sBusinessConnection: DeserializationStrategy<Ok<BusinessConnection>> = Ok.serializer(BusinessConnection.serializer())
    @JvmField val sPoll: DeserializationStrategy<Ok<Poll>> = Ok.serializer(Poll.serializer())
    @JvmField val sListMessageId: DeserializationStrategy<Ok<List<MessageId>>> = Ok.serializer(ListSerializer(MessageId.serializer()))
    @JvmField val sPreparedKeyboardButton: DeserializationStrategy<Ok<PreparedKeyboardButton>> = Ok.serializer(PreparedKeyboardButton.serializer())
    @JvmField val aListInputSticker: SerializationStrategy<List<InputSticker>> = ListSerializer(InputSticker.serializer())
    @JvmField val sChatAdministratorRights: DeserializationStrategy<Ok<ChatAdministratorRights>> = Ok.serializer(ChatAdministratorRights.serializer())
    @JvmField val sSentWebAppMessage: DeserializationStrategy<Ok<SentWebAppMessage>> = Ok.serializer(SentWebAppMessage.serializer())
    @JvmField val sListUpdate: DeserializationStrategy<Ok<List<Update>>> = Ok.serializer(ListSerializer(Update.serializer()))
    @JvmField val sListGameHighScore: DeserializationStrategy<Ok<List<GameHighScore>>> = Ok.serializer(ListSerializer(GameHighScore.serializer()))
    @JvmField val sChatInviteLink: DeserializationStrategy<Ok<ChatInviteLink>> = Ok.serializer(ChatInviteLink.serializer())
    @JvmField val sListMessage: DeserializationStrategy<Ok<List<Message>>> = Ok.serializer(ListSerializer(Message.serializer()))
    @JvmField val sListBotCommand: DeserializationStrategy<Ok<List<BotCommand>>> = Ok.serializer(ListSerializer(BotCommand.serializer()))
    @JvmField val sBotDescription: DeserializationStrategy<Ok<BotDescription>> = Ok.serializer(BotDescription.serializer())
    @JvmField val sFile: DeserializationStrategy<Ok<File>> = Ok.serializer(File.serializer())
    @JvmField val sLong: DeserializationStrategy<Ok<Long>> = Ok.serializer(Long.serializer())
    @JvmField val sMenuButton: DeserializationStrategy<Ok<MenuButton>> = Ok.serializer(MenuButton.serializer())
    @JvmField val sStory: DeserializationStrategy<Ok<Story>> = Ok.serializer(Story.serializer())
    @JvmField val sUserProfileAudios: DeserializationStrategy<Ok<UserProfileAudios>> = Ok.serializer(UserProfileAudios.serializer())
    @JvmField val sStickerSet: DeserializationStrategy<Ok<StickerSet>> = Ok.serializer(StickerSet.serializer())
    @JvmField val sListSticker: DeserializationStrategy<Ok<List<Sticker>>> = Ok.serializer(ListSerializer(Sticker.serializer()))
    @JvmField val sForumTopic: DeserializationStrategy<Ok<ForumTopic>> = Ok.serializer(ForumTopic.serializer())
    @JvmField val sGifts: DeserializationStrategy<Ok<Gifts>> = Ok.serializer(Gifts.serializer())
    @JvmField val sStarAmount: DeserializationStrategy<Ok<StarAmount>> = Ok.serializer(StarAmount.serializer())
    @JvmField val sListChatMember: DeserializationStrategy<Ok<List<ChatMember>>> = Ok.serializer(ListSerializer(ChatMember.serializer()))
    @JvmField val sUser: DeserializationStrategy<Ok<User>> = Ok.serializer(User.serializer())
    @JvmField val sOwnedGifts: DeserializationStrategy<Ok<OwnedGifts>> = Ok.serializer(OwnedGifts.serializer())
    @JvmField val sUserChatBoosts: DeserializationStrategy<Ok<UserChatBoosts>> = Ok.serializer(UserChatBoosts.serializer())
    @JvmField val sString: DeserializationStrategy<Ok<String>> = Ok.serializer(String.serializer())
    @JvmField val sBotShortDescription: DeserializationStrategy<Ok<BotShortDescription>> = Ok.serializer(BotShortDescription.serializer())
    @JvmField val sBotName: DeserializationStrategy<Ok<BotName>> = Ok.serializer(BotName.serializer())
    @JvmField val sMessage: DeserializationStrategy<Ok<Message>> = Ok.serializer(Message.serializer())
    @JvmField val sUserProfilePhotos: DeserializationStrategy<Ok<UserProfilePhotos>> = Ok.serializer(UserProfilePhotos.serializer())
    @JvmField val aListString: SerializationStrategy<List<String>> = ListSerializer(String.serializer())
    @JvmField val sWebhookInfo: DeserializationStrategy<Ok<WebhookInfo>> = Ok.serializer(WebhookInfo.serializer())
    @JvmField val aListStoryArea: SerializationStrategy<List<StoryArea>> = ListSerializer(StoryArea.serializer())
    @JvmField val aListInputPaidMedia: SerializationStrategy<List<InputPaidMedia>> = ListSerializer(InputPaidMedia.serializer())
    @JvmField val aListMessageEntity: SerializationStrategy<List<MessageEntity>> = ListSerializer(MessageEntity.serializer())
    @JvmField val sChatMember: DeserializationStrategy<Ok<ChatMember>> = Ok.serializer(ChatMember.serializer())
    @JvmField val sStarTransactions: DeserializationStrategy<Ok<StarTransactions>> = Ok.serializer(StarTransactions.serializer())
    @JvmField val sMessageId: DeserializationStrategy<Ok<MessageId>> = Ok.serializer(MessageId.serializer())
    @JvmField val sChatFullInfo: DeserializationStrategy<Ok<ChatFullInfo>> = Ok.serializer(ChatFullInfo.serializer())
    @JvmField val sPreparedInlineMessage: DeserializationStrategy<Ok<PreparedInlineMessage>> = Ok.serializer(PreparedInlineMessage.serializer())
}

