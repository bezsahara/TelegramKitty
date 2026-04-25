package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype ChatBoostSource.source
 * - type ChatBoostSourcePremium.source
 * - type ChatBoostSourceGiftCode.source
 * - type ChatBoostSourceGiveaway.source
 */
@Serializable(with = ChatBoostSourceKindSerializer::class)
class ChatBoostSourceKind internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ChatBoostSourceKind.Known>() {
    companion object : ResolveEnumLike<ChatBoostSourceKind>() {
        @JvmField
        val PREMIUM = ChatBoostSourceKind("premium", Known.PREMIUM)

        @JvmField
        val GIFT_CODE = ChatBoostSourceKind("gift_code", Known.GIFT_CODE)

        @JvmField
        val GIVEAWAY = ChatBoostSourceKind("giveaway", Known.GIVEAWAY)

        override fun resolve(value: String): ChatBoostSourceKind {
            return when (value) {
                "premium" -> PREMIUM
                "gift_code" -> GIFT_CODE
                "giveaway" -> GIVEAWAY
                else -> ChatBoostSourceKind(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PREMIUM,
        GIFT_CODE,
        GIVEAWAY;

        fun toChatBoostSourceKind(): ChatBoostSourceKind {
            return when (this) {
                PREMIUM -> ChatBoostSourceKind.PREMIUM
                GIFT_CODE -> ChatBoostSourceKind.GIFT_CODE
                GIVEAWAY -> ChatBoostSourceKind.GIVEAWAY
            }
        }
    }

    override fun toString(): String {
        return "ChatBoostSourceKind($value)"
    }
}

internal object ChatBoostSourceKindSerializer : EnumLikeJsonSerializer<ChatBoostSourceKind>("ChatBoostSourceKind", ChatBoostSourceKind)

