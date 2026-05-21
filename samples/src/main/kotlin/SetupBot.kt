package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.each.testEach
import org.bezsahara.kittybot.bot.action.other.contextHook
import org.bezsahara.kittybot.bot.action.other.contextValue
import org.bezsahara.kittybot.bot.action.other.handleUnknownUpdate
import org.bezsahara.kittybot.bot.builder.FelineBuilder
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.attrKeyOf
import org.bezsahara.kittybot.bot.dispatchers.y.callbackQuery
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.bot.sendHttpCat
import org.bezsahara.kittybot.bot.updates.updaters.MultiIdentity
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChat
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeDefault
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import org.bezsahara.kittybot.telegram.classes.keyboard.CopyTextButton
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardButton
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.values.KeyboardButtonStyle

fun FelineBuilder<*>.buildBot() {
    // Prevent Telegram from replaying stale updates after a restart.
    ensureOnlyNewUpdates()

    // Register visible bot commands once before update handling starts.
    init {
        // An example of using helper function to create bot commands
        deleteMyCommands(BotCommandScopeDefault)
        if (false) {
            createCommandsSimpleExample()
            return@init
        }

        setMyCommands(
            listOf(
                BotCommand("/start", "Show the sample overview"),
                BotCommand("/id", "Show the current chat id"),
                BotCommand("/menu", "Inline keyboard example"),
                BotCommand("/cat", "Send an http.cat image"),
                BotCommand("/route", "Choose a routing target"),
                BotCommand("/route_clear", "Clear the active route"),
                BotCommand("/wizard", "Start the flow example"),
                BotCommand("/reset", "Reset the active flow"),
                BotCommand("/error", "Trigger the error handler sample"),
                BotCommand("/auth", "Conversation example 1"),
                BotCommand("/callback", "Conversation example 2"),
            ),
            scope = BotCommandScopeDefault
        ).consume()
    }

    // Restrict updates to what this sample actually handles.
    allowUpdatesOf(CallbackQueryUpdate, MessageUpdate)

    updaterMode = UpdaterMode.MultiThread(MultiIdentity.OfAnyUserChatIdentity, 8)

    // Centralized error handling for all sample handlers.
    setErrorHandler { throwable, bot, update, _, _ ->
        update.asMessageUpdateOrNull()?.chatIdOrNull()?.let { chatId ->
            val errorText = throwable.message ?: throwable.javaClass.simpleName
            bot.sendMessage(chatId, "Handler failed: $errorText")
        }
        throwable.printStackTrace()
        Decision.Consumed
    }

    // Handler are tested in order of definition.
    // If one consumes the update, handlers after it are not tested!
    // This behavior can be changed with testEach for example
    dispatchers {
        // A normal command handler.
        command("/start") {
            bot.sendMessage(
                chatId,
                """
                This sample bot shows several TelegramKitty features.
                Try:
                /id - basic command handler
                /menu - inline keyboard + callback query
                /cat - calling an API helper
                /route - routing example
                /wizard - flow example
                /files - files example
                /httpcat [number] - low level addHandler example
                /error - error handler demo
                """.trimIndent()
            )
        }

        command("/update_cmd") {
            bot.deleteMyCommands(BotCommandScopeChat(chatId))
            bot.createCommandsSimpleExample(chatId)
            bot.sendMessage(chatId, "Updated")
        }

        stateHandlerExample()
        commandGroupExample()
        mediaGroupExample()
        conversations()

        // You can create attr key like this:
        val attrKey = attrKeyOf<String>("TestAttribute")
        // Or easier way:
        val contextValue = contextValue { mutableListOf<Int>() }
        // Or without initial value
        // val contextValue = contextValue<MutableList<Int>>()

        val getContextValue = contextValue.getter()


        // HandlerContext is not thread safe.
        // BUT, it is guaranteed that at most one handler will access it
        // You can change context as well to later access it in handlers
        contextHook { update, handlerContext ->
            handlerContext[attrKey] = "Current update class is: ${update.javaClass.simpleName}"
            handlerContext.getContextValue()
                .add(12)
        }

        command("/id") {
            bot.sendMessage(chatId, "Current chat id: ${message.chat.id}")
        }

        command("/echo", "Echo what you put after the command", addToBotCommands = true) {
            bot.sendMessage(chatId, "You said: ${commandArgs ?: "Nothing"}").consume()
        }

        // A regular text handler.
        text("hi") {
            bot.sendMessage(chatId, "Hi back. Try /menu, /route or /wizard next.")
        }

        text("bye") {
            bot.sendMessage(chatId, "Bye.")
        }

        // Inline keyboard builder with several button types.
        command("/menu") {
            bot.sendMessage(chatId, "Inline keyboard example", replyMarkup = buildInlineKeyboardMarkup {
                url("Open Google", "https://google.com")
                addRow(3) {
                    callback("Ping callback", "sample:pop", style = KeyboardButtonStyle.DANGER)
                    addButton(InlineKeyboardButton("CopyThis!", copyText = CopyTextButton("copied text")))
                    copyText("Copy via DSL", "Copied from TelegramKitty")
                }
            })
        }

        callbackQuery({ it.data == "sample:pop" }) {
            val targetChatId = callbackQuery.message?.chat?.id?.toChatId()!!

            bot.answerCallbackQuery(callbackQuery.id, "Callback received")
            bot.sendMessage(targetChatId, "The callback query handler worked.")
        }

        // A helper from the Vert.x client module.
        command("/cat") {
            val code = commandArgs?.toIntOrNull() ?: 404
            bot.sendHttpCat(chatId, code).consume()
        }

        // Intentionally fail so the custom error handler can be observed.
        command("/error") {
            error("Sample error thrown from /error")
        }

        // testEach keeps evaluating handlers in the block even after one consumes the update.
        testEach {
            text("cars") {
                bot.sendMessage(chatId, "First handler inside testEach matched exactly `cars`.")
            }

            text({ it.startsWith("cars") }) {
                bot.sendMessage(chatId, "Second handler inside testEach matched a string starting with `cars`.")
            }
        }

        // Low level access if you want to bypass the higher-level DSL.
        addHandler(setOf(MessageUpdate)) { update, bot, _ ->
            val text = (update as MessageUpdate).message.text ?: return@addHandler Decision.Next
            if (!text.startsWith("/httpcat ")) return@addHandler Decision.Next

            val httpCode = text.substringAfter("/httpcat ").trim().toIntOrNull()
            val targetChatId = update.chatIdOrNull() ?: return@addHandler Decision.Next

            if (httpCode == null) {
                bot.sendMessage(targetChatId, "Usage: /httpcat <status-code>")
                return@addHandler Decision.Consumed
            }

            bot.sendHttpCat(targetChatId, httpCode).consume()
            Decision.Consumed
        }

        // More advanced dispatcher helpers live in their own sample files.
        dynamicHandlersExample()
        routingExample()
        filesExample()
        flowExample()

        handleUnknownUpdate {
            println("Unknown update type was noticed!")
            println(update.asJsonString())
        }
    }
}
