package org.bezsahara.kittybot.bot.dispatchers.y.scopes

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.updates.Update

class LambdaScope(
    val bot: KittyBot,
    val update: Update
)