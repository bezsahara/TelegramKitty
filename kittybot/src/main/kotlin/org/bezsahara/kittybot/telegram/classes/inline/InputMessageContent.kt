package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable(with = InputMessageContentSerializer::class)
sealed interface InputMessageContent

