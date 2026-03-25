package org.bezsahara.kittybot.telegram.classes.keyboard

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo

object KBSerializer : KSerializer<KeyboardButton> {
    private const val IDX_TEXT = 0
    private const val IDX_REQUEST_USERS = 1
    private const val IDX_REQUEST_CHAT = 2
    private const val IDX_REQUEST_CONTACT = 3
    private const val IDX_REQUEST_LOCATION = 4
    private const val IDX_REQUEST_POLL = 5
    private const val IDX_WEB_APP = 6

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("KeyboardButton") {
            element<String>("text")                                   // 0
            element<KeyboardButtonRequestUsers?>("request_users", isOptional = true) // 1
            element<KeyboardButtonRequestChat?>("request_chat", isOptional = true)   // 2
            element<Boolean?>("request_contact", isOptional = true)                  // 3
            element<Boolean?>("request_location", isOptional = true)                 // 4
            element<KeyboardButtonPollType?>("request_poll", isOptional = true)      // 5
            element<WebAppInfo?>("web_app", isOptional = true)                       // 6
        }

    override fun serialize(encoder: Encoder, value: KeyboardButton) {
        val out = encoder.beginStructure(descriptor)
        out.encodeStringElement(descriptor, IDX_TEXT, value.text)
        when (value) {
            is KeyboardButton.Text ->
            { /* no optional field */ }

            is KeyboardButton.RequestUsers ->
                out.encodeSerializableElement(descriptor, IDX_REQUEST_USERS, KeyboardButtonRequestUsers.serializer(), value.requestUsers)

            is KeyboardButton.RequestChat ->
                out.encodeSerializableElement(descriptor, IDX_REQUEST_CHAT, KeyboardButtonRequestChat.serializer(), value.requestChat)

            is KeyboardButton.RequestContact ->
                out.encodeBooleanElement(descriptor, IDX_REQUEST_CONTACT, true)

            is KeyboardButton.RequestLocation ->
                out.encodeBooleanElement(descriptor, IDX_REQUEST_LOCATION, true)

            is KeyboardButton.RequestPoll ->
                out.encodeSerializableElement(descriptor, IDX_REQUEST_POLL, KeyboardButtonPollType.serializer(), value.requestPoll)

            is KeyboardButton.WebApp ->
                out.encodeSerializableElement(descriptor, IDX_WEB_APP, WebAppInfo.serializer(), value.webApp)
        }
        out.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): KeyboardButton {
        val structure = decoder.beginStructure(descriptor)

        lateinit var text: String

        while (true) {
            when (val elIndex = structure.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break

                IDX_TEXT -> text = structure.decodeStringElement(descriptor, IDX_TEXT)

                IDX_REQUEST_USERS -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_REQUEST_USERS, KeyboardButtonRequestUsers.serializer())
                    structure.endStructure(descriptor)
                    return KeyboardButton.RequestUsers(text, v)
                }

                IDX_REQUEST_CHAT -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_REQUEST_CHAT, KeyboardButtonRequestChat.serializer())
                    structure.endStructure(descriptor)
                    return KeyboardButton.RequestChat(text, v)
                }

                IDX_REQUEST_CONTACT -> {
                    val v = structure.decodeBooleanElement(descriptor, IDX_REQUEST_CONTACT)
                    if (!v) error("`request_contact` must be true when present")
                    structure.endStructure(descriptor)
                    return KeyboardButton.RequestContact(text)
                }

                IDX_REQUEST_LOCATION -> {
                    val v = structure.decodeBooleanElement(descriptor, IDX_REQUEST_LOCATION)
                    if (!v) error("`request_location` must be true when present")
                    structure.endStructure(descriptor)
                    return KeyboardButton.RequestLocation(text)
                }

                IDX_REQUEST_POLL -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_REQUEST_POLL, KeyboardButtonPollType.serializer())
                    structure.endStructure(descriptor)
                    return KeyboardButton.RequestPoll(text, v)
                }

                IDX_WEB_APP -> {
                    val v = structure.decodeSerializableElement(descriptor, IDX_WEB_APP, WebAppInfo.serializer())
                    structure.endStructure(descriptor)
                    return KeyboardButton.WebApp(text, v)
                }

                else -> error("Unexpected index: $elIndex")
            }
        }

        structure.endStructure(descriptor)
        return KeyboardButton.Text(text)
    }
}
