package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.other.withStartOf
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.botCommandsKey
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.CommandScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.other.HandlerNS
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import kotlin.coroutines.Continuation


class CommandHandler(
    private val command: String,
    onSuccess: suspend CommandScope.() -> Unit
) : HandlerNS<CommandScope>(onSuccess as ((CommandScope, Continuation<Any?>) -> Any?)?) {
    init {
        require(commandRegex.matches(command)) {
            """Command "$command" does not match the pattern.
                |Commands must always start with the / symbol and contain up to 32 characters.
                |They can use Latin letters, numbers and underscores,
                |though simple lowercase text is recommended for a cleaner look.
                |For further info check: https://core.telegram.org/bots/features#commands""".trimMargin()
        }
    }

    private val commandLength = command.length

    override val allowedKinds: Set<UpdKind> get() = setOf(MessageUpdate)

    override fun allowUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): CommandScope? {
        val text = (update as MessageUpdate).message.text ?: return null

        return if (text.withStartOf(command)) {
            val parsedArgs = if (commandLength == text.length) {
                null
            } else {
                if (text[commandLength] != ' ') return null
                text.substring(commandLength + 1, text.length)
            }
            CommandScope(
                bot,
                update,
                parsedArgs,
                handlerContext
            )
        } else null
    }

    override fun toString(): String {
        return "CommandHandler($command)"
    }

    internal companion object {
        internal val commandRegex = Regex("^/[a-zA-Z0-9_]{1,32}\$")
    }
}


fun HandlerStore.command(
    command: String,
    onSuccess: suspend CommandScope.() -> Unit
) {
    addHandler(
        CommandHandler(command, onSuccess)
    )
}

// If you specify description and set addToBotCommands to true
// Bot will automatically add it to your bot commands via setMyCommands
@Deprecated(message = "Automatic addition to bot commands is now deprecated.")
fun HandlerStore.command(
    command: String,
    description: String? = null,
    addToBotCommands: Boolean = false,
    onSuccess: suspend CommandScope.() -> Unit
) {
    addHandler(
        CommandHandler(command, onSuccess)
    )
    if (addToBotCommands && (description != null && description.length > 1)) {
        felineDispatcher.felineBuilder.botContext.getOrPut(botCommandsKey) { mutableListOf() }
            .add(BotCommand(command, description))
    }
}