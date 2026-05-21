package org.bezsahara.kittybot.bot.action.comms

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.action.flow.FlowIdentityStorage
import org.bezsahara.kittybot.bot.action.flow.FlowIdentityStorageInMem
import org.bezsahara.kittybot.bot.action.flow.FlowManager
import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import org.bezsahara.kittybot.telegram.classes.message.Message

/**
 * Builds a [CommandGroupStore] for [commandGroup], registers a [CommandGroupHandler] in this
 * [HandlerStore], and returns the created store.
 *
 * Registered commands are matched against slash-prefixed message text such as `/help` or
 * `/ban "user"`.
 */
fun HandlerStore.commandGroupHandler(commandGroup: CommandGroup): CommandGroupStore {
    val commandGroupStore = CommandGroupStore.create(commandGroup)
    addHandler(CommandGroupHandler(commandGroupStore, null) { _, _ -> "" })
    return commandGroupStore
}

/**
 * Version of [commandGroupHandler] where a command can be sent first and arguments after it.
 *
 * For example, you have a command like `/hello "name"`
 * In this version, you can either send `/hello "name"` in one line
 * OR
 * you can first send `/hello` then bot will reply [firstStepReply] and expect arguments.
 */
fun TransparentHandlerStore.twoStepCommandGroupHandler(
    commandGroup: CommandGroup,
    flowIdentityStorage: FlowIdentityStorage<String> = FlowIdentityStorageInMem(),
    firstStepReply: (Message, HandlerContext) -> String = { _, _ -> "Send arguments!" },
) {
    val commandGroupStore = CommandGroupStore.create(commandGroup)
    flowHandler(
        { u, _ -> u.asMessageUpdateOrNull()?.message?.chat?.id?.toString() },
        setOf(MessageUpdate),
        flowIdentityStorage
    ) {
        section {
            addHandler(CommandGroupHandler(commandGroupStore, flowManager, firstStepReply))
        }
        section {
            addHandler(CommandGroupHandler.SecondStep(flowManager, commandGroupStore))
        }
    }
}

/**
 * Dispatches slash commands from [MessageUpdate]s to executables resolved from a
 * [CommandGroupStore].
 *
 * The handler ignores non-message updates, messages without text, and messages whose command name
 * or parsed argument signature do not match anything in [commandGroupStore].
 */
internal class CommandGroupHandler(
    private val commandGroupStore: CommandGroupStore,
    private val flowManager: FlowManager<String>?,
    private val firstStepReply: (Message, HandlerContext) -> String,
) : Handler {
    override val allowedKinds: Set<UpdKind>
        get() = setOf(MessageUpdate)

    internal class Scope(
        override val bot: KittyBot,
        override val update: MessageUpdate,
        override val handlerContext: HandlerContext,
    ) : HandlerScope<MessageUpdate>

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val message = (update as MessageUpdate).message
        val text = message.text ?: message.caption ?: return Decision.Next

        if (text.length > 0 && text[0] == '/') {
            val endIndex = text.indexOfSpace()
            val comm = if (endIndex == -1) text.substring(1) else text.substring(1, endIndex)
            val paramsList = commandGroupStore.executableMap[comm] ?: return Decision.Next

            val mdArgsDesc: List<BotType>
            val mdArgs: Array<Any?>

            if (endIndex == -1) {
                mdArgsDesc = emptyList()
                mdArgs = emptyArray()
            } else {
                val parsed = ParsedBotCall.parse(text.substring(1))
                if (parsed !is ParsedBotCall) throw errorHappened("Parsing failed", parsed as ParsedBotError)
                mdArgsDesc = parsed.method.args
                mdArgs = parsed.arguments
            }

            val mh = paramsList[mdArgsDesc]

            if (flowManager != null && mh == null && endIndex == -1) {
                // only command was sent and it was not found without arguments
                bot.sendMessage(message.chat.id.toChatId(), firstStepReply(message, handlerContext))
                    .consume()
                flowManager.nextSection(handlerContext, args = comm)
                return Decision.Consumed
            }

            val exe = commandGroupStore.getExecOrDefOrNull(mh, comm, mdArgs)
                ?: throw errorHappened("Failure to find handle for $comm${mdArgsDesc.toDescString()}! And not default was defined!")

            val scope = Scope(bot, update, handlerContext)
            exe.apply {
                scope.execute()
            }
            return Decision.Consumed
        }

        return Decision.Next
    }

    private fun String.indexOfSpace(): Int {
        return (this as java.lang.String).indexOf(' '.code, 0)
    }

    internal class SecondStep(
        val flowManager: FlowManager<String>,
        private val commandGroupStore: CommandGroupStore,
    ) : Handler {
        override val allowedKinds: Set<UpdKind>
            get() = setOf(MessageUpdate)

        override suspend fun handleUpdate(
            update: Update,
            bot: KittyBot,
            handlerContext: HandlerContext,
        ): Decision {
            val message = (update as MessageUpdate).message
            flowManager.resetFlow(handlerContext)

            val scope = Scope(bot, update, handlerContext)
            val flowData = flowManager.getFlowData(handlerContext)
            val name = flowData.args as? String
                ?: throw KittyError("No flow data found for 2 stage in CommandGroupHandler")

            val map = commandGroupStore.executableMap[name]
                ?: throw errorHappened("No group data found for $name in CommandGroupHandler")

            val text = message.text ?: message.caption
            ?: throw errorHappened("No arguments were provided for command named $name")

            val parsed = ParsedBotCall.parseArgsOnly(name, text)

            if (parsed is ParsedBotError) {
                throw errorHappened("Failure to pass command!", parsed)
            }
            parsed as ParsedBotCall

            val mh = map[parsed.method.args]

            val exec = commandGroupStore.getExecOrDefOrNull(mh, name, parsed.arguments)
                ?: throw errorHappened("Failure to find handle for ${parsed.method.toDescString()}! And not default was defined!")

            exec.apply {
                scope.execute()
            }

            return Decision.Consumed
        }

        private fun errorHappened(message: String?, cause: Throwable? = null): CommandGroupHandlerException {
            return CommandGroupHandlerException(message, commandGroupStore, cause)
        }
    }

    private fun errorHappened(message: String?, cause: Throwable? = null): CommandGroupHandlerException {
        return CommandGroupHandlerException(message, commandGroupStore, cause)
    }
}

class CommandGroupHandlerException(
    message: String?,
    val commandGroupStore: CommandGroupStore,
    override val cause: Throwable? = null,
) : Exception(message)





