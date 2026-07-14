package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.values.RichBlockListItemType


/**
 * An item of a list.
 *
 * [link](https://core.telegram.org/bots/api#richblocklistitem): https://core.telegram.org/bots/api#richblocklistitem
 *
 * @param label Label of the item
 * @param blocks The content of the item
 * @param hasCheckbox Optional. True, if the item has a checkbox
 * @param isChecked Optional. True, if the item has a checked checkbox
 * @param value Optional. For ordered lists, the numeric value of the item label
 * @param type Optional. For ordered lists, the type of the item label; must be one of "a" for lowercase letters, "A" for uppercase letters, "i" for lowercase Roman numerals, "I" for uppercase Roman numerals, or "1" for decimal numbers
 */
@Serializable
data class RichBlockListItem(
    val label: String,
    val blocks: List<RichBlock>,
    @SerialName("has_checkbox") val hasCheckbox: Boolean? = null,
    @SerialName("is_checked") val isChecked: Boolean? = null,
    val value: Long? = null,
    val type: RichBlockListItemType? = null
)
