package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type RichBlockTableCell.align
 */
@Serializable(with = RichBlockTableCellAlignSerializer::class)
class RichBlockTableCellAlign internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<RichBlockTableCellAlign.Known>() {
    companion object : ResolveEnumLike<RichBlockTableCellAlign>() {
        @JvmField
        val LEFT = RichBlockTableCellAlign("left", Known.LEFT)

        @JvmField
        val CENTER = RichBlockTableCellAlign("center", Known.CENTER)

        @JvmField
        val RIGHT = RichBlockTableCellAlign("right", Known.RIGHT)

        override fun resolve(value: String): RichBlockTableCellAlign {
            return when (value) {
                "left" -> LEFT
                "center" -> CENTER
                "right" -> RIGHT
                else -> RichBlockTableCellAlign(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        LEFT,
        CENTER,
        RIGHT;

        fun toRichBlockTableCellAlign(): RichBlockTableCellAlign {
            return when (this) {
                LEFT -> RichBlockTableCellAlign.LEFT
                CENTER -> RichBlockTableCellAlign.CENTER
                RIGHT -> RichBlockTableCellAlign.RIGHT
            }
        }
    }

    override fun toString(): String {
        return "RichBlockTableCellAlign($value)"
    }
}

internal object RichBlockTableCellAlignSerializer : EnumLikeJsonSerializer<RichBlockTableCellAlign>("RichBlockTableCellAlign", RichBlockTableCellAlign)
