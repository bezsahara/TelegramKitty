@file:Suppress("NOTHING_TO_INLINE")

package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.telegram.classes.message.MaybeInaccessibleMessage
import org.bezsahara.kittybot.telegram.classes.message.Message

inline fun MaybeInaccessibleMessage.asMessageOrNull(): Message? {
    return if (javaClass === Message::class.java) (this as Message) else null
}

inline fun <T> List<T>.forList(block: (T) -> Unit) {
    for (i in this.indices) {
        block(get(i))
    }
}