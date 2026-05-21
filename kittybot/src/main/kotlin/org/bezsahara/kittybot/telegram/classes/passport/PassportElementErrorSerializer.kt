package org.bezsahara.kittybot.telegram.classes.passport

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.telegram.values.PassportElementType


internal object PassportElementErrorSerializer : KSerializer<PassportElementError> {
    private val listSerializer0 = ListSerializer(String.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PassportElementError") {
        element<String>("source")
        element<PassportElementType>("type")
        element<String>("field_name", isOptional = true)
        element<String>("data_hash", isOptional = true)
        element<String>("message")
        element<String>("file_hash", isOptional = true)
        element<List<String>>("file_hashes", isOptional = true)
        element<String>("element_hash", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: PassportElementError) {
        when (value) {
            is PassportElementErrorDataField -> PassportElementErrorDataField.serializer().serialize(encoder, value)
            is PassportElementErrorFrontSide -> PassportElementErrorFrontSide.serializer().serialize(encoder, value)
            is PassportElementErrorReverseSide -> PassportElementErrorReverseSide.serializer().serialize(encoder, value)
            is PassportElementErrorSelfie -> PassportElementErrorSelfie.serializer().serialize(encoder, value)
            is PassportElementErrorFile -> PassportElementErrorFile.serializer().serialize(encoder, value)
            is PassportElementErrorFiles -> PassportElementErrorFiles.serializer().serialize(encoder, value)
            is PassportElementErrorTranslationFile -> PassportElementErrorTranslationFile.serializer().serialize(encoder, value)
            is PassportElementErrorTranslationFiles -> PassportElementErrorTranslationFiles.serializer().serialize(encoder, value)
            is PassportElementErrorUnspecified -> PassportElementErrorUnspecified.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder): PassportElementError {
        var source: String? = null
        var type: PassportElementType? = null
        var fieldName: String? = null
        var dataHash: String? = null
        var message: String? = null
        var fileHash: String? = null
        var fileHashes: List<String>? = null
        var elementHash: String? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> source = dec.decodeStringElement(descriptor, 0)
                1 -> type = dec.decodeSerializableElement(descriptor, 1, PassportElementType.serializer())
                2 -> fieldName = dec.decodeStringElement(descriptor, 2)
                3 -> dataHash = dec.decodeStringElement(descriptor, 3)
                4 -> message = dec.decodeStringElement(descriptor, 4)
                5 -> fileHash = dec.decodeStringElement(descriptor, 5)
                6 -> fileHashes = dec.decodeSerializableElement(descriptor, 6, listSerializer0)
                7 -> elementHash = dec.decodeStringElement(descriptor, 7)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing PassportElementError")
            }
        }
        dec.endStructure(descriptor)
        return when (source ?: throwMissingField("source")) {
            "data" -> PassportElementErrorDataField(
                type = type ?: throwMissingField("type"),
                fieldName = fieldName ?: throwMissingField("field_name"),
                dataHash = dataHash ?: throwMissingField("data_hash"),
                message = message ?: throwMissingField("message")
            )
            "front_side" -> PassportElementErrorFrontSide(
                type = type ?: throwMissingField("type"),
                fileHash = fileHash ?: throwMissingField("file_hash"),
                message = message ?: throwMissingField("message")
            )
            "reverse_side" -> PassportElementErrorReverseSide(
                type = type ?: throwMissingField("type"),
                fileHash = fileHash ?: throwMissingField("file_hash"),
                message = message ?: throwMissingField("message")
            )
            "selfie" -> PassportElementErrorSelfie(
                type = type ?: throwMissingField("type"),
                fileHash = fileHash ?: throwMissingField("file_hash"),
                message = message ?: throwMissingField("message")
            )
            "file" -> PassportElementErrorFile(
                type = type ?: throwMissingField("type"),
                fileHash = fileHash ?: throwMissingField("file_hash"),
                message = message ?: throwMissingField("message")
            )
            "files" -> PassportElementErrorFiles(
                type = type ?: throwMissingField("type"),
                fileHashes = fileHashes ?: throwMissingField("file_hashes"),
                message = message ?: throwMissingField("message")
            )
            "translation_file" -> PassportElementErrorTranslationFile(
                type = type ?: throwMissingField("type"),
                fileHash = fileHash ?: throwMissingField("file_hash"),
                message = message ?: throwMissingField("message")
            )
            "translation_files" -> PassportElementErrorTranslationFiles(
                type = type ?: throwMissingField("type"),
                fileHashes = fileHashes ?: throwMissingField("file_hashes"),
                message = message ?: throwMissingField("message")
            )
            "unspecified" -> PassportElementErrorUnspecified(
                type = type ?: throwMissingField("type"),
                elementHash = elementHash ?: throwMissingField("element_hash"),
                message = message ?: throwMissingField("message")
            )
            else -> throw SerializationException("Serializer wasn't found for PassportElementError with source $source")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing PassportElementError")
    }
}
