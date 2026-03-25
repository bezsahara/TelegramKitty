package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text

fun FelineDispatcher.flowExample() {
    // A flow groups several handlers into ordered sections.
    // Here we use the chat id, so each chat progresses through the flow independently.
    flowHandler({ update -> update.message?.chat?.id?.toString() }) {
        section {
            command("/wizard") {
                // pauseSection keeps the user in the current section and can store data for later steps.
                handlerContext.pauseSection("Started from /wizard")
                bot.sendMessage(chatId, "Wizard started. First step: send `hi`.")
            }

            text("hi") {
                val previousStepInfo = handlerContext.getFlowArgs() as? String ?: "Skipped the explicit start command"

                // nextSection moves this chat to the next flow section and can pass data forward.
                handlerContext.nextSection(previousStepInfo)
                bot.sendMessage(chatId, "Nice. Step 1 is done.\nSaved flow data: $previousStepInfo")
            }

            text {
                bot.sendMessage(chatId, "Send /wizard to start the flow, then answer with `hi`.")
            }
        }

        section {
            command("/reset") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "Flow reset. Send /wizard to start again.")
            }

            text("secret") {
                val previousStepInfo = handlerContext.getFlowArgs() as? String ?: "No data"

                // This demonstrates carrying some state through the flow.
                handlerContext.nextSection(previousStepInfo)
                bot.sendMessage(chatId, "Correct. You unlocked the last section.")
            }

            text {
                bot.sendMessage(chatId, "Step 2: send `secret`, or /reset to start over.")
            }
        }

        section {
            command("/reset") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "Flow reset. Send /wizard to start again.")
            }

            text {
                val previousStepInfo = handlerContext.getFlowArgs() as? String ?: "No data"
                bot.sendMessage(
                    chatId,
                    "You reached the end of the flow.\nSaved flow data: $previousStepInfo\nSend /reset to run it again."
                )
            }
        }
    }
}
