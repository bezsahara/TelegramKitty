package org.bezsahara.kittybot.telegram.classes.core.update

import kotlinx.serialization.json.JsonObject
import org.bezsahara.kittybot.telegram.classes.chat.ChatId


// In case telegram decides to create new update type
data class UnknownUpdate(
    override val updateId: Long,
    val jsonObject: JsonObject
) : Update() {
    override val ordinal: Int
        get() = 24
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId? = null

    fun asJsonString(): String {
        return jsonObject.toString()
    }

    companion object : UpdKind(
        24, UnknownUpdate::class.java, "UnknownUpdate"
    )
}
