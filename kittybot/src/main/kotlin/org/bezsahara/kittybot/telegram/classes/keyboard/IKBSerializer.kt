package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.classes.games.CallbackGame
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardButton.*
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo

// ---- Custom serializer: flat shape, strict one-of on decode ----
object IKBSerializer : KSerializer<InlineKeyboardButton> {
    private const val IDX_TEXT = 0
    private const val IDX_URL = 1
    private const val IDX_CALLBACK_DATA = 2
    private const val IDX_WEB_APP = 3
    private const val IDX_LOGIN_URL = 4
    private const val IDX_SWITCH_INLINE_QUERY = 5
    private const val IDX_SWITCH_INLINE_QUERY_CURRENT_CHAT = 6
    private const val IDX_SWITCH_INLINE_QUERY_CHOSEN_CHAT = 7
    private const val IDX_COPY_TEXT = 8
    private const val IDX_CALLBACK_GAME = 9
    private const val IDX_PAY = 10

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("InlineKeyboardButton") {
            element<String>("text")
            element<String?>("url", isOptional = true)
            element<String?>("callback_data", isOptional = true)
            element<WebAppInfo?>("web_app", isOptional = true)
            element<LoginUrl?>("login_url", isOptional = true)
            element<String?>("switch_inline_query", isOptional = true)
            element<String?>("switch_inline_query_current_chat", isOptional = true)
            element<SwitchInlineQueryChosenChat?>("switch_inline_query_chosen_chat", isOptional = true)
            element<CopyTextButton?>("copy_text", isOptional = true)
            element<CallbackGame?>("callback_game", isOptional = true)
            element<Boolean?>("pay", isOptional = true)
        }

    override fun serialize(encoder: Encoder, value: InlineKeyboardButton) {
        val out = encoder.beginStructure(descriptor)
        out.encodeStringElement(descriptor, IDX_TEXT, value.text)

        when (value) {
            is Url ->
                out.encodeStringElement(descriptor, IDX_URL, value.url)

            is Callback ->
                out.encodeStringElement(descriptor, IDX_CALLBACK_DATA, value.callbackData)

            is WebApp ->
                out.encodeSerializableElement(descriptor, IDX_WEB_APP, WebAppInfo.serializer(), value.webApp)

            is ILoginUrl ->
                out.encodeSerializableElement(descriptor, IDX_LOGIN_URL, LoginUrl.serializer(), value.loginUrl)

            is SwitchInline ->
                out.encodeStringElement(descriptor, IDX_SWITCH_INLINE_QUERY, value.switchInlineQuery)

            is SwitchCurrent ->
                out.encodeStringElement(descriptor, IDX_SWITCH_INLINE_QUERY_CURRENT_CHAT, value.switchInlineQueryCurrentChat)

            is SwitchChosen ->
                out.encodeSerializableElement(
                    descriptor, IDX_SWITCH_INLINE_QUERY_CHOSEN_CHAT,
                    SwitchInlineQueryChosenChat.serializer(), value.switchInlineQueryChosenChat
                )

            is CopyText ->
                out.encodeSerializableElement(descriptor, IDX_COPY_TEXT, CopyTextButton.serializer(), value.copyText)

            is ICallbackGame ->
                out.encodeSerializableElement(descriptor, IDX_CALLBACK_GAME, CallbackGame.serializer(), value.callbackGame)

            is Pay ->
                out.encodeBooleanElement(descriptor, IDX_PAY, true)
        }

        out.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): InlineKeyboardButton {
        val structure = decoder.beginStructure(descriptor)

        var text: String? = null

        while (true) {
            when (val elIndex = structure.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break

                IDX_TEXT -> text = structure.decodeStringElement(descriptor, IDX_TEXT)

                IDX_URL -> {
                    val v = structure.decodeStringElement(descriptor, IDX_URL)
                    structure.endStructure(descriptor)
                    return Url(text!!, v)
                }

                IDX_CALLBACK_DATA -> {
                    val v = structure.decodeStringElement(descriptor, IDX_CALLBACK_DATA)
                    structure.endStructure(descriptor)
                    return Callback(text!!, v)
                }

                IDX_WEB_APP -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_WEB_APP, WebAppInfo.serializer())
                    structure.endStructure(descriptor)
                    return WebApp(text!!, v)
                }

                IDX_LOGIN_URL -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_LOGIN_URL, LoginUrl.serializer())
                    structure.endStructure(descriptor)
                    return ILoginUrl(text!!, v)
                }

                IDX_SWITCH_INLINE_QUERY -> {
                    val v = structure.decodeStringElement(descriptor, IDX_SWITCH_INLINE_QUERY)
                    structure.endStructure(descriptor)
                    return SwitchInline(text!!, v)
                }

                IDX_SWITCH_INLINE_QUERY_CURRENT_CHAT -> {
                    val v = structure.decodeStringElement(descriptor, IDX_SWITCH_INLINE_QUERY_CURRENT_CHAT)
                    structure.endStructure(descriptor)
                    return SwitchCurrent(text!!, v)
                }

                IDX_SWITCH_INLINE_QUERY_CHOSEN_CHAT -> {
                    val v = structure.decodeSerializableElement(
                        descriptor, IDX_SWITCH_INLINE_QUERY_CHOSEN_CHAT, SwitchInlineQueryChosenChat.serializer()
                    )
                    structure.endStructure(descriptor)
                    return SwitchChosen(text!!, v)
                }

                IDX_COPY_TEXT -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_COPY_TEXT, CopyTextButton.serializer())
                    structure.endStructure(descriptor)
                    return CopyText(text!!, v)
                }

                IDX_CALLBACK_GAME -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_CALLBACK_GAME, CallbackGame.serializer())
                    structure.endStructure(descriptor)
                    return ICallbackGame(text!!, v)
                }

                IDX_PAY -> {
                    val v = structure.decodeBooleanElement(descriptor, IDX_PAY)
                    if (!v) error("`pay` must be true when present")
                    structure.endStructure(descriptor)
                    return Pay(text!!)
                }

                else -> error("Unexpected index: $elIndex")
            }
        }

        structure.endStructure(descriptor)
        error("No valid data found")
    }
}