package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype OwnedGift.type
 * - type OwnedGiftRegular.type
 * - type OwnedGiftUnique.type
 */
@Serializable(with = OwnedGiftTypeSerializer::class)
class OwnedGiftType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<OwnedGiftType.Known>() {
    companion object : ResolveEnumLike<OwnedGiftType>() {
        @JvmField
        val REGULAR = OwnedGiftType("regular", Known.REGULAR)

        @JvmField
        val UNIQUE = OwnedGiftType("unique", Known.UNIQUE)

        override fun resolve(value: String): OwnedGiftType {
            return when (value) {
                "regular" -> REGULAR
                "unique" -> UNIQUE
                else -> OwnedGiftType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        REGULAR,
        UNIQUE;

        fun toOwnedGiftType(): OwnedGiftType {
            return when (this) {
                REGULAR -> OwnedGiftType.REGULAR
                UNIQUE -> OwnedGiftType.UNIQUE
            }
        }
    }

    override fun toString(): String {
        return "OwnedGiftType($value)"
    }
}

internal object OwnedGiftTypeSerializer : EnumLikeJsonSerializer<OwnedGiftType>("OwnedGiftType", OwnedGiftType)

