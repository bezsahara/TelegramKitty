package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.dispatchers.Handler

internal fun List<Handler>.checkIfIdentityDuplicated(): Pair<Handler, Handler>? {
    val map = IntIntHashMap(missingValue = -1)
    forEachIndexed { index, handler ->
        handler.identity?.let { identity ->
            val k = map.putIfAbsent(identity.value, index)
            if (k != -1) {
                return get(k) to handler
            }
        }
    }
    return null
}