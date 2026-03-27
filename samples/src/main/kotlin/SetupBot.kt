package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.each.testEach
import org.bezsahara.kittybot.bot.action.other.contextHook
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
import org.bezsahara.kittybot.bot.updates.MultiIdentity
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import org.bezsahara.kittybot.telegram.classes.keyboard.CopyTextButton
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardButton
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup

fun FelineBuilder<*>.buildBot() {
    // Prevent Telegram from replaying stale updates after a restart.
    ensureOnlyNewUpdates()

    // Register visible bot commands once before update handling starts.
    init {
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
                BotCommand("/error", "Trigger the error handler sample")
            )
        ).consume()
    }

    // Restrict updates to what this sample actually handles.
    allowUpdatesOf(CallbackQueryUpdate, MessageUpdate)

    updaterMode = UpdaterMode.MultiThread(MultiIdentity.OfUserChatIdentity, 8)

    // Centralized error handling for all sample handlers.
    setErrorHandler { throwable, bot, update, _, _ ->
        update.asMessageUpdateOrNull()?.chatIdOrNull()?.let { chatId ->
            val errorText = throwable.message ?: throwable.javaClass.simpleName
            bot.sendMessage(chatId, "Handler failed: $errorText")
        }
        throwable.printStackTrace()
        Decision.Consumed
    }

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

        val attrKey = attrKeyOf<String>("TestAttribute")

        // You can change context as well to later access it in handlers
        contextHook { update, handlerContext ->
            handlerContext[attrKey] = "Current update class is: ${update.javaClass.simpleName}"
        }

        command("/id") {
            bot.sendMessage(chatId, "Current chat id: ${message.chat.id}")
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
                    callback("Ping callback", "sample:pop")
                    addButton(InlineKeyboardButton.CopyText("CopyThis!", CopyTextButton("copied text")))
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
            text("s") {
                bot.sendMessage(chatId, "First handler inside testEach matched exactly `s`.")
            }

            text({ it.startsWith("s") }) {
                bot.sendMessage(chatId, "Second handler inside testEach matched a string starting with `s`.")
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
        routingExample()
        filesExample()
        flowExample()
    }
}
