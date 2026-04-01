package org.bezsahara.samples

import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.bezsahara.kittybot.bot.conv.buildConversation
import org.bezsahara.kittybot.bot.conv.cts.receiveCallbackData
import org.bezsahara.kittybot.bot.conv.cts.receiveCallbackQuery
import org.bezsahara.kittybot.bot.conv.cts.receiveMessage
import org.bezsahara.kittybot.bot.conv.cts.receivePhotos
import org.bezsahara.kittybot.bot.conv.cts.receiveText
import org.bezsahara.kittybot.bot.conv.scope.onStartCommand
import org.bezsahara.kittybot.bot.conv.scope.onText
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup

fun FelineDispatcher.conversations() {

    buildConversation {
        onStartCommand {
            bot.sendMessage(chatId, "Hi! What's your name?")
            val name = receiveText().await()
            bot.sendMessage(chatId, "Send a picture, $name")
            val picture = receivePhotos().await()
        }
    }

    buildConversation {
        onText({ it.text == "/auth" }) {
            bot.sendMessage(chatId, "Hi, auth began. Send your pic")

            launch {
                receiveMessage { it.text == "/cancel" }.await()

                endConversation()
            }

            var pics: List<PhotoSize>? = null

            val gettingPics = launch {
                while (pics == null) {
                    val nextPics = withTimeoutOrNull(5000) {
                        receivePhotos().await()
                    }

                    if (nextPics == null) {
                        bot.sendMessage(
                            chatId,
                            "There is no pics still! Reminder to send them! You can send /stop to stop reminders"
                        )
                    } else {
                        pics = nextPics
                    }
                }
            }

            val stopListener = launch {
                receiveMessage { it.text == "/stop" }.await()
                bot.sendMessage(chatId, "stopped! tho pic is expected still! to cancel send /cancel")
                gettingPics.cancel()
            }

            gettingPics.invokeOnCompletion { stopListener.cancel() }
            gettingPics.join()

            if (gettingPics.isCancelled) {
                pics = receivePhotos().await()
            }

            val fp = pics ?: error("No pics found!")

            bot.sendMessage(chatId, "Pictures received!")
            bot.sendPhoto(chatId, TelegramFile.withId(fp.first().fileId))
        }

        onText("/callback") {
            bot.sendMessage(chatId, "Click on buttons! Write /cancel to cancel", replyMarkup = buildInlineKeyboardMarkup {
                callback("one", ":1")
                callback("two", ":2")
                callback("three", ":3")
            })

            launch {
                receiveMessage { it.text == "/cancel" }.await()
                bot.sendMessage(chatId, "stopped!")
                endConversation()
            }

            while (isActive) {
                val cq = receiveCallbackQuery { it.data?.startsWith(":") == true }.await()
                val cd = cq.data!![1]
                val text = when (cd) {
                    '1' -> "You clicked button one"
                    '2' -> "You clicked button two"
                    '3' -> "You clicked button three"
                    else -> "error"
                }
                bot.answerCallbackQuery(cq.id)
                bot.sendMessage(chatId, text)
            }
        }
    }
}