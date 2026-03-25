package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.route.routingInt
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.callbackQuery
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.handleTypeOf
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup
import java.util.concurrent.ConcurrentHashMap

fun FelineDispatcher.routingExample() {
    // This state decides which routing section a chat should be sent to.
    // ConcurrentHashMap keeps the sample safe if you switch to a multithreaded updater.
    val routeByChatId = ConcurrentHashMap<Long, Int>()

    command("/route") {
        bot.sendMessage(
            chatId,
            "Pick a route. After that, regular text messages in this chat will jump directly to the selected section.",
            replyMarkup = buildInlineKeyboardMarkup {
                callback("Route 1", "route:1")
                callback("Route 2", "route:2")
                callback("Route 3", "route:3")
            }
        )
    }

    // A special helper to catch one type of update
    handleTypeOf<MessageUpdate>({ it.message.text == "cookies" }) {
        bot.sendMessage(chatId, "Cookies were sent!")
    }

    command("/route_clear") {
        routeByChatId.remove(message.chat.id)
        bot.sendMessage(chatId, "Route cleared. Use /route to pick one again.")
    }

    callbackQuery({ it.data?.startsWith("route:") == true }) {
        val number = callbackQuery.data!!.substringAfter("route:").toIntOrNull()
        val originChatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        val originChat = originChatId.toChatId()

        if (number == null) {
            bot.answerCallbackQuery(callbackQuery.id, "Invalid route")
            return@callbackQuery
        }

        routeByChatId[originChatId] = number
        bot.answerCallbackQuery(callbackQuery.id, "Route $number selected")
        bot.sendMessage(originChat, "Saved route $number for this chat. Send /route_clear to remove it.")
    }

    // routingInt skips unrelated handlers once a key is known.
    // For dense integer keys it can become a very cheap array lookup.
    routingInt({ update, _ ->
        val chatId = update.asMessageUpdateOrNull()?.message?.chat?.id ?: return@routingInt Int.MIN_VALUE
        routeByChatId[chatId] ?: Int.MIN_VALUE
    }) {
        section(1) {
            text {
                bot.sendMessage(chatId, "You are on route 1.")
            }
        }
        section(2) {
            text {
                bot.sendMessage(chatId, "You are on route 2.")
            }
        }
        section(3) {
            text {
                bot.sendMessage(chatId, "You are on route 3.")
            }
        }
    }
}
