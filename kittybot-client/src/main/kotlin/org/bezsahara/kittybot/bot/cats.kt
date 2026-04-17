package org.bezsahara.kittybot.bot

import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientRequest
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.RequestOptions
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.withContext
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.client.TApiClient
import org.bezsahara.kittybot.telegram.client.file.TelegramFileVertx
import org.bezsahara.kittybot.telegram.utils.TResult
import java.net.URLEncoder
import java.util.function.Function

var theCatApiToken: String? = null


/**
 * Uses the https://thecatapi.com/ API.
 * Please use this service responsibly. Can be used without the token.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendTheCatApi(chatId: ChatId): TResult<Message> {
    val apiClient = vertxClientForCats()

    val byteArray = apiClient.client.request(RequestOptions()
        .setMethod(HttpMethod.GET)
        .setAbsoluteURI("https://api.thecatapi.com/v1/images/search?limit=1")
    )
        .compose(Function {
            theCatApiToken?.let { token ->
                it.putHeader("x-api-key", token)
            }
            it.send()
        })
        .compose(Function {
            it.body()
        })
        .compose(Function {
            val url = it.toJsonArray().getJsonObject(0).getString("url")!!

            apiClient.client.request(RequestOptions().setMethod(HttpMethod.GET)
                .setAbsoluteURI(url))
        })
        .compose(vertxFunctionSend)
        .compose(vertxFunctionBody)
        .coAwait().bytes

    return sendPhoto(chatId, TelegramFileVertx.Bytes(byteArray))
}

/**
 * Uses the https://cataas.com/ API. Please use this service responsibly.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendCatPicture(chatId: ChatId, says: String? = null): TResult<Message> {
    val apiClient = vertxClientForCats()
    return withContext(apiClient.dispatcher) {
        val urlStr = if (says != null)
            "https://cataas.com/cat/says/${
                URLEncoder.encode(
                    says,
                    Charsets.UTF_8
                )
            }?font=Impact&fontSize=30&fontColor=%23000&fontBackground=%23FFFFFF&position=center"
        else
            "https://cataas.com/cat?position=center"


        val bytes = apiClient.client.request(RequestOptions()
            .setMethod(HttpMethod.GET)
            .setAbsoluteURI(urlStr))
            .compose(vertxFunctionSend)
            .compose(vertxFunctionBody)
            .coAwait().bytes

        sendPhoto(chatId, TelegramFileVertx.Bytes(bytes))
    }
}

/**
 * Uses the https://http.cat/ API. Please use this service responsibly.
 * The author of KittyBot is not responsible for how you use this code or the content generated.
 */
suspend fun KittyBot.sendHttpCat(
    chatId: ChatId,
    httpCode: Int
): TResult<Message> {
    val apiClient = vertxClientForCats()
    return withContext(apiClient.dispatcher) {
        val bytes = apiClient.client.request(RequestOptions().setMethod(HttpMethod.GET).setAbsoluteURI("https://http.cat/$httpCode"))
            .compose(vertxFunctionSend)
            .compose(vertxFunctionBody)
            .coAwait().bytes
        sendPhoto(chatId, TelegramFileVertx.Bytes(bytes))
    }
}

private val vertxFunctionSend = Function<HttpClientRequest, Future<HttpClientResponse>> { it.send() }
private val vertxFunctionBody = Function<HttpClientResponse, Future<Buffer>> { it.body() }

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

fun KittyBot.vertxClientForCats(): TApiClient {
    return vertxClient("You need to use Vert.x client for sending cat pics.")
}