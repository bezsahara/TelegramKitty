package org.bezsahara.samples

import org.bezsahara.kittybot.bot.action.flow.FlowSectionStore
import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

fun FelineDispatcher.flowExample() {


    command("/fa a il") {

    }
    // A flow groups several handlers into ordered sections.
    // Here we use the chat id, so each chat progresses through the flow independently.
    flowHandler({ update, _ -> update.message?.chat?.id?.toString() }) {
        // Common - is a builder for a common handler that will run anywhere.
        // It has access only to resetFlow. If you use resetFlow you also need to consume the update!
        common(setOf(MessageUpdate)) {
            handler@{ update, bot, handlerContext ->
                if (update.message!!.text == "/reset") {
                    handlerContext.resetFlow()
                    bot.sendMessage(update.chatIdOrNull()!!, "Flow reset. Send /wizard to start again.")
                    return@handler Decision.Consumed
                }
                Decision.Next
            }
        }

        // OR if you do not want to use common, another option is just to create a function with common handlers
        // and reuse it in all sections
        fun FlowSectionStore.commons() {
            command("/reset_v2") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "Flow reset. Send /wizard to start again.")
            }
        }

        section {
            command("/wizard") {
                // pauseSection keeps the user in the current section and can store data for later steps.
                handlerContext.pauseSection("Started from /wizard")
                bot.sendMessage(chatId, "Wizard started. First step: send `step 1`.")
            }

            text("step 1") {
                val previousStepInfo = handlerContext.getFlowArgs() as? String ?: "Skipped the explicit start command"

                // nextSection moves this chat to the next flow section and can pass data forward.
                handlerContext.nextSection(previousStepInfo)
                bot.sendMessage(chatId, "Nice. Step 1 is done.\nSaved flow data: $previousStepInfo")
            }

            text("step end") {
                // You can also jump to sections by their name
                handlerContext.nextSectionWithName("the end")
                // Or directly by the id
                // handlerContext.nextSectionWithId(2)
                bot.sendMessage(chatId, "Going to the end!")
            }

            text {
                bot.sendMessage(chatId, "Send /wizard to start the flow, then answer with `hi`.")
            }
        }

        section {
            commons()

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

        section("the end") {
            commons()

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
