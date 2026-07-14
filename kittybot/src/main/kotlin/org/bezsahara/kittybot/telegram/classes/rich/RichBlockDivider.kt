package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer


/**
 * A divider, corresponding to the HTML tag <hr/>.
 *
 * [link](https://core.telegram.org/bots/api#richblockdivider): https://core.telegram.org/bots/api#richblockdivider
 *
 * @param type Type of the block, always "divider"
 */
@Serializable(with = RichBlockDividerJsonSerializer::class)
object RichBlockDivider : RichBlock {
    override val type: String = "divider"
}


internal class RichBlockDividerJsonSerializer : PureJsonSerializer<RichBlockDivider>("RichBlockDivider", RichBlockDivider, buildJsonObject { put("type", JsonPrimitive("divider")) })
