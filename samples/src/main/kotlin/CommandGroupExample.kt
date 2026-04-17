package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.comms.BotCommand
import org.bezsahara.kittybot.bot.action.comms.CommandGroup
import org.bezsahara.kittybot.bot.action.comms.ExecBlock
import org.bezsahara.kittybot.bot.action.comms.commandGroupHandler
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId

fun HandlerStore.commandGroupExample() {
    commandGroupHandler(CommandGroupExample())
}


// You can call them like normal commands:
// /example_group
// With arguments:
// /example_group argument1 20300 "arg with space"
// Only Double, Long, String, or List of them are allowed for types
class CommandGroupExample : CommandGroup {
    @BotCommand
    fun example_group() = ExecBlock {
        bot.sendMessage(chatId, "Hello world!")
    }

    @BotCommand
    fun example_group(arg: String, num: Long, str: String) = ExecBlock {
        bot.sendMessage(chatId, "Hello world! Arg: $arg, Num: $num, Str: $str")
    }

//    override val canDefault: Boolean
//        get() = true

//    Default will be called if name was found but descriptor wasn't
//    override fun catchDefault(
//        name: String,
//        args: Array<Any?>,
//    ): ExecBlock = {
//        bot.sendMessage(chatId, "! Arg: $args")
//    }
}