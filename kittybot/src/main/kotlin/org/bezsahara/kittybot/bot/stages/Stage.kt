package org.bezsahara.kittybot.bot.stages

import org.bezsahara.kittybot.bot.stages.builder.stageHandler


/**
 * Used in stage handlers. Users are marked with these stages in [StageManager].
 * The purpose is to guide tbot interaction based on metadata that does not depend on what
 * telegram bot receives. You can use [stageHandler] to create a handler that will work
 * when user's stage and handler's stage are the same.
 *
 * It is also quite useful if you need to pass some additional data between handlers about the user
 * because it supports arguments.
 * `/path/to/something/&argument1/&arg2/&argN`
 */
@JvmInline
value class Stage(val value: String) {
    init {
        require(value[0] == '/') {
            "Stage should always start with '/'"
        }
        require(!(value == "//" && registeredNone)) {
            "Stage '//' is reserved"
        }
    }

    companion object {
        internal var registeredNone = false
        internal val none = Stage("//")

        init {
            registeredNone = true
        }
    }
}

fun String.toStage(): Stage = Stage(this)