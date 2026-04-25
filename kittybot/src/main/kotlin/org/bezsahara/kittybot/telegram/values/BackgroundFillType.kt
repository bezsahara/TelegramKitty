package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype BackgroundFill.type
 * - type BackgroundFillSolid.type
 * - type BackgroundFillGradient.type
 * - type BackgroundFillFreeformGradient.type
 */
@Serializable(with = BackgroundFillTypeSerializer::class)
class BackgroundFillType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<BackgroundFillType.Known>() {
    companion object : ResolveEnumLike<BackgroundFillType>() {
        @JvmField
        val SOLID = BackgroundFillType("solid", Known.SOLID)

        @JvmField
        val GRADIENT = BackgroundFillType("gradient", Known.GRADIENT)

        @JvmField
        val FREEFORM_GRADIENT = BackgroundFillType("freeform_gradient", Known.FREEFORM_GRADIENT)

        override fun resolve(value: String): BackgroundFillType {
            return when (value) {
                "solid" -> SOLID
                "gradient" -> GRADIENT
                "freeform_gradient" -> FREEFORM_GRADIENT
                else -> BackgroundFillType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        SOLID,
        GRADIENT,
        FREEFORM_GRADIENT;

        fun toBackgroundFillType(): BackgroundFillType {
            return when (this) {
                SOLID -> BackgroundFillType.SOLID
                GRADIENT -> BackgroundFillType.GRADIENT
                FREEFORM_GRADIENT -> BackgroundFillType.FREEFORM_GRADIENT
            }
        }
    }

    override fun toString(): String {
        return "BackgroundFillType($value)"
    }
}

internal object BackgroundFillTypeSerializer : EnumLikeJsonSerializer<BackgroundFillType>("BackgroundFillType", BackgroundFillType)

