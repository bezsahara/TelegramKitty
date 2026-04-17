package org.bezsahara.kittybot.telegram.classes.message.checklists

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a task in a checklist.
 * 
 * [link](https://core.telegram.org/bots/api#checklisttask): https://core.telegram.org/bots/api#checklisttask
 * 
 * @param id Unique identifier of the task
 * @param text Text of the task
 * @param textEntities Optional. Special entities that appear in the task text
 * @param completedByUser Optional. User that completed the task; omitted if the task wasn't completed by a user
 * @param completedByChat Optional. Chat that completed the task; omitted if the task wasn't completed by a chat
 * @param completionDate Optional. Point in time (Unix timestamp) when the task was completed; 0 if the task wasn't completed
 */
@Serializable
data class ChecklistTask(
    val id: Long,
    val text: String,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
    @SerialName("completed_by_user") val completedByUser: User? = null,
    @SerialName("completed_by_chat") val completedByChat: Chat? = null,
    @SerialName("completion_date") val completionDate: Long? = null
)

