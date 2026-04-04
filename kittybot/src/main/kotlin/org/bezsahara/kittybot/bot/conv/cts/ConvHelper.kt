package org.bezsahara.kittybot.bot.conv.cts

import kotlinx.coroutines.Deferred
import org.bezsahara.kittybot.bot.action.other.withStartOf
import org.bezsahara.kittybot.bot.conv.scope.OnMsgScope
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.games.Game
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.media.Animation
import org.bezsahara.kittybot.telegram.classes.media.Audio
import org.bezsahara.kittybot.telegram.classes.media.Contact
import org.bezsahara.kittybot.telegram.classes.media.Dice
import org.bezsahara.kittybot.telegram.classes.media.Document
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import org.bezsahara.kittybot.telegram.classes.media.Video
import org.bezsahara.kittybot.telegram.classes.media.VideoNote
import org.bezsahara.kittybot.telegram.classes.media.Voice
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.media.geo.Venue
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.payments.Invoice

private fun OnMsgScope.originChatId() = message.chat.id.toChatId()

private fun OnMsgScope.originUserId(): Long {
    return requireNotNull(message.from?.id) {
        "Callback query waiters require the conversation to start from a user message"
    }
}

/**
 * Waits for the next message from the same chat as the conversation start update.
 *
 * The returned [Deferred] is owned by the current coroutine, so timeout or cancellation of
 * that coroutine unregisters the waiter automatically.
 */
suspend fun OnMsgScope.receiveMessage(check: ((Message) -> Boolean)? = null): Deferred<Message> {
    return receive(MessageCatcher(check, originChatId()))
}

suspend fun OnMsgScope.receiveCommand(command: String): Deferred<Message> {
    return receive(MessageCatcher({ it.text?.withStartOf(command) == true }, originChatId()))
}

/**
 * Waits for the next text message from the same chat as the conversation start update.
 *
 * Use [check] to reject texts that should not resume the conversation yet.
 */
suspend fun OnMsgScope.receiveText(check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(TextCatcher(check, originChatId()))
}

/**
 * Waits for the next animation message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveAnimation(check: ((Animation) -> Boolean)? = null): Deferred<Animation> {
    return receive(AnimationCatcher(check, originChatId()))
}

/**
 * Waits for the next audio message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveAudio(check: ((Audio) -> Boolean)? = null): Deferred<Audio> {
    return receive(AudioCatcher(check, originChatId()))
}

/**
 * Waits for the next document message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveDocument(check: ((Document) -> Boolean)? = null): Deferred<Document> {
    return receive(DocumentCatcher(check, originChatId()))
}

/**
 * Waits for the next photo message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receivePhotos(check: ((List<PhotoSize>) -> Boolean)? = null): Deferred<List<PhotoSize>> {
    return receive(PhotoCatcher(check, originChatId()))
}

/**
 * Waits for the next sticker message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveSticker(check: ((Sticker) -> Boolean)? = null): Deferred<Sticker> {
    return receive(StickerCatcher(check, originChatId()))
}

/**
 * Waits for the next video message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveVideo(check: ((Video) -> Boolean)? = null): Deferred<Video> {
    return receive(VideoCatcher(check, originChatId()))
}

/**
 * Waits for the next video note message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveVideoNote(check: ((VideoNote) -> Boolean)? = null): Deferred<VideoNote> {
    return receive(VideoNoteCatcher(check, originChatId()))
}

/**
 * Waits for the next voice message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveVoice(check: ((Voice) -> Boolean)? = null): Deferred<Voice> {
    return receive(VoiceCatcher(check, originChatId()))
}

/**
 * Waits for the next contact message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveContact(check: ((Contact) -> Boolean)? = null): Deferred<Contact> {
    return receive(ContactCatcher(check, originChatId()))
}

/**
 * Waits for the next dice message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveDice(check: ((Dice) -> Boolean)? = null): Deferred<Dice> {
    return receive(DiceCatcher(check, originChatId()))
}

/**
 * Waits for the next game message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveGame(check: ((Game) -> Boolean)? = null): Deferred<Game> {
    return receive(GameCatcher(check, originChatId()))
}

/**
 * Waits for the next poll message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receivePoll(check: ((Poll) -> Boolean)? = null): Deferred<Poll> {
    return receive(PollCatcher(check, originChatId()))
}

/**
 * Waits for the next venue message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveVenue(check: ((Venue) -> Boolean)? = null): Deferred<Venue> {
    return receive(VenueCatcher(check, originChatId()))
}

/**
 * Waits for the next location message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveLocation(check: ((Location) -> Boolean)? = null): Deferred<Location> {
    return receive(LocationCatcher(check, originChatId()))
}

/**
 * Waits for the next invoice message from the same chat as the conversation start update.
 */
suspend fun OnMsgScope.receiveInvoice(check: ((Invoice) -> Boolean)? = null): Deferred<Invoice> {
    return receive(InvoiceCatcher(check, originChatId()))
}

/**
 * Waits for the next callback query from the user who started the conversation.
 *
 * This helper requires the conversation start message to have a sender.
 */
suspend fun OnMsgScope.receiveCallbackQuery(check: ((CallbackQuery) -> Boolean)? = null): Deferred<CallbackQuery> {
    return receive(CallbackQueryCatcher(check, originUserId()))
}

/**
 * Waits for the next callback data payload from the user who started the conversation.
 *
 * Use [check] to accept only specific callback payloads.
 */
suspend fun OnMsgScope.receiveCallbackData(check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackDataCatcher(check, originUserId()))
}

/**
 * Waits for the next callback query with an attached message from the user who started the conversation.
 */
suspend fun OnMsgScope.receiveCallbackMessage(
    check: ((MaybeInaccessibleMessage) -> Boolean)? = null
): Deferred<MaybeInaccessibleMessage> {
    return receive(CallbackMessageCatcher(check, originUserId()))
}

/**
 * Waits for the next inline callback message identifier from the user who started the conversation.
 */
suspend fun OnMsgScope.receiveCallbackInlineMessageId(check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackInlineMessageIdCatcher(check, originUserId()))
}

/**
 * Waits for the next callback game short name from the user who started the conversation.
 */
suspend fun OnMsgScope.receiveCallbackGameShortName(check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackGameShortNameCatcher(check, originUserId()))
}
