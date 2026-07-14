package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type RichBlockListItem.type
 */
@Serializable(with = RichBlockListItemTypeSerializer::class)
class RichBlockListItemType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<RichBlockListItemType.Known>() {
    companion object : ResolveEnumLike<RichBlockListItemType>() {
        @JvmField
        val A = RichBlockListItemType("a", Known.A)

        @JvmField
        val A_2 = RichBlockListItemType("A", Known.A_2)

        @JvmField
        val I = RichBlockListItemType("i", Known.I)

        @JvmField
        val I_2 = RichBlockListItemType("I", Known.I_2)

        @JvmField
        val VALUE_1 = RichBlockListItemType("1", Known.VALUE_1)

        override fun resolve(value: String): RichBlockListItemType {
            if (value.length > 1) return RichBlockListItemType(value, null)
            return when (value[0]) {
                'a' -> A
                'A' -> A_2
                'i' -> I
                'I' -> I_2
                '1' -> VALUE_1
                else -> RichBlockListItemType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        A,
        A_2,
        I,
        I_2,
        VALUE_1;

        fun toRichBlockListItemType(): RichBlockListItemType {
            return when (this) {
                A -> RichBlockListItemType.A
                A_2 -> RichBlockListItemType.A_2
                I -> RichBlockListItemType.I
                I_2 -> RichBlockListItemType.I_2
                VALUE_1 -> RichBlockListItemType.VALUE_1
            }
        }
    }

    override fun toString(): String {
        return "RichBlockListItemType($value)"
    }
}

internal object RichBlockListItemTypeSerializer : EnumLikeJsonSerializer<RichBlockListItemType>("RichBlockListItemType", RichBlockListItemType)
