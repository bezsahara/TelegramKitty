@file:Suppress("NOTHING_TO_INLINE")

package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds

inline fun <T> MutableList<T>.replaceLast(block: (T) -> T): T {
    val lastOneNew = block(this[size-1])
    this[size-1] = lastOneNew
    return lastOneNew
}

inline fun String.withStartOf(prefix: String): Boolean {
    return (this as java.lang.String).startsWith(prefix, 0)
}

internal fun createBoolUpdateKindArray(): BooleanArray {
    return BooleanArray(telegramUpdateKinds.size)
}

internal inline fun <reified T> createArrayOfUKSize(): Array<T?> {
    return arrayOfNulls(telegramUpdateKinds.size)
}

internal fun createBoolUpdateKindArray(ofKinds: Collection<UpdateKind<*>>): BooleanArray {
    val array = createBoolUpdateKindArray()
    ofKinds.forEach { updateKind ->
        array[updateKind.ordinal] = true
    }
    return array
}