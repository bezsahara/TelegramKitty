package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.bezsahara.kittybot.bot.builder.KittyBotPolling
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.core.update.EditedMessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.client.CustomClient
import org.bezsahara.kittybot.telegram.client.CustomRequest
import org.bezsahara.kittybot.telegram.client.CustomResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class FurballTest {

    @Test
    fun nextSkipsHandlersThatRejectCurrentUpdateKind() = runBlocking {
        val calls = arrayListOf<String>()

        val start = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "start"
            Decision.Next
        }
        val editedOnly = RecordingHandler(allowedKinds = setOf(EditedMessageUpdate)) {
            error("Edited-message handler should have been skipped for MessageUpdate")
        }
        val tail = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "tail"
            Decision.Consumed
        }

        withUpdater(start, editedOnly, tail) { updater ->
            updater.applyHandlers(messageUpdate())
        }

        assertEquals(listOf("start", "tail"), calls)
    }

    @Test
    fun afterNextToWithAdjustSkipsToNextAcceptedHandler() = runBlocking {
        val calls = arrayListOf<String>()
        val anchor = AnchorHandler()

        val router = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "router"
            Decision.AfterNextTo(anchor.identity, adjust = true)
        }
        val editedOnly = RecordingHandler(allowedKinds = setOf(EditedMessageUpdate)) {
            error("Adjust=true should skip handlers that reject current update kind")
        }
        val target = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "target"
            Decision.Consumed
        }

        withUpdater(router, anchor, editedOnly, target) { updater ->
            updater.applyHandlers(messageUpdate())
        }

        assertEquals(listOf("router", "target"), calls)
    }

    @Test
    fun afterNextToSupportsEmptyKindJumpAnchors() = runBlocking {
        val calls = arrayListOf<String>()
        val anchor = AnchorHandler()

        val router = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "router"
            Decision.AfterNextTo(anchor.identity)
        }
        val target = RecordingHandler(allowedKinds = setOf(MessageUpdate)) {
            calls += "target"
            Decision.Consumed
        }

        withUpdater(router, anchor, target) { updater ->
            updater.applyHandlers(messageUpdate())
        }

        assertEquals(listOf("router", "target"), calls)
    }

    private suspend fun withUpdater(
        vararg handlers: Handler,
        block: suspend (Furball) -> Unit,
    ) {
        val config = KittyBotPolling {
            token = "test-token"
            useCustomClient(NoOpCustomClient)
            setErrorHandler(HandlerErrorHandler { e, _, _, _, _ -> throw e })
            dispatchers {
                handlers.forEach(::addHandler)
            }
        }

        try {
            block(config.updater)
        } finally {
            config.supervisorJob.cancel()
            config.close()
        }
    }

    private class RecordingHandler(
        override val allowedKinds: Set<UpdateKind<*>>?,
        private val action: suspend (Update) -> Decision,
    ) : Handler {
        override suspend fun handleUpdate(
            update: Update,
            bot: org.bezsahara.kittybot.bot.KittyBot,
            handlerContext: HandlerContext,
        ): Decision {
            return action(update)
        }
    }

    private class AnchorHandler : Handler {
        override val identity: HandlerIdentity = HandlerIdentity.createNew()
        override val allowedKinds: Set<UpdateKind<*>> = emptySet()

        override suspend fun handleUpdate(
            update: Update,
            bot: org.bezsahara.kittybot.bot.KittyBot,
            handlerContext: HandlerContext,
        ): Decision {
            error("Jump anchor should never execute directly")
        }
    }

    private object NoOpCustomClient : CustomClient {
        override suspend fun CoroutineScope.createMPRequest(
            urlAbs: String,
            contentType: String,
        ): CustomRequest {
            error("No multipart requests are expected in Furball tests")
        }

        override suspend fun sendJSONRequest(
            urlAbs: String,
            json: ByteArray,
            isGetUpdates: Boolean,
        ): CustomResponse {
            error("No API requests are expected in Furball tests")
        }

        override fun close() {}
    }

    private fun messageUpdate(): MessageUpdate {
        return MessageUpdate(
            updateId = 1L,
            message = Message(
                messageId = 10L,
                date = 0L,
                chat = Chat(id = 100L, type = "private"),
                from = User(id = 200L, isBot = false, firstName = "Test"),
                text = "hello",
            ),
        )
    }
}
