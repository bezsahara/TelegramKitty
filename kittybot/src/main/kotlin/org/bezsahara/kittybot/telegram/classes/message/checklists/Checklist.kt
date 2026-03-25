package org.bezsahara.kittybot.telegram.classes.message.checklists

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.checklists.ChecklistTask
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a checklist.
 * 
 * [link](https://core.telegram.org/bots/api#checklist): https://core.telegram.org/bots/api#checklist
 * 
 * @param title Title of the checklist
 * @param titleEntities Optional. Special entities that appear in the checklist title
 * @param tasks List of tasks in the checklist
 * @param othersCanAddTasks Optional. True, if users other than the creator of the list can add tasks to the list
 * @param othersCanMarkTasksAsDone Optional. True, if users other than the creator of the list can mark tasks as done or not done
 */
@Serializable
data class Checklist(
    val title: String,
    val tasks: List<ChecklistTask>,
    @SerialName("title_entities") val titleEntities: List<MessageEntity>? = null,
    @SerialName("others_can_add_tasks") val othersCanAddTasks: Boolean? = null,
    @SerialName("others_can_mark_tasks_as_done") val othersCanMarkTasksAsDone: Boolean? = null
)

