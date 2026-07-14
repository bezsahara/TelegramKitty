package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A table, corresponding to the HTML tag <table>.
 *
 * [link](https://core.telegram.org/bots/api#richblocktable): https://core.telegram.org/bots/api#richblocktable
 *
 * @param type Type of the block, always "table"
 * @param cells Cells of the table
 * @param isBordered Optional. True, if the table has borders
 * @param isStriped Optional. True, if the table is striped
 * @param caption Optional. Caption of the table
 */
@Serializable
data class RichBlockTable(
    val cells: List<List<RichBlockTableCell>>,
    @SerialName("is_bordered") val isBordered: Boolean? = null,
    @SerialName("is_striped") val isStriped: Boolean? = null,
    val caption: RichText? = null
) : RichBlock {
    override val type: String = "table"
}
