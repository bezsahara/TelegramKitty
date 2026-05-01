package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.flow.stateHandler
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import java.util.concurrent.ConcurrentHashMap


sealed interface State {
    object None : State

    class Active(val name: String) : State

    sealed interface Exit : State {
        object Failure : Exit
        object Success : Exit
    }

    class Waiting(val exit: Exit) : State
}

fun TransparentHandlerStore.stateHandlerExample() {
    val map = ConcurrentHashMap<Long, State>()

    // State is tested from top to bottom
    // in case no filter can catch the state the default will be used
    // if no default is defined, nothing will be used and next handlers if any will be tested for the update
    stateHandler({ upd, _ ->
        val msg = upd.message ?: return@stateHandler null
        map.getOrPut(msg.chat.id) { State.None }
    }) {
        // valueOf compares objects using `equals`
        valueOf(State.None) {
            text("/state_test") {
                map[chatId.value.toLong()] = State.Active(message.chat.firstName ?: "No name")
                bot.sendMessage(chatId, "You are registered! Send /what to see your account. /logout to remove your account")
            }
        }
        // instanceOf<State.Active>
        // or classOf can be used:
        // classOf is faster for concrete types
        classOf<State.Active> {
            text("/what") {
                // You can retrieve state from your own data structure
                // val name = (map[message.chat.id]!! as State.Active).name
                // Or with a helper
                val name = handlerContext.state().name
                bot.sendMessage(chatId, "You have been registered. Your name is $name")
            }
            text("/logout") {
                map[message.chat.id] = State.Exit.Success
                bot.sendMessage(chatId, "You have been logged out")
            }
            text("/force_logout") {
                map[message.chat.id] = State.Exit.Failure
                bot.sendMessage(chatId, "You have been logged out with force")
            }
        }
        // instanceOf is the same as `is` operator in kotlin
        instanceOf<State.Exit> {
            text("/reset") {
                map[message.chat.id] = State.None
                bot.sendMessage(chatId, "You have been reset. Send /state_test to try again")
            }
            text {
                val reason = when (handlerContext.state()) {
                    State.Exit.Failure -> "Your decision with force"
                    State.Exit.Success -> "Your decision"
                }
                bot.sendMessage(chatId, "You have been logged out! Because $reason  Send /reset to reset")
            }
        }

        // Check can define custom lambda for how to check state
        check({
            print("WHAT?222")
            (it as? State.Waiting)?.exit == State.Exit.Failure
        }) {
            text {
                bot.sendMessage(chatId, "You are waiting for exit")
            }
        }

        // Default is equivalent to kotlin's `else` in `when`
        default {
            text {
                bot.sendMessage(chatId, "Unknown type ${handlerContext.state().javaClass.canonicalName}")
            }
        }
    }
}