package org.bezsahara.kittybot.bot.conv.cts

import kotlinx.coroutines.Deferred
import org.bezsahara.kittybot.bot.action.other.withStartOf
import org.bezsahara.kittybot.bot.conv.scope.OnMsgScope
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.games.Game
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.media.*
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.media.geo.Venue
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.payments.Invoice

fun OnMsgScope.originUserId(): Long {
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
suspend inline fun OnMsgScope.receiveMessage(noinline check: ((Message) -> Boolean)? = null): Deferred<Message> {
    return receive(MessageCatcher(check, message.chat.id.toChatId()))
}

suspend inline fun OnMsgScope.receiveCommand(command: String): Deferred<Message> {
    return receive(MessageCatcher({ it.text?.withStartOf(command) == true }, message.chat.id.toChatId()))
}

/**
 * Waits for the next text message from the same chat as the conversation start update.
 *
 * Use [check] to reject texts that should not resume the conversation yet.
 */
suspend inline fun OnMsgScope.receiveText(noinline check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(TextCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next animation message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveAnimation(noinline check: ((Animation) -> Boolean)? = null): Deferred<Animation> {
    return receive(AnimationCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next audio message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveAudio(noinline check: ((Audio) -> Boolean)? = null): Deferred<Audio> {
    return receive(AudioCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next document message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveDocument(noinline check: ((Document) -> Boolean)? = null): Deferred<Document> {
    return receive(DocumentCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next photo message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receivePhotos(noinline check: ((List<PhotoSize>) -> Boolean)? = null): Deferred<List<PhotoSize>> {
    return receive(PhotoCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next sticker message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveSticker(noinline check: ((Sticker) -> Boolean)? = null): Deferred<Sticker> {
    return receive(StickerCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next video message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveVideo(noinline check: ((Video) -> Boolean)? = null): Deferred<Video> {
    return receive(VideoCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next video note message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveVideoNote(noinline check: ((VideoNote) -> Boolean)? = null): Deferred<VideoNote> {
    return receive(VideoNoteCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next voice message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveVoice(noinline check: ((Voice) -> Boolean)? = null): Deferred<Voice> {
    return receive(VoiceCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next contact message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveContact(noinline check: ((Contact) -> Boolean)? = null): Deferred<Contact> {
    return receive(ContactCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next dice message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveDice(noinline check: ((Dice) -> Boolean)? = null): Deferred<Dice> {
    return receive(DiceCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next game message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveGame(noinline check: ((Game) -> Boolean)? = null): Deferred<Game> {
    return receive(GameCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next poll message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receivePoll(noinline check: ((Poll) -> Boolean)? = null): Deferred<Poll> {
    return receive(PollCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next venue message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveVenue(noinline check: ((Venue) -> Boolean)? = null): Deferred<Venue> {
    return receive(VenueCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next location message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveLocation(noinline check: ((Location) -> Boolean)? = null): Deferred<Location> {
    return receive(LocationCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next invoice message from the same chat as the conversation start update.
 */
suspend inline fun OnMsgScope.receiveInvoice(noinline check: ((Invoice) -> Boolean)? = null): Deferred<Invoice> {
    return receive(InvoiceCatcher(check, message.chat.id.toChatId()))
}

/**
 * Waits for the next callback query from the user who started the conversation.
 *
 * This helper requires the conversation start message to have a sender.
 */
suspend inline fun OnMsgScope.receiveCallbackQuery(noinline check: ((CallbackQuery) -> Boolean)? = null): Deferred<CallbackQuery> {
    return receive(CallbackQueryCatcher(check, originUserId()))
}

/**
 * Waits for the next callback data payload from the user who started the conversation.
 *
 * Use [check] to accept only specific callback payloads.
 */
suspend inline fun OnMsgScope.receiveCallbackData(noinline check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackDataCatcher(check, originUserId()))
}

/**
 * Waits for the next callback query with an attached message from the user who started the conversation.
 */
suspend inline fun OnMsgScope.receiveCallbackMessage(
    noinline check: ((MaybeInaccessibleMessage) -> Boolean)? = null
): Deferred<MaybeInaccessibleMessage> {
    return receive(CallbackMessageCatcher(check, originUserId()))
}

/**
 * Waits for the next inline callback message identifier from the user who started the conversation.
 */
suspend inline fun OnMsgScope.receiveCallbackInlineMessageId(noinline check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackInlineMessageIdCatcher(check, originUserId()))
}

/**
 * Waits for the next callback game short name from the user who started the conversation.
 */
suspend inline fun OnMsgScope.receiveCallbackGameShortName(noinline check: ((String) -> Boolean)? = null): Deferred<String> {
    return receive(CallbackGameShortNameCatcher(check, originUserId()))
}
