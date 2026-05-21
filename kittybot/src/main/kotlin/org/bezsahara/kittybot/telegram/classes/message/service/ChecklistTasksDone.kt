package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.Message


/**
 * Describes a service message about checklist tasks marked as done or not done.
 * 
 * [link](https://core.telegram.org/bots/api#checklisttasksdone): https://core.telegram.org/bots/api#checklisttasksdone
 * 
 * @param checklistMessage Optional. Message containing the checklist whose tasks were marked as done or not done. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param markedAsDoneTaskIds Optional. Identifiers of the tasks that were marked as done
 * @param markedAsNotDoneTaskIds Optional. Identifiers of the tasks that were marked as not done
 */
@Serializable
data class ChecklistTasksDone(
    @SerialName("checklist_message") val checklistMessage: Message? = null,
    @SerialName("marked_as_done_task_ids") val markedAsDoneTaskIds: List<Long>? = null,
    @SerialName("marked_as_not_done_task_ids") val markedAsNotDoneTaskIds: List<Long>? = null
)

