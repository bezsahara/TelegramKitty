package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder


@Serializable(with = InputMediaSerializer::class)
sealed interface InputMedia {
    val type: String
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}

