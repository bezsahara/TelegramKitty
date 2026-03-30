package org.bezsahara.kittybot.bot.conv.cts

import org.bezsahara.kittybot.bot.conv.CatcherHandler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.games.Game
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
import org.bezsahara.kittybot.telegram.classes.message.polls.Poll
import org.bezsahara.kittybot.telegram.classes.payments.Invoice

/**
 * Base class for built-in message catchers that only accept messages from one chat.
 */
abstract class SameChatMessageCatcher<T>(
    ofChatId: ChatId
) : CatcherHandler<T> {
    private val ofChatId = ofChatId.value.toLong()
    final override val updateKind: UpdateKind<*> get() = MessageUpdate

    protected fun sameChatMessageOrNull(update: Update): Message? {
        update as MessageUpdate
        val message = update.message
        if (message.chat.id != ofChatId) return null
        return message
    }

    protected fun valueIfMatched(value: T?, checker: ((T) -> Boolean)?): T? {
        if (value == null) return null
        if (checker != null && !checker(value)) return null
        return value
    }
}

/**
 * Base class for built-in catchers that extract one message field and optionally validate it.
 */
open class MessageFieldCatcher<T>(
    private val checker: ((T) -> Boolean)?,
    ofChatId: ChatId,
    private val extractor: (Message) -> T?,
) : SameChatMessageCatcher<T>(ofChatId) {
    final override suspend fun catchOrNull(
        update: Update,
        handlerContext: HandlerContext,
    ): T? {
        return valueIfMatched(
            sameChatMessageOrNull(update)?.let(extractor),
            checker
        )
    }
}

/**
 * Waiter matcher for any message from a specific chat.
 */
class MessageCatcher(private val checker: ((Message) -> Boolean)?, ofChatId: ChatId) : SameChatMessageCatcher<Message>(ofChatId) {
    override suspend fun catchOrNull(
        update: Update,
        handlerContext: HandlerContext,
    ): Message? {
        return valueIfMatched(sameChatMessageOrNull(update), checker)
    }
}

/**
 * Waiter matcher for text messages from a specific chat.
 */
class TextCatcher(checker: ((String) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<String>(checker, ofChatId, Message::text)

/**
 * Waiter matcher for animation messages from a specific chat.
 */
class AnimationCatcher(checker: ((Animation) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Animation>(checker, ofChatId, Message::animation)

/**
 * Waiter matcher for audio messages from a specific chat.
 */
class AudioCatcher(checker: ((Audio) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Audio>(checker, ofChatId, Message::audio)

/**
 * Waiter matcher for document messages from a specific chat.
 */
class DocumentCatcher(checker: ((Document) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Document>(checker, ofChatId, Message::document)

/**
 * Waiter matcher for photo messages from a specific chat.
 */
class PhotoCatcher(checker: ((List<PhotoSize>) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<List<PhotoSize>>(checker, ofChatId, Message::photo) {
    constructor(ofChatId: ChatId) : this(null, ofChatId)
}

/**
 * Waiter matcher for sticker messages from a specific chat.
 */
class StickerCatcher(checker: ((Sticker) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Sticker>(checker, ofChatId, Message::sticker)

/**
 * Waiter matcher for video messages from a specific chat.
 */
class VideoCatcher(checker: ((Video) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Video>(checker, ofChatId, Message::video)

/**
 * Waiter matcher for video note messages from a specific chat.
 */
class VideoNoteCatcher(checker: ((VideoNote) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<VideoNote>(checker, ofChatId, Message::videoNote)

/**
 * Waiter matcher for voice messages from a specific chat.
 */
class VoiceCatcher(checker: ((Voice) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Voice>(checker, ofChatId, Message::voice)

/**
 * Waiter matcher for contact messages from a specific chat.
 */
class ContactCatcher(checker: ((Contact) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Contact>(checker, ofChatId, Message::contact)

/**
 * Waiter matcher for dice messages from a specific chat.
 */
class DiceCatcher(checker: ((Dice) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Dice>(checker, ofChatId, Message::dice)

/**
 * Waiter matcher for game messages from a specific chat.
 */
class GameCatcher(checker: ((Game) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Game>(checker, ofChatId, Message::game)

/**
 * Waiter matcher for poll messages from a specific chat.
 */
class PollCatcher(checker: ((Poll) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Poll>(checker, ofChatId, Message::poll)

/**
 * Waiter matcher for venue messages from a specific chat.
 */
class VenueCatcher(checker: ((Venue) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Venue>(checker, ofChatId, Message::venue)

/**
 * Waiter matcher for location messages from a specific chat.
 */
class LocationCatcher(checker: ((Location) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Location>(checker, ofChatId, Message::location)

/**
 * Waiter matcher for invoice messages from a specific chat.
 */
class InvoiceCatcher(checker: ((Invoice) -> Boolean)?, ofChatId: ChatId) :
    MessageFieldCatcher<Invoice>(checker, ofChatId, Message::invoice)
