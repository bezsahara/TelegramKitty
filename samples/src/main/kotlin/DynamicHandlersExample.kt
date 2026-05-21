package org.bezsahara.samples

import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.bezsahara.kittybot.bot.action.dyn.collectAndAdd
import org.bezsahara.kittybot.bot.action.dyn.createDynamicHandlerStore
import org.bezsahara.kittybot.bot.action.route.textMapHandler
import org.bezsahara.kittybot.bot.conv.buildConversation
import org.bezsahara.kittybot.bot.conv.cts.receiveCommand
import org.bezsahara.kittybot.bot.conv.cts.receiveMessage
import org.bezsahara.kittybot.bot.conv.scope.onText
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChat
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import org.bezsahara.kittybot.telegram.utils.setMyCommands
import org.bezsahara.kittybot.telegram.utils.unwrap

fun FelineDispatcher.dynamicHandlersExample() {
    var hands: List<Handler>? = null
    val register = createDynamicHandlerStore {
        hands = collectAndAdd {
            textMapHandler {
                text("Hello World") {
                    bot.sendMessage(chatId, "Hello World!")
                }
                text("Bye Byeee") {
                    bot.sendMessage(chatId, "Bye!")
                }
            }
        }
    }

    val handlerToAdd = Handler { update, bot, context ->
        if (update.asMessageUpdateOrNull()?.message?.text != "added") {
            return@Handler Decision.Next
        }
        bot.sendMessage(update.chatIdOrNull(), "Reply")
        Decision.Consumed
    }

    buildConversation {
        onText("/dynamic") {
            var added: Handler? = null

            launch {
                while (isActive) {
                    receiveCommand("/add").await()
                    if (added != null) {
                        bot.sendMessage(chatId, "You have added the command already")
                        continue
                    }
                    added = handlerToAdd
                    register.addHandler(added!!)
                    bot.sendMessage(chatId, "Added")
                }
            }

            launch {
                while (isActive) {
                    receiveCommand("/remove_map").await()
                    hands?.let {
                        register.removeHandlers(it)
                        bot.sendMessage(chatId, "Removed groups of handlers")
                        return@launch
                    }
                }
            }

            launch {
                while (isActive) {
                    receiveCommand("/remove").await()
                    if (added != null) {
                        register.removeHandler(added!!)
                        added = null
                        bot.sendMessage(chatId, "Handler removed")
                    } else {
                        bot.sendMessage(chatId, "There is no handler to remove")
                    }
                }
            }

            val previousCommands = bot.getMyCommands().unwrap()
            bot.setMyCommands {
                chat(chatId) {
                    command("/add", "Add dynamic handler")
                    command("/remove", "Remove dynamic handler")
                    command("/cancel", "Cancel this conversation")
                }
            }

            bot.sendMessage(
                chatId,
                "Send /add to add dynamic handler or /remove to remove it." +
                        "Also /remove_map to remove several handlers. Or /cancel to cancel this conversation."
            ).consume()

            receiveMessage { it.text == "/cancel" }.await()
            bot.deleteMyCommands(BotCommandScopeChat(chatId))
            bot.setMyCommands(previousCommands)
        }
    }
}