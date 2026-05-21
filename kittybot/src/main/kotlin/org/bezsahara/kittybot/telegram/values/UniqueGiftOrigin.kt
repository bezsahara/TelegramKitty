package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type UniqueGiftInfo.origin
 */
@Serializable(with = UniqueGiftOriginSerializer::class)
class UniqueGiftOrigin internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<UniqueGiftOrigin.Known>() {
    companion object : ResolveEnumLike<UniqueGiftOrigin>() {
        @JvmField
        val UPGRADE = UniqueGiftOrigin("upgrade", Known.UPGRADE)

        @JvmField
        val TRANSFER = UniqueGiftOrigin("transfer", Known.TRANSFER)

        @JvmField
        val RESALE = UniqueGiftOrigin("resale", Known.RESALE)

        @JvmField
        val GIFTED_UPGRADE = UniqueGiftOrigin("gifted_upgrade", Known.GIFTED_UPGRADE)

        @JvmField
        val OFFER = UniqueGiftOrigin("offer", Known.OFFER)

        override fun resolve(value: String): UniqueGiftOrigin {
            return when (value) {
                "upgrade" -> UPGRADE
                "transfer" -> TRANSFER
                "resale" -> RESALE
                "gifted_upgrade" -> GIFTED_UPGRADE
                "offer" -> OFFER
                else -> UniqueGiftOrigin(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        UPGRADE,
        TRANSFER,
        RESALE,
        GIFTED_UPGRADE,
        OFFER;

        fun toUniqueGiftOrigin(): UniqueGiftOrigin {
            return when (this) {
                UPGRADE -> UniqueGiftOrigin.UPGRADE
                TRANSFER -> UniqueGiftOrigin.TRANSFER
                RESALE -> UniqueGiftOrigin.RESALE
                GIFTED_UPGRADE -> UniqueGiftOrigin.GIFTED_UPGRADE
                OFFER -> UniqueGiftOrigin.OFFER
            }
        }
    }

    override fun toString(): String {
        return "UniqueGiftOrigin($value)"
    }
}

internal object UniqueGiftOriginSerializer : EnumLikeJsonSerializer<UniqueGiftOrigin>("UniqueGiftOrigin", UniqueGiftOrigin)

