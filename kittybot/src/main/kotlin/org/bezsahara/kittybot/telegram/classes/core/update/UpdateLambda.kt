package org.bezsahara.kittybot.telegram.classes.core.update

internal fun interface UpdateLambda {
    fun create(updateId: Long): Update
}