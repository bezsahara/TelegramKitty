package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.geo.Location


/**
 * A block with a map, corresponding to the custom HTML tag <tg-map>.
 *
 * [link](https://core.telegram.org/bots/api#richblockmap): https://core.telegram.org/bots/api#richblockmap
 *
 * @param type Type of the block, always "map"
 * @param location Location of the center of the map
 * @param zoom Map zoom level; 13-20
 * @param width Expected width of the map
 * @param height Expected height of the map
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockMap(
    val location: Location,
    val zoom: Long,
    val width: Long,
    val height: Long,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "map"
}
