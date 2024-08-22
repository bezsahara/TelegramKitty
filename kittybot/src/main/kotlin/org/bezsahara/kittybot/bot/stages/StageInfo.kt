package org.bezsahara.kittybot.bot.stages

import io.ktor.http.*


/**
 * Information about stage. Accessible from [StageChainScope].
 * @param stagePath a path without any arguments, it should look like this /path/to/something
 * @param stageArgs arguments that might be present in a stage (if you set them).
 * @see Stage
 */
class StageInfo(
    val stagePath: Stage,
    val stageArgs: Array<String>
) {
    companion object {
        fun fromStage(stage: Stage): StageInfo {
            val parts = stage.value.split("/&")
            val path = parts[0]
            val args = Array(parts.size - 1) {
                parts[it + 1].decodeURLPart()
            }
            return StageInfo(Stage(path), args)
        }

        val none = StageInfo(Stage.none, emptyArray())
    }
}

fun Stage.withArguments(vararg args: String): Stage {
    return Stage(args.joinToString(separator = "/", prefix = "${this.value}/&") {
        it.encodeURLPathPart()
    })
}

fun Stage.withArguments(args: List<String>): Stage {
    return Stage(args.joinToString(separator = "/", prefix = "${this.value}/&") {
        it.encodeURLPathPart()
    })
}

private fun String.decodeSpecialChars(): String {
    val result = StringBuilder(length)
    var i = 0
    while (i < length) {
        val allowed = i < this.length - 1
        when {
            allowed && this[i] == '&' && this[i + 1] == '&' -> {
                result.append('&')
                i += 2
            }
            allowed && this[i] == '/' && this[i + 1] == '/' -> {
                result.append('/')
                i += 2
            }
            else -> {
                result.append(this[i])
                i++
            }
        }
    }
    return result.toString()
}

private fun String.encodeSpecialChars(): String {
    val sb = StringBuilder(length)

    var index = 0
    while (index < length) {
        val c = this[index]

        when (c) {
            '/' -> {
                sb.append("//")
            }
            '&' -> {
                sb.append("&&")
            }
            else -> {
                sb.append(c)
            }
        }
        index++
    }

    return sb.toString()
}

fun extractPath(input: Stage): Stage {
    val endIndex = (input.value as java.lang.String).indexOf("/&", 0)
    return Stage(if (endIndex == -1) input.value else (input.value as String).substring(0, endIndex))
}