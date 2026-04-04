package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.updates.IntIntHashMap

class DynRouterInfo(
    val jumpTo: HandlerIdentity,
    val initialOffset: Int,
    internal val handlers: Array<HandlerInfo>,
    internal val hiMap: IntIntHashMap,
)
