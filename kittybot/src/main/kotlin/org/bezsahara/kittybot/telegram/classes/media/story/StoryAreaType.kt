package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.Serializable


@Serializable(with = StoryAreaTypeSerializer::class)
sealed interface StoryAreaType {
    val type: String
}

