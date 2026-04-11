package org.bezsahara.kittybot.bot.action.mgroup

import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.updates.MultiIdentity


fun UpdaterMode.isSequential(): Boolean {
    return when (this) {
        is UpdaterMode.MultiThread if (multiIdentity == MultiIdentity.OfAnyUserChatIdentity || multiIdentity == MultiIdentity.OfMessageChatIdentity) -> true
        UpdaterMode.SingleThread -> true
        else -> false
    }
}

