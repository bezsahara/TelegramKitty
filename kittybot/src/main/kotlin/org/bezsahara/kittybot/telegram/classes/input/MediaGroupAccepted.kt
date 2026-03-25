package org.bezsahara.kittybot.telegram.classes.input

import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import kotlin.Unit
import kotlinx.serialization.Serializable


@Serializable
sealed interface MediaGroupAccepted {
    suspend fun executeAll(
        builder: MultiPartBuilder
    ) 
    suspend fun executeAll(
        builder: CustomMPB
    ) 
}

