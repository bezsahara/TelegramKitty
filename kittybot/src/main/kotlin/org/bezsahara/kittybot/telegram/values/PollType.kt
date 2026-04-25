package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type Poll.type
 * - method sendPoll.type
 * - type KeyboardButtonPollType.type
 */
@Serializable(with = PollTypeSerializer::class)
class PollType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<PollType.Known>() {
    companion object : ResolveEnumLike<PollType>() {
        @JvmField
        val REGULAR = PollType("regular", Known.REGULAR)

        @JvmField
        val QUIZ = PollType("quiz", Known.QUIZ)

        override fun resolve(value: String): PollType {
            return when (value) {
                "regular" -> REGULAR
                "quiz" -> QUIZ
                else -> PollType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        REGULAR,
        QUIZ;

        fun toPollType(): PollType {
            return when (this) {
                REGULAR -> PollType.REGULAR
                QUIZ -> PollType.QUIZ
            }
        }
    }

    override fun toString(): String {
        return "PollType($value)"
    }
}

internal object PollTypeSerializer : EnumLikeJsonSerializer<PollType>("PollType", PollType)

