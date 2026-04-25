package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype TransactionPartner.type
 * - type TransactionPartnerUser.type
 * - type TransactionPartnerChat.type
 * - type TransactionPartnerAffiliateProgram.type
 * - type TransactionPartnerFragment.type
 * - type TransactionPartnerTelegramAds.type
 * - type TransactionPartnerTelegramApi.type
 * - type TransactionPartnerOther.type
 */
@Serializable(with = TransactionPartnerTypeSerializer::class)
class TransactionPartnerType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<TransactionPartnerType.Known>() {
    companion object : ResolveEnumLikeBig<TransactionPartnerType, Known>(Known::class.java) {
        @JvmField
        val USER = TransactionPartnerType("user", Known.USER).register()

        @JvmField
        val CHAT = TransactionPartnerType("chat", Known.CHAT).register()

        @JvmField
        val AFFILIATE_PROGRAM = TransactionPartnerType("affiliate_program", Known.AFFILIATE_PROGRAM).register()

        @JvmField
        val FRAGMENT = TransactionPartnerType("fragment", Known.FRAGMENT).register()

        @JvmField
        val TELEGRAM_ADS = TransactionPartnerType("telegram_ads", Known.TELEGRAM_ADS).register()

        @JvmField
        val TELEGRAM_API = TransactionPartnerType("telegram_api", Known.TELEGRAM_API).register()

        @JvmField
        val OTHER = TransactionPartnerType("other", Known.OTHER).register()

        override fun create(value: String): TransactionPartnerType {
            return TransactionPartnerType(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        USER,
        CHAT,
        AFFILIATE_PROGRAM,
        FRAGMENT,
        TELEGRAM_ADS,
        TELEGRAM_API,
        OTHER;

        fun toTransactionPartnerType(): TransactionPartnerType {
            return TransactionPartnerType.mapGet(this)
        }
    }

    override fun toString(): String {
        return "TransactionPartnerType($value)"
    }
}

internal object TransactionPartnerTypeSerializer : EnumLikeJsonSerializer<TransactionPartnerType>("TransactionPartnerType", TransactionPartnerType)

