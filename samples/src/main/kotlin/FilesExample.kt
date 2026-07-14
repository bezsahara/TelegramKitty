package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.flow.FlowIdentityFinder
import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.downloadFileAsByteArrayById
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionTypeEmoji
import org.bezsahara.kittybot.telegram.client.file.TelegramFile
import org.bezsahara.kittybot.telegram.utils.unwrap
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.values.ReactionEmoji
import org.intellij.lang.annotations.Language

fun FelineDispatcher.filesExample() {
    flowHandler<Unit>(FlowIdentityFinder.OfMessageUpdateChatId) {
        section {
            command("/files") {
                handlerContext.nextSection()
                @Language("html")
                val msg = "Bot will now echo <b>images</b> you send it"

                bot.sendMessage(
                    chatId,
                    msg,
                    parseMode = ParseMode.HTML
                )
            }
        }

        section {
            command("/cancel") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "You canceled the flow")
            }

            command("/files2") {
                handlerContext.nextSection()
                bot.sendMessage(chatId, "Bot will now echo files you send it: version 2")
            }

            addHandler(setOf(MessageUpdate)) { update, bot, handlerContext ->
                val msg = (update as MessageUpdate).message
                val chatId = msg.chat.id.toChatId()
                val photos = msg.photo
                if (photos.isNullOrEmpty()) {
                    bot.sendMessage(update.chatIdOrNull(), "No photos available. To stop write /cancel")
                } else {
                    bot.setMessageReaction(chatId, msg.messageId, listOf(ReactionTypeEmoji(ReactionEmoji.FIRE)))
                        .consume()
                    val a = photos.maxBy { it.height }
                    bot.sendPhoto(chatId, TelegramFile.withId(a.fileId), caption = "Here is your photo. To stop write /cancel")
                }
                Decision.Consumed
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
                    val bytes = bot.downloadFileAsByteArrayById(a.fileId).unwrap()
                    bot.sendPhoto(update.chatIdOrNull(), TelegramFile.withBytes(bytes), caption = "Here is your photo. To stop write /cancel").unwrap()
                }
                Decision.Consumed
            }
        }
    }
}
