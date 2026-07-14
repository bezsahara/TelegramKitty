package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.values.RichBlockTableCellAlign
import org.bezsahara.kittybot.telegram.values.RichBlockTableCellValign


/**
 * Cell in a table.
 *
 * [link](https://core.telegram.org/bots/api#richblocktablecell): https://core.telegram.org/bots/api#richblocktablecell
 *
 * @param text Optional. Text in the cell. If omitted, then the cell is invisible.
 * @param isHeader Optional. True, if the cell is a header cell
 * @param colspan Optional. The number of columns the cell spans if it is bigger than 1
 * @param rowspan Optional. The number of rows the cell spans if it is bigger than 1
 * @param align Horizontal cell content alignment. Currently, must be one of "left", "center", or "right".
 * @param valign Vertical cell content alignment. Currently, must be one of "top", "middle", or "bottom".
 */
@Serializable
data class RichBlockTableCell(
    val align: RichBlockTableCellAlign,
    val valign: RichBlockTableCellValign,
    val text: RichText? = null,
    @SerialName("is_header") val isHeader: Boolean? = null,
    val colspan: Long? = null,
    val rowspan: Long? = null
)
