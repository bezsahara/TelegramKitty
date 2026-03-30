package org.bezsahara.kittybot.bot.conv.cts

import org.bezsahara.kittybot.bot.conv.CatcherHandler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.inline.CallbackQuery
import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage

/**
 * Base class for built-in callback query catchers that only accept queries from one user.
 */
abstract class SameUserCallbackQueryCatcher<T>(
    private val userId: Long
) : CatcherHandler<T> {
    final override val updateKind: UpdateKind<*> get() = CallbackQueryUpdate

    protected fun sameUserCallbackQueryOrNull(update: Update): CallbackQuery? {
        update as CallbackQueryUpdate
        val callbackQuery = update.callbackQuery
        if (callbackQuery.from.id != userId) return null
        return callbackQuery
    }

    protected fun valueIfMatched(value: T?, checker: ((T) -> Boolean)?): T? {
        if (value == null) return null
        if (checker != null && !checker(value)) return null
        return value
    }
}

/**
 * Base class for built-in catchers that extract one callback query field and optionally validate it.
 */
open class CallbackQueryFieldCatcher<T>(
    private val checker: ((T) -> Boolean)?,
    userId: Long,
    private val extractor: (CallbackQuery) -> T?,
) : SameUserCallbackQueryCatcher<T>(userId) {
    override suspend fun catchOrNull(
        update: Update,
        handlerContext: HandlerContext,
    ): T? {
        return valueIfMatched(
            sameUserCallbackQueryOrNull(update)?.let(extractor),
            checker
        )
    }
}

/**
 * Waiter matcher for any callback query from a specific user.
 */
class CallbackQueryCatcher(
    private val checker: ((CallbackQuery) -> Boolean)?,
    userId: Long
) : SameUserCallbackQueryCatcher<CallbackQuery>(userId) {
    override suspend fun catchOrNull(
        update: Update,
        handlerContext: HandlerContext,
    ): CallbackQuery? {
        return valueIfMatched(sameUserCallbackQueryOrNull(update), checker)
    }
}

/**
 * Waiter matcher for callback data payloads from a specific user.
 */
class CallbackDataCatcher(checker: ((String) -> Boolean)?, userId: Long) :
    CallbackQueryFieldCatcher<String>(checker, userId, CallbackQuery::data)

/**
 * Waiter matcher for callback queries that carry a message from a specific user.
 */
class CallbackMessageCatcher(checker: ((MaybeInaccessibleMessage) -> Boolean)?, userId: Long) :
    CallbackQueryFieldCatcher<MaybeInaccessibleMessage>(checker, userId, CallbackQuery::message)

/**
 * Waiter matcher for inline callback query message identifiers from a specific user.
 */
class CallbackInlineMessageIdCatcher(checker: ((String) -> Boolean)?, userId: Long) :
    CallbackQueryFieldCatcher<String>(checker, userId, CallbackQuery::inlineMessageId)

/**
 * Waiter matcher for game callback short names from a specific user.
 */
class CallbackGameShortNameCatcher(checker: ((String) -> Boolean)?, userId: Long) :
    CallbackQueryFieldCatcher<String>(checker, userId, CallbackQuery::gameShortName)
