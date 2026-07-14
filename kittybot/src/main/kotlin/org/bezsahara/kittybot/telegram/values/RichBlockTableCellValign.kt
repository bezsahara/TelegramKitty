package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type RichBlockTableCell.valign
 */
@Serializable(with = RichBlockTableCellValignSerializer::class)
class RichBlockTableCellValign internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<RichBlockTableCellValign.Known>() {
    companion object : ResolveEnumLike<RichBlockTableCellValign>() {
        @JvmField
        val TOP = RichBlockTableCellValign("top", Known.TOP)

        @JvmField
        val MIDDLE = RichBlockTableCellValign("middle", Known.MIDDLE)

        @JvmField
        val BOTTOM = RichBlockTableCellValign("bottom", Known.BOTTOM)

        override fun resolve(value: String): RichBlockTableCellValign {
            return when (value) {
                "top" -> TOP
                "middle" -> MIDDLE
                "bottom" -> BOTTOM
                else -> RichBlockTableCellValign(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        TOP,
        MIDDLE,
        BOTTOM;

        fun toRichBlockTableCellValign(): RichBlockTableCellValign {
            return when (this) {
                TOP -> RichBlockTableCellValign.TOP
                MIDDLE -> RichBlockTableCellValign.MIDDLE
                BOTTOM -> RichBlockTableCellValign.BOTTOM
            }
        }
    }

    override fun toString(): String {
        return "RichBlockTableCellValign($value)"
    }
}

internal object RichBlockTableCellValignSerializer : EnumLikeJsonSerializer<RichBlockTableCellValign>("RichBlockTableCellValign", RichBlockTableCellValign)
