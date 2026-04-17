package org.bezsahara.kittybot.bot.action.comms

import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

interface CommandGroup {
    open val canDefault: Boolean get() = false

    fun catchDefault(name: String, args: Array<Any?>): ExecBlock {
        error("Not implemented")
    }
}


fun interface ExecBlock {
    suspend fun HandlerScope<MessageUpdate>.execute()
}

/**
 * Marks a [CommandGroup] method as invokable from a parsed telegram command.
 *
 * The command name is the Kotlin method name. Annotated methods must return [ExecBlock].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BotCommand(val description: String = "No description")

