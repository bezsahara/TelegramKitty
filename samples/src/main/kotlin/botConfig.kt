package org.bezsahara.samples

import org.bezsahara.kittybot.bot.*
import org.bezsahara.kittybot.bot.dispatchers.x.messageRateFilter
import org.bezsahara.kittybot.bot.dispatchers.y.*
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.stages.InMemoryStageManager
import org.bezsahara.kittybot.bot.stages.builder.startStageHandler
import org.bezsahara.kittybot.bot.stages.builder.thenStageHandler
import org.bezsahara.kittybot.bot.updates.FelineBuilder
import org.bezsahara.kittybot.telegram.classes.bots.commands.BotCommand
import org.bezsahara.kittybot.telegram.classes.chats.toChatId
import org.bezsahara.kittybot.telegram.classes.keyboards.inlineKeyboard
import org.bezsahara.kittybot.telegram.classes.keyboards.replyKeyboard
import org.bezsahara.kittybot.telegram.classes.messages.entities.EntityType
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.client.TelegramFile
import org.bezsahara.kittybot.telegram.utils.*

fun FelineBuilder<*>.buildBot() {
    // This function can be used to stop old updates from being processed again
    // if an application is stopped without a chance to invalidate its last updates.
    ensureOnlyNewUpdates()
    // or
    // ensureOnlyNewUpdatesWithFile(File("")) <--- this will be faster

    init {
        setMyCommands(
            listOf(
                BotCommand("/cat", "Sends a cat image"),
                BotCommand("/me", "Shows your current stage"),
                BotCommand("/stage", "Starts stage chain")
            )
        )
    }

    // Definition order matters; handlers will be tested against the update one after another
    // in the order they are defined in this block.
    // The same goes for filters.
    // Also, if a handler succeeds, then all handlers beneath it are not tested (does not apply to filters).
    dispatchers {
        setErrorHandler { e, bot, update ->
            println("Error occurred: ${e.message}")
        }
        // You need to use StageManager if you want to use stage handlers.
        useStageManager(InMemoryStageManager())
        // OR you can use a different implementation, but it will also save stages to a file.
        // useStageManager(PersistentStageManager(File("")))

        filters {
            // Message rate filter does not pass user's update to handlers
            // if it is less than N (here it is 800) milliseconds apart from the previous one.
            messageRateFilter(800)
        }

        handleTypeOf<MessageUpdate>({ it.message.text == "type" }) {
            bot.sendMessage(chatId, "Type is ${update::class.java.name}")
        }

        text("welcome") {
            val welcome = buildEntityString {
                addString("Hello, ")
                addString(message.chat.firstName ?: "noname", EntityType.BOLD)
            }

            bot.sendMessage(chatId, welcome)
            // OR you can send it manually.
            // bot.sendMessage(chatId, text = welcome.text, entities = welcome.entities)
        }

        text("/me") {
            bot.sendMessage(chatId, "Your current stage is ${stageManager.getStage(message.chat.id)}")
        }

        // Commands
        // You can also access command arguments that a user might send.
        // Arguments are considered anything after the command, for example, '/start argument'.
        command("/arg") {
            bot.sendMessage(chatId, "Args sent: $commandArgs")
        }
        // You can send:
        // `/cat` will return a picture.
        // `/cat says [text]` will return a cat that is talking.
        // `/cat text` ascii art.
        // `/cat http [code]` returns a cat picture for the http status code.
        command("/cat") {
            if (commandArgs == null) {
                bot.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO)
                bot.sendTheCatApi(chatId)
            } else {
                val commandArgs = commandArgs!!
                when {
                    commandArgs.startsWith("says ") -> {
                        bot.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO)
                        bot.sendCatPicture(chatId, commandArgs.substring(5))
                    }
                    commandArgs == "text" -> {
                        bot.sendTextCat(chatId)
                    }
                    commandArgs.split(" ").getOrNull(0) == "http" -> {
                        commandArgs.split(" ").let {
                            if (it.size > 1) {
                                bot.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO)
                                bot.sendHttpCat(chatId, it[1].toInt())
                            }
                        }
                    }
                    else -> bot.sendMessage(chatId, "Unknown command: $commandArgs")
                }
            }
        }

        callback({ it.data?.getOrNull(0) == 's' }) {
            val type = when (callbackQuery.data?.getOrNull(1)) {
                '1' -> "Top"
                '2' -> "Left"
                '3' -> "Right"
                '4' -> "Bottom"
                else -> return@callback
            }
            bot.answerCallbackQuery(callbackQuery.id, "Button pressed: $type")
        }

        startStageHandler({ it.text == "/stage" }) {
            // This message will be parsed as HTML.
            val msg = bot.sendMessage(
                chatId = message.chat.id.toChatId(),
                text = "<i>Send a number between 1 and 10</i>",
                replyMarkup = inlineKeyboard {
                    button("Top", "s1")
                    row {
                        button("Left", "s2")
                        button("Right", "s3")
                    }
                    button("Bottom", "s4")
                },
                parseMode = ParseMode.HTML
            )

            // unwrap throws an error if there was something wrong with sending the message
            // or returns the message itself.
            msg.unwrap().messageId.also {
                println("Message id is $it")
            }
        }.thenStageHandler {
            val number = message.text?.toLongOrNull()

            if (number == null) {
                bot.sendMessage(message.chat.id.toChatId(), "Not a number!")
                // This function tells the handler not to set the next stage for the user,
                // so the user will have the same stage
                // and this handler will react to their next message.
                return@thenStageHandler cannotProceed()
            }

            if (number !in 1..10) {
                bot.sendMessage(message.chat.id.toChatId(), "Number must be between 1 and 10")
                // Just to make sure, you do not need to return cannotProceed().
                // Just calling this function is enough.
                cannotProceed()
                return@thenStageHandler
            }

            bot.sendMessage(
                message.chat.id.toChatId(),
                "Number accepted. Continue?",
                replyMarkup = replyKeyboard(oneTimeKeyboard = true) {
                    row {
                        button("Yes")
                        button("No")
                    }
                }
            )
        }.thenStageHandler {
            val text = message.text

            if (text == null) {
                bot.sendMessage(message.chat.id.toChatId(), "Text not found. Cancelling this chain.")
                return@thenStageHandler exitTheChain()
            } else {
                // Adds an argument(s) to the next stage, and they can be accessed in the next handler.
                // Currently, it adds the text that the user sent.
                canProceed(listOf(text))
            }
            message.text?.let {
                if (it == "No") {
                    bot.sendMessage(chatId, "Stopped")
                    // Exits the chain.
                    exitTheChain()
                    return@thenStageHandler
                }
            }

            bot.sendMessage(
                message.chat.id.toChatId(),
                "Send an image and the bot will send it back. Or send /cancel to cancel."
            )
        }.thenStageHandler {
            val photo = message.photo?.last()
            if (message.text == "/cancel") {
                bot.sendMessage(chatId, "Cancelled")
                return@thenStageHandler exitTheChain()
            }
            if (photo == null) {
                bot.sendMessage(chatId, "Bot expects either a photo or /cancel. Neither was received.")
                return@thenStageHandler cannotProceed()
            }

            val arg = stageInfo.stageArgs.getOrNull(0) // Access to that argument.

            bot.sendPhoto(chatId, TelegramFile.Id(photo.fileId))

//            bot.downloadFileAsByteArray(
//                photo.fileId
//            ).onFailure {
//                bot.sendMessage(chatId, "Failed to download photo: $it")
//            }?.onSuccess {
//                bot.sendPhoto(chatId, it.toTelegramFile(imgName))
//            }
            exitTheChain()
        }

        // You can implement your own handlers (Handler interface) with the addHandler function.
    }
}
