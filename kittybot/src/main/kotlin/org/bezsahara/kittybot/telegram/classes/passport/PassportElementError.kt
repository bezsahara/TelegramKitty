package org.bezsahara.kittybot.telegram.classes.passport

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorFrontSide
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorTranslationFile
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorDataField
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementError
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorTranslationFiles
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorUnspecified
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorSelfie
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorFiles
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorReverseSide
import org.bezsahara.kittybot.telegram.classes.passport.PassportElementErrorFile
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = PassportElementErrorSerializer::class)
sealed interface PassportElementError {
    val source: String
}


private object PassportElementErrorSerializer : JsonContentPolymorphicSerializer<PassportElementError>(PassportElementError::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<PassportElementError> {
        return when (element.jsonObject["source"]!!.jsonPrimitive.content) {
            "data" -> PassportElementErrorDataField.serializer()
            "front_side" -> PassportElementErrorFrontSide.serializer()
            "reverse_side" -> PassportElementErrorReverseSide.serializer()
            "selfie" -> PassportElementErrorSelfie.serializer()
            "file" -> PassportElementErrorFile.serializer()
            "files" -> PassportElementErrorFiles.serializer()
            "translation_file" -> PassportElementErrorTranslationFile.serializer()
            "translation_files" -> PassportElementErrorTranslationFiles.serializer()
            "unspecified" -> PassportElementErrorUnspecified.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["source"]!!.jsonPrimitive.content}")
        }
    }
}



