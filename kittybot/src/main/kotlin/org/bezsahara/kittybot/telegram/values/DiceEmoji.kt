package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - method sendDice.emoji
 */
@Serializable(with = DiceEmojiSerializer::class)
class DiceEmoji internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<DiceEmoji.Known>() {
    companion object : ResolveEnumLikeBig<DiceEmoji, Known>(Known::class.java) {
        @JvmField
        val DICE = DiceEmoji("🎲", Known.DICE).register()

        @JvmField
        val DARTS = DiceEmoji("🎯", Known.DARTS).register()

        @JvmField
        val BASKETBALL = DiceEmoji("🏀", Known.BASKETBALL).register()

        @JvmField
        val FOOTBALL = DiceEmoji("⚽", Known.FOOTBALL).register()

        @JvmField
        val BOWLING = DiceEmoji("🎳", Known.BOWLING).register()

        @JvmField
        val SLOT_MACHINE = DiceEmoji("🎰", Known.SLOT_MACHINE).register()

        override fun create(value: String): DiceEmoji {
            return DiceEmoji(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        DICE,
        DARTS,
        BASKETBALL,
        FOOTBALL,
        BOWLING,
        SLOT_MACHINE;

        fun toDiceEmoji(): DiceEmoji {
            return DiceEmoji.mapGet(this)
        }
    }

    override fun toString(): String {
        return "DiceEmoji($value)"
    }
}

internal object DiceEmojiSerializer : EnumLikeJsonSerializer<DiceEmoji>("DiceEmoji", DiceEmoji)

