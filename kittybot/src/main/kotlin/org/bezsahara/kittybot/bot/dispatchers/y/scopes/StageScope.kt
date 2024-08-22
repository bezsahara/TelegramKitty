package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.stages.StageInfo
import org.bezsahara.kittybot.bot.stages.StageManager
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message

class StageScope(
    message: Message,
    bot: KittyBot,
    val stageInfo: StageInfo,
    val stageManager: StageManager
) : MessageScope(message, bot)