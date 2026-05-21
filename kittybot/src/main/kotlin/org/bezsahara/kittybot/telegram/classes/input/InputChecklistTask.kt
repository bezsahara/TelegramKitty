package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.values.ParseMode


/**
 * Describes a task to add to a checklist.
 * 
 * [link](https://core.telegram.org/bots/api#inputchecklisttask): https://core.telegram.org/bots/api#inputchecklisttask
 * 
 * @param id Unique identifier of the task; must be positive and unique among all task identifiers currently present in the checklist
 * @param text Text of the task; 1-100 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the text. See formatting options for more details.
 * @param textEntities Optional. List of special entities that appear in the text, which can be specified instead of parse_mode. Currently, only bold, italic, underline, strikethrough, spoiler, custom_emoji, and date_time entities are allowed.
 */
@Serializable
data class InputChecklistTask(
    val id: Long,
    val text: String,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null
)

