package org.bezsahara.kittybot.bot.dispatchers.y

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.GroupedMediaUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.SyntheticUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.message.Message

class MediaGroupScope(
    override val bot: KittyBot,
    override val handlerContext: HandlerContext,
    override val update: GroupedMediaUpdate
) : HandlerScope<GroupedMediaUpdate> {
    val chatId get() = update.firstMessage.chat.id.toChatId()
}


fun HandlerStore.mediaGroup(
    check: (List<Message>) -> Boolean,
    block: suspend MediaGroupScope.() -> Unit
) {
    addHandler(object : Handler {
        override val allowedKinds: Set<UpdateKind<*>>
            get() = setOf(SyntheticUpdate)

        override suspend fun handleUpdate(update: Update, bot: KittyBot, handlerContext: HandlerContext): Decision {
            val mediaUpdate = (update as? GroupedMediaUpdate) ?: return Decision.Next
            if (!check(mediaUpdate.mediaMessagesGrouped)) return Decision.Next

            MediaGroupScope(bot, handlerContext, update).block()

            return Decision.Consumed
        }
    })
}