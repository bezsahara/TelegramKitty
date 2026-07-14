package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.bezsahara.kittybot.telegram.classes.rich.RichTextSerializer.A.listSerializer


internal object RichTextSerializer : KSerializer<RichText> {
    internal object A {
        internal val listSerializer = ListSerializer(RichTextSerializer)
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RichText")

    override fun serialize(encoder: Encoder, value: RichText) {
        when (value) {
            is RichTextPlain -> encoder.encodeString(value.value)
            is RichTextArray -> listSerializer.serialize(encoder, value.value)
            is RichTextBold -> RichTextBold.serializer().serialize(encoder, value)
            is RichTextItalic -> RichTextItalic.serializer().serialize(encoder, value)
            is RichTextUnderline -> RichTextUnderline.serializer().serialize(encoder, value)
            is RichTextStrikethrough -> RichTextStrikethrough.serializer().serialize(encoder, value)
            is RichTextSpoiler -> RichTextSpoiler.serializer().serialize(encoder, value)
            is RichTextDateTime -> RichTextDateTime.serializer().serialize(encoder, value)
            is RichTextTextMention -> RichTextTextMention.serializer().serialize(encoder, value)
            is RichTextSubscript -> RichTextSubscript.serializer().serialize(encoder, value)
            is RichTextSuperscript -> RichTextSuperscript.serializer().serialize(encoder, value)
            is RichTextMarked -> RichTextMarked.serializer().serialize(encoder, value)
            is RichTextCode -> RichTextCode.serializer().serialize(encoder, value)
            is RichTextCustomEmoji -> RichTextCustomEmoji.serializer().serialize(encoder, value)
            is RichTextMathematicalExpression -> RichTextMathematicalExpression.serializer().serialize(encoder, value)
            is RichTextUrl -> RichTextUrl.serializer().serialize(encoder, value)
            is RichTextEmailAddress -> RichTextEmailAddress.serializer().serialize(encoder, value)
            is RichTextPhoneNumber -> RichTextPhoneNumber.serializer().serialize(encoder, value)
            is RichTextBankCardNumber -> RichTextBankCardNumber.serializer().serialize(encoder, value)
            is RichTextMention -> RichTextMention.serializer().serialize(encoder, value)
            is RichTextHashtag -> RichTextHashtag.serializer().serialize(encoder, value)
            is RichTextCashtag -> RichTextCashtag.serializer().serialize(encoder, value)
            is RichTextBotCommand -> RichTextBotCommand.serializer().serialize(encoder, value)
            is RichTextAnchor -> RichTextAnchor.serializer().serialize(encoder, value)
            is RichTextAnchorLink -> RichTextAnchorLink.serializer().serialize(encoder, value)
            is RichTextReference -> RichTextReference.serializer().serialize(encoder, value)
            is RichTextReferenceLink -> RichTextReferenceLink.serializer().serialize(encoder, value)
            else -> throw SerializationException("Unknown RichText subtype ${value::class}")
        }
    }

    override fun deserialize(decoder: Decoder): RichText {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("RichText serializer supports only JSON")
        return when (val element = input.decodeJsonElement()) {
            is JsonPrimitive -> RichTextPlain(element.content)
            is JsonArray -> RichTextArray(element.map { input.json.decodeFromJsonElement(RichTextSerializer, it) })
            is JsonObject -> when (val type = element["type"]?.jsonPrimitive?.content
                ?: throw SerializationException("Missing type while deserializing RichText")) {
                    "bold" -> input.json.decodeFromJsonElement(RichTextBold.serializer(), element)
                    "italic" -> input.json.decodeFromJsonElement(RichTextItalic.serializer(), element)
                    "underline" -> input.json.decodeFromJsonElement(RichTextUnderline.serializer(), element)
                    "strikethrough" -> input.json.decodeFromJsonElement(RichTextStrikethrough.serializer(), element)
                    "spoiler" -> input.json.decodeFromJsonElement(RichTextSpoiler.serializer(), element)
                    "date_time" -> input.json.decodeFromJsonElement(RichTextDateTime.serializer(), element)
                    "text_mention" -> input.json.decodeFromJsonElement(RichTextTextMention.serializer(), element)
                    "subscript" -> input.json.decodeFromJsonElement(RichTextSubscript.serializer(), element)
                    "superscript" -> input.json.decodeFromJsonElement(RichTextSuperscript.serializer(), element)
                    "marked" -> input.json.decodeFromJsonElement(RichTextMarked.serializer(), element)
                    "code" -> input.json.decodeFromJsonElement(RichTextCode.serializer(), element)
                    "custom_emoji" -> input.json.decodeFromJsonElement(RichTextCustomEmoji.serializer(), element)
                    "mathematical_expression" -> input.json.decodeFromJsonElement(RichTextMathematicalExpression.serializer(), element)
                    "url" -> input.json.decodeFromJsonElement(RichTextUrl.serializer(), element)
                    "email_address" -> input.json.decodeFromJsonElement(RichTextEmailAddress.serializer(), element)
                    "phone_number" -> input.json.decodeFromJsonElement(RichTextPhoneNumber.serializer(), element)
                    "bank_card_number" -> input.json.decodeFromJsonElement(RichTextBankCardNumber.serializer(), element)
                    "mention" -> input.json.decodeFromJsonElement(RichTextMention.serializer(), element)
                    "hashtag" -> input.json.decodeFromJsonElement(RichTextHashtag.serializer(), element)
                    "cashtag" -> input.json.decodeFromJsonElement(RichTextCashtag.serializer(), element)
                    "bot_command" -> input.json.decodeFromJsonElement(RichTextBotCommand.serializer(), element)
                    "anchor" -> input.json.decodeFromJsonElement(RichTextAnchor.serializer(), element)
                    "anchor_link" -> input.json.decodeFromJsonElement(RichTextAnchorLink.serializer(), element)
                    "reference" -> input.json.decodeFromJsonElement(RichTextReference.serializer(), element)
                    "reference_link" -> input.json.decodeFromJsonElement(RichTextReferenceLink.serializer(), element)
                    else -> throw SerializationException("Serializer wasn't found for RichText with type $type")
                }
        }
    }
}
