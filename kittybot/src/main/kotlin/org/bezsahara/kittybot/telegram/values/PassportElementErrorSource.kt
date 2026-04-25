package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype PassportElementError.source
 * - type PassportElementErrorDataField.source
 * - type PassportElementErrorFrontSide.source
 * - type PassportElementErrorReverseSide.source
 * - type PassportElementErrorSelfie.source
 * - type PassportElementErrorFile.source
 * - type PassportElementErrorFiles.source
 * - type PassportElementErrorTranslationFile.source
 * - type PassportElementErrorTranslationFiles.source
 * - type PassportElementErrorUnspecified.source
 */
@Serializable(with = PassportElementErrorSourceSerializer::class)
class PassportElementErrorSource internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<PassportElementErrorSource.Known>() {
    companion object : ResolveEnumLikeBig<PassportElementErrorSource, Known>(Known::class.java) {
        @JvmField
        val DATA = PassportElementErrorSource("data", Known.DATA).register()

        @JvmField
        val FRONT_SIDE = PassportElementErrorSource("front_side", Known.FRONT_SIDE).register()

        @JvmField
        val REVERSE_SIDE = PassportElementErrorSource("reverse_side", Known.REVERSE_SIDE).register()

        @JvmField
        val SELFIE = PassportElementErrorSource("selfie", Known.SELFIE).register()

        @JvmField
        val FILE = PassportElementErrorSource("file", Known.FILE).register()

        @JvmField
        val FILES = PassportElementErrorSource("files", Known.FILES).register()

        @JvmField
        val TRANSLATION_FILE = PassportElementErrorSource("translation_file", Known.TRANSLATION_FILE).register()

        @JvmField
        val TRANSLATION_FILES = PassportElementErrorSource("translation_files", Known.TRANSLATION_FILES).register()

        @JvmField
        val UNSPECIFIED = PassportElementErrorSource("unspecified", Known.UNSPECIFIED).register()

        override fun create(value: String): PassportElementErrorSource {
            return PassportElementErrorSource(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        DATA,
        FRONT_SIDE,
        REVERSE_SIDE,
        SELFIE,
        FILE,
        FILES,
        TRANSLATION_FILE,
        TRANSLATION_FILES,
        UNSPECIFIED;

        fun toPassportElementErrorSource(): PassportElementErrorSource {
            return PassportElementErrorSource.mapGet(this)
        }
    }

    override fun toString(): String {
        return "PassportElementErrorSource($value)"
    }
}

internal object PassportElementErrorSourceSerializer : EnumLikeJsonSerializer<PassportElementErrorSource>("PassportElementErrorSource", PassportElementErrorSource)

