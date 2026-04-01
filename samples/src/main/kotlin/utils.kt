package org.bezsahara.samples

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.utils.setMyCommands

suspend fun KittyBot.createCommandsSimpleExample() {
    setMyCommands {
        allPrivateChats {
            command("/start", "Show the sample overview")
            command("/id", "Show the current chat id")
            command("/menu", "Inline keyboard example")
            command("/cat", "Send an http.cat image")
            command("/route", "Choose a routing target")
            command("/route_clear", "Clear the active route")
            command("/wizard", "Start the flow example")
            command("/reset", "Reset the active flow")
            command("/error", "Trigger the error handler sample")
            command("/auth", "Conversation example 1")
            command("/callback", "Conversation example 2")
        }

        val specialUsers = listOf<ChatId>()
        for (user in specialUsers) {
            chat(user) {
                command("/special", "A special command only for a specific chat")
            }
        }
    }
}