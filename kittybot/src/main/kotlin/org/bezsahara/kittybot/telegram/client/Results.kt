package org.bezsahara.kittybot.telegram.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Ok<T>(
    val ok: Boolean,
    @JvmField val result: T,
    val description: String? = null
)

@Serializable
data class TelegramError(
    // ok should always be false
    val ok: Boolean,
    @SerialName("error_code") val errorCode: Int,
    val description: String
) {
    companion object {
        val none = TelegramError(ok = false, errorCode = -1, description = "")
    }
}