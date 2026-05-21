package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type EncryptedPassportElement.type
 * - type PassportElementErrorDataField.type
 * - type PassportElementErrorFile.type
 * - type PassportElementErrorFiles.type
 * - type PassportElementErrorFrontSide.type
 * - type PassportElementErrorReverseSide.type
 * - type PassportElementErrorSelfie.type
 * - type PassportElementErrorTranslationFile.type
 * - type PassportElementErrorTranslationFiles.type
 * - type PassportElementErrorUnspecified.type
 */
@Serializable(with = PassportElementTypeSerializer::class)
class PassportElementType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<PassportElementType.Known>() {
    companion object : ResolveEnumLikeBig<PassportElementType, Known>(Known::class.java) {
        @JvmField
        val PERSONAL_DETAILS = PassportElementType("personal_details", Known.PERSONAL_DETAILS).register()

        @JvmField
        val PASSPORT = PassportElementType("passport", Known.PASSPORT).register()

        @JvmField
        val DRIVER_LICENSE = PassportElementType("driver_license", Known.DRIVER_LICENSE).register()

        @JvmField
        val IDENTITY_CARD = PassportElementType("identity_card", Known.IDENTITY_CARD).register()

        @JvmField
        val INTERNAL_PASSPORT = PassportElementType("internal_passport", Known.INTERNAL_PASSPORT).register()

        @JvmField
        val ADDRESS = PassportElementType("address", Known.ADDRESS).register()

        @JvmField
        val UTILITY_BILL = PassportElementType("utility_bill", Known.UTILITY_BILL).register()

        @JvmField
        val BANK_STATEMENT = PassportElementType("bank_statement", Known.BANK_STATEMENT).register()

        @JvmField
        val RENTAL_AGREEMENT = PassportElementType("rental_agreement", Known.RENTAL_AGREEMENT).register()

        @JvmField
        val PASSPORT_REGISTRATION = PassportElementType("passport_registration", Known.PASSPORT_REGISTRATION).register()

        @JvmField
        val TEMPORARY_REGISTRATION = PassportElementType("temporary_registration", Known.TEMPORARY_REGISTRATION).register()

        @JvmField
        val PHONE_NUMBER = PassportElementType("phone_number", Known.PHONE_NUMBER).register()

        @JvmField
        val EMAIL = PassportElementType("email", Known.EMAIL).register()

        override fun create(value: String): PassportElementType {
            return PassportElementType(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        PERSONAL_DETAILS,
        PASSPORT,
        DRIVER_LICENSE,
        IDENTITY_CARD,
        INTERNAL_PASSPORT,
        ADDRESS,
        UTILITY_BILL,
        BANK_STATEMENT,
        RENTAL_AGREEMENT,
        PASSPORT_REGISTRATION,
        TEMPORARY_REGISTRATION,
        PHONE_NUMBER,
        EMAIL;

        fun toPassportElementType(): PassportElementType {
            return PassportElementType.mapGet(this)
        }
    }

    override fun toString(): String {
        return "PassportElementType($value)"
    }
}

internal object PassportElementTypeSerializer : EnumLikeJsonSerializer<PassportElementType>("PassportElementType", PassportElementType)

