package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Audio


/**
 * A block with a music file, corresponding to the HTML tag <audio>.
 *
 * [link](https://core.telegram.org/bots/api#richblockaudio): https://core.telegram.org/bots/api#richblockaudio
 *
 * @param type Type of the block, always "audio"
 * @param audio The audio
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockAudio(
    val audio: Audio,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "audio"
}
