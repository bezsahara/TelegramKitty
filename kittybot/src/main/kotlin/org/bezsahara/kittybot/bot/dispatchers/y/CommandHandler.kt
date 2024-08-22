package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.CommandScope
import org.bezsahara.kittybot.telegram.classes.updates.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.updates.Update


class CommandHandler(
    private val command: String,
    val onSuccess: suspend CommandScope.() -> Unit
) : Handler {

    init {
        require(commandRegex.matches(command)) {
            """Command "$command" does not match the pattern.
                |Commands must always start with the / symbol and contain up to 32 characters. 
                |They can use Latin letters, numbers and underscores, 
                |though simple lowercase text is recommended for a cleaner look.""".trimMargin()
        }
    }

    private val spaceIndex = command.length

    override fun checkUpdate(update: Update): Boolean {
        val text = update.message?.text ?: return false
        return if (text.startsWith(command)) {
            if (text.length == command.length) {
                true
            } else {
                text[spaceIndex] == ' '
            }
        } else {
            false
        }
    }

    override suspend fun handleUpdate(update: Update, bot: KittyBot) {
        update as MessageUpdate
        CommandScope(
            bot,
            update.message, // /12345 sdf
            update.message.text?.let {
                if (command.length == it.length) {
                    null
                } else {
                    it.substring(spaceIndex + 1, it.length).ifBlank { null }
                }
            }
        ).onSuccess()
    }

    internal companion object {
        internal val commandRegex = Regex("^/[a-zA-Z0-9_]{1,32}\$")
    }
}

fun FelineDispatcher.command(
    command: String,
    onSuccess: suspend CommandScope.() -> Unit
) {
    addHandler(
        CommandHandler(command, onSuccess)
    )
}