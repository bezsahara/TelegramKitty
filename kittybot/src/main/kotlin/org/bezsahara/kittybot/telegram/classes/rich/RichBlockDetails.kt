package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * An expandable block for details disclosure, corresponding to the HTML tag <details>.
 *
 * [link](https://core.telegram.org/bots/api#richblockdetails): https://core.telegram.org/bots/api#richblockdetails
 *
 * @param type Type of the block, always "details"
 * @param summary Always shown summary of the block
 * @param blocks Content of the block
 * @param isOpen Optional. True, if the content of the block is visible by default
 */
@Serializable
data class RichBlockDetails(
    val summary: RichText,
    val blocks: List<RichBlock>,
    @SerialName("is_open") val isOpen: Boolean? = null
) : RichBlock {
    override val type: String = "details"
}
