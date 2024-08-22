package org.bezsahara.kittybot.bot

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.util.cio.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.classes.messages.inaccessible.Message
import org.bezsahara.kittybot.telegram.client.TelegramFile
import org.bezsahara.kittybot.telegram.utils.TResult

var theCatApiToken: String? = null

/**
 * Uses the https://thecatapi.com/ API.
 * Please use this service responsibly. Can be used without the token.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendTheCatApi(chatId:ChatId) {
    val result = apiClient.client.get {
        url("https://api.thecatapi.com/v1/images/search?limit=1")
        theCatApiToken?.let { headers["x-api-key"] = it }
        headers["Content-Type"] = "application/json"
    }
    val url = if (result.status.isSuccess()) {
        apiClient.json.decodeFromString(JsonElement.serializer(), result.bodyAsText())
            .jsonArray[0]
            .jsonObject["url"]!!
            .jsonPrimitive.content
    } else {
        hiss("A problem occurred: ${result.bodyAsText()}")
    }
    val imageResult = apiClient.client.get { url(url) }
    val byteArray = if (imageResult.status.isSuccess()) {
        imageResult.bodyAsChannel().toByteArray()
    } else {
        hiss("A problem occurred: ${result.bodyAsText()}")
    }
    sendPhoto(chatId, TelegramFile.Bytes(byteArray))
}

/**
 * Uses the https://cataas.com/ API. Please use this service responsibly.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendCatPicture(chatId: ChatId, says: String? = null): TResult<Message> = withContext(Dispatchers.IO) {
    val byteArray: ByteArray = if (says == null) {
        apiClient.client
            .get("https://cataas.com/cat?position=center")
            .bodyAsChannel()
            .toByteArray()
    } else {
        val carSays = says.encodeURLPathPart()
        apiClient.client
            .get("https://cataas.com/cat/says/$carSays?font=Impact&fontSize=30&fontColor=%23000&fontBackground=%23FFFFFF&position=center")
            .bodyAsChannel()
            .toByteArray()
    }
    sendPhoto(chatId, TelegramFile.Bytes(byteArray))
}

private val allowedHttpCodes = intArrayOf(525, 530, 599)

/**
 * Uses the https://http.cat/ API. Please use this service responsibly.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendHttpCat(
    chatId: ChatId,
    httpCode: Int,
    acceptAny: Boolean = false
): TResult<Message> = withContext(Dispatchers.IO) {
    HttpStatusCode.fromValue(httpCode).let {
        if (it.description == "Unknown Status Code" && !acceptAny && httpCode !in allowedHttpCodes) {
            return@withContext sendMessage(chatId, "$httpCode is unknown status code")
        }
    }
    val bytes = apiClient.client.get("https://http.cat/$httpCode")
        .bodyAsChannel()
        .toByteArray()
    sendPhoto(chatId, TelegramFile.Bytes(bytes))
}

suspend fun KittyBot.sendTextCat(chatId: ChatId): TResult<Message> {
    val toSend = """
|                     ＿＿
　　　　/ ＞　　フ
　　　　| 　_　 _ l
 　　　／` ミ＿xノ
　 　 /　　　 　 |
　　 /　 ヽ　　 ﾉ
 　 │　　|　|　|
／￣|　　 |　|　|
| (￣ヽ＿ヽ)__)
＼二つ
    """
    return sendMessage(chatId, toSend)
}