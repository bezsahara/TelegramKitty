package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Voice


/**
 * A block with a voice note, corresponding to the HTML tag <audio>.
 *
 * [link](https://core.telegram.org/bots/api#richblockvoicenote): https://core.telegram.org/bots/api#richblockvoicenote
 *
 * @param type Type of the block, always "voice_note"
 * @param voiceNote The voice note
 * @param caption Optional. Caption of the block
 */
@Serializable
data class RichBlockVoiceNote(
    @SerialName("voice_note") val voiceNote: Voice,
    val caption: RichBlockCaption? = null
) : RichBlock {
    override val type: String = "voice_note"
}
