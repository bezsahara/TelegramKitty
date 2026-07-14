package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A block with a "Thinking..." placeholder, corresponding to the custom HTML tag <tg-thinking>. The block may be used only in sendRichMessageDraft, therefore it can't be received in messages. See https://t.me/addemoji/AIActions for examples of custom emoji, which are recommended for usage in the block.
 *
 * [link](https://core.telegram.org/bots/api#richblockthinking): https://core.telegram.org/bots/api#richblockthinking
 *
 * @param type Type of the block, always "thinking"
 * @param text Text of the block. See https://t.me/addemoji/AIActions for examples of custom emoji, which are recommended for usage in the block.
 */
@Serializable
data class RichBlockThinking(
    val text: RichText
) : RichBlock {
    override val type: String = "thinking"
}
