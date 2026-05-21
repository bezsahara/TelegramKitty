package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.checklists.ChecklistTask


/**
 * Describes a service message about tasks added to a checklist.
 * 
 * [link](https://core.telegram.org/bots/api#checklisttasksadded): https://core.telegram.org/bots/api#checklisttasksadded
 * 
 * @param checklistMessage Optional. Message containing the checklist to which the tasks were added. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param tasks List of tasks added to the checklist
 */
@Serializable
data class ChecklistTasksAdded(
    val tasks: List<ChecklistTask>,
    @SerialName("checklist_message") val checklistMessage: Message? = null
)

