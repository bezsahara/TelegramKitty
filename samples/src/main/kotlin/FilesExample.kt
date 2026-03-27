package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.each.testEach
import org.bezsahara.kittybot.bot.action.flow.FlowIdentityFinder
import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.action.other.contextHook
import org.bezsahara.kittybot.bot.action.other.debugHook
import org.bezsahara.kittybot.bot.action.route.routingInt
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.client.file.TelegramFile

fun FelineDispatcher.filesExample() {
    flowHandler(FlowIdentityFinder.OfMessageUpdateChatId) {
        section {
            command("/files", "Files example", addToBotCommands = true) {
                handlerContext.nextSection()
                bot.sendMessage(chatId, "Bot will now echo images you send it")
            }
        }
        section {
            command("/cancel") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "You canceled the flow")
            }

            addHandler(setOf(MessageUpdate)) { update, bot, handlerContext ->
                val msg = (update as MessageUpdate).message
                val photos = msg.photo
                if (photos.isNullOrEmpty()) {
                    bot.sendMessage(update.chatIdOrNull(), "No photos available. To stop write /cancel")
                } else {
                    val a = photos.maxBy { it.height }
                    bot.sendPhoto(update.chatIdOrNull(), TelegramFile.withId(a.fileId), caption = "Here is your photo. To stop write /cancel")
                }
                Decision.Consumed
            }
        }
    }
}
