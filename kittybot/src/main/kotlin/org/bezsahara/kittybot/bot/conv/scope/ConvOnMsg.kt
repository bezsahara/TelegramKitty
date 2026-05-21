package org.bezsahara.kittybot.bot.conv.scope

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.conv.*
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.message.Message


val StartCommand
    get() = ConvHandlerBuilder<OnMsgScope> {
        ConvOnMsg(
            convInfo,
            MsgCheck { m -> m.text?.startsWith("/start") == true },
            it
        )
    }


fun msgCheckBuilder(c: MsgCheck) = ConvHandlerBuilder<OnMsgScope> {
    ConvOnMsg(convInfo, c, it)
}

fun fullMsgCheckBuilder(c: FullMsgCheck) = ConvHandlerBuilder<OnMsgScope> {
    ConvOnMsg(convInfo, c, it)
}

class ConvOnMsg(
    val convInfo: ConvInfo,
    private val msgCheck: FullMsgCheck,
    val block: suspend OnMsgScope.() -> Unit,
) : ConvHandler<OnMsgScope>() {
    override val allowedKinds: Set<UpdKind>? get() = null

    override val scope: CoroutineScope
        get() = convInfo.scope

    private val runtime = convInfo.runtime

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        if (handleWaiters(update, handlerContext) !== Decision.Next) {
            return Decision.Consumed
        }

        val message = (update as? MessageUpdate)?.message ?: return Decision.Next
        if (!msgCheck.check(message, handlerContext)) return Decision.Next

        val peerId = ChatId(message.chat.id)
        val claim = runtime.tryStart(peerId) ?: return Decision.Next

        val job = scope.launch {
            try {
                OnMsgScope(
                    bot,
                    update,
                    handlerContext,
                    convInfo,
                    this,
                    this@ConvOnMsg
                ).block()
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                errorHandler(t)
            } finally {
                coroutineContext.cancelChildren()
            }
        }
        job.invokeOnCompletion { runtime.finish(peerId, claim) }
        return Decision.Consumed
    }

    companion object {
        var errorHandler: (Throwable) -> Unit = { it.printStackTrace() }
    }
}

/**
 * Predicate used by message-based conversation handlers to decide whether an incoming
 * message should start a new conversation.
 */
fun interface MsgCheck : FullMsgCheck {
    override fun check(
        message: Message,
        handlerContext: HandlerContext,
    ): Boolean {
        return check(message)
    }

    fun check(message: Message): Boolean
}

fun interface FullMsgCheck {
    fun check(message: Message, handlerContext: HandlerContext): Boolean
}

class OnMsgScope(
    override val bot: KittyBot,
    override val update: MessageUpdate,
    override val handlerContext: HandlerContext,
    override val convInfo: ConvInfo,
    override val scope: CoroutineScope,
    override val chc: CatcherHandlerCentral
) : ConvScope(), CoroutineScope by scope, HandlerScope<MessageUpdate> {
    val message: Message get() = update.message
}
