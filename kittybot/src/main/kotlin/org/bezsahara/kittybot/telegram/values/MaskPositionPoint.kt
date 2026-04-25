package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type MaskPosition.point
 */
@Serializable(with = MaskPositionPointSerializer::class)
class MaskPositionPoint internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<MaskPositionPoint.Known>() {
    companion object : ResolveEnumLike<MaskPositionPoint>() {
        @JvmField
        val FOREHEAD = MaskPositionPoint("forehead", Known.FOREHEAD)

        @JvmField
        val EYES = MaskPositionPoint("eyes", Known.EYES)

        @JvmField
        val MOUTH = MaskPositionPoint("mouth", Known.MOUTH)

        @JvmField
        val CHIN = MaskPositionPoint("chin", Known.CHIN)

        override fun resolve(value: String): MaskPositionPoint {
            return when (value) {
                "forehead" -> FOREHEAD
                "eyes" -> EYES
                "mouth" -> MOUTH
                "chin" -> CHIN
                else -> MaskPositionPoint(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        FOREHEAD,
        EYES,
        MOUTH,
        CHIN;

        fun toMaskPositionPoint(): MaskPositionPoint {
            return when (this) {
                FOREHEAD -> MaskPositionPoint.FOREHEAD
                EYES -> MaskPositionPoint.EYES
                MOUTH -> MaskPositionPoint.MOUTH
                CHIN -> MaskPositionPoint.CHIN
            }
        }
    }

    override fun toString(): String {
        return "MaskPositionPoint($value)"
    }
}

internal object MaskPositionPointSerializer : EnumLikeJsonSerializer<MaskPositionPoint>("MaskPositionPoint", MaskPositionPoint)

