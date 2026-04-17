package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.input.InputChecklistTask
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.utils.ParseMode
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a checklist to create.
 * 
 * [link](https://core.telegram.org/bots/api#inputchecklist): https://core.telegram.org/bots/api#inputchecklist
 * 
 * @param title Title of the checklist; 1-255 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the title. See formatting options for more details.
 * @param titleEntities Optional. List of special entities that appear in the title, which can be specified instead of parse_mode. Currently, only bold, italic, underline, strikethrough, spoiler, custom_emoji, and date_time entities are allowed.
 * @param tasks List of 1-30 tasks in the checklist
 * @param othersCanAddTasks Optional. Pass True if other users can add tasks to the checklist
 * @param othersCanMarkTasksAsDone Optional. Pass True if other users can mark tasks as done or not done in the checklist
 */
@Serializable
data class InputChecklist(
    val title: String,
    val tasks: List<InputChecklistTask>,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("title_entities") val titleEntities: List<MessageEntity>? = null,
    @SerialName("others_can_add_tasks") val othersCanAddTasks: Boolean? = null,
    @SerialName("others_can_mark_tasks_as_done") val othersCanMarkTasksAsDone: Boolean? = null
)

