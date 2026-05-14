package org.bezsahara.kittybot.telegram.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ok<T>(
    val ok: Boolean,
    @JvmField val result: T,
    val description: String? = null
)

@Serializable
data class TelegramError(
    // ok should always be false
    val ok: Boolean,
    @SerialName("error_code") val errorCode: Int,
    val description: String,
    val parameters: ResponseParameters? = null
) {
    fun messageIsTooLong(): Boolean {
        return description == "Bad Request: message is too long"
    }

    fun botWasBlocked(): Boolean {
        return description == "Forbidden: bot was blocked by the user"
    }

    companion object {
        val none = TelegramError(ok = false, errorCode = -1, description = "")
    }
}

@Serializable
data class ResponseParameters(
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long? = null,
    @SerialName("retry_after") val retryAfter: Long? = null
)

@Serializable
class OkBoolOpt(
    val ok: Boolean,
    @JvmField val result: Boolean,
)

class TelegramErrorException(val error: TelegramError) : RuntimeException() {
    override val message: String
        get() = buildString {
            append(error.errorCode).append(": ").append(error.description)
            if (error.parameters != null) {
                append(" | params: ").append(error.parameters)
            }
        }
}