package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type UniqueGiftModel.rarity
 */
@Serializable(with = UniqueGiftModelRaritySerializer::class)
class UniqueGiftModelRarity internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<UniqueGiftModelRarity.Known>() {
    companion object : ResolveEnumLike<UniqueGiftModelRarity>() {
        @JvmField
        val UNCOMMON = UniqueGiftModelRarity("uncommon", Known.UNCOMMON)

        @JvmField
        val RARE = UniqueGiftModelRarity("rare", Known.RARE)

        @JvmField
        val EPIC = UniqueGiftModelRarity("epic", Known.EPIC)

        @JvmField
        val LEGENDARY = UniqueGiftModelRarity("legendary", Known.LEGENDARY)

        override fun resolve(value: String): UniqueGiftModelRarity {
            return when (value) {
                "uncommon" -> UNCOMMON
                "rare" -> RARE
                "epic" -> EPIC
                "legendary" -> LEGENDARY
                else -> UniqueGiftModelRarity(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY;

        fun toUniqueGiftModelRarity(): UniqueGiftModelRarity {
            return when (this) {
                UNCOMMON -> UniqueGiftModelRarity.UNCOMMON
                RARE -> UniqueGiftModelRarity.RARE
                EPIC -> UniqueGiftModelRarity.EPIC
                LEGENDARY -> UniqueGiftModelRarity.LEGENDARY
            }
        }
    }

    override fun toString(): String {
        return "UniqueGiftModelRarity($value)"
    }
}

internal object UniqueGiftModelRaritySerializer : EnumLikeJsonSerializer<UniqueGiftModelRarity>("UniqueGiftModelRarity", UniqueGiftModelRarity)

