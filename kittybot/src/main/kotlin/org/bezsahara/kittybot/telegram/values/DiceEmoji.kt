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
        val U_1F3B2 = DiceEmoji("🎲", Known.U_1F3B2).register()

        @JvmField
        val U_1F3AF = DiceEmoji("🎯", Known.U_1F3AF).register()

        @JvmField
        val U_1F3C0 = DiceEmoji("🏀", Known.U_1F3C0).register()

        @JvmField
        val U_26BD = DiceEmoji("⚽", Known.U_26BD).register()

        @JvmField
        val U_1F3B3 = DiceEmoji("🎳", Known.U_1F3B3).register()

        @JvmField
        val U_1F3B0 = DiceEmoji("🎰", Known.U_1F3B0).register()

        override fun create(value: String): DiceEmoji {
            return DiceEmoji(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        U_1F3B2,
        U_1F3AF,
        U_1F3C0,
        U_26BD,
        U_1F3B3,
        U_1F3B0;

        fun toDiceEmoji(): DiceEmoji {
            return DiceEmoji.mapGet(this)
        }
    }

    override fun toString(): String {
        return "DiceEmoji($value)"
    }
}

internal object DiceEmojiSerializer : EnumLikeJsonSerializer<DiceEmoji>("DiceEmoji", DiceEmoji)

