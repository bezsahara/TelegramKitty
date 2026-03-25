package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
@JvmField
internal val jsonInstance = Json {
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
}

