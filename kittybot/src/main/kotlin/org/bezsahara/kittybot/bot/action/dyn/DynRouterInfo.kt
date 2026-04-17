package org.bezsahara.kittybot.bot.action.dyn

import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.updates.IntIntHashMap

class DynRouterInfo(
    val jumpTo: HandlerIdentity,
    val initialOffset: Int,
    val adjust: Boolean,
    internal val handlers: Array<HandlerInfo>,
    internal val hiMap: IntIntHashMap,
)
