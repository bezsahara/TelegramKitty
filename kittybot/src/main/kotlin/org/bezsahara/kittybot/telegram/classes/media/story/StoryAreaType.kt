package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaTypeWeather
import org.bezsahara.kittybot.telegram.values.StoryAreaTypeKind
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaTypeUniqueGift
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaType
import kotlinx.serialization.json.JsonElement
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaTypeLocation
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaTypeSuggestedReaction
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaTypeLink
import kotlinx.serialization.Serializable


@Serializable(with = StoryAreaTypeSerializer::class)
sealed interface StoryAreaType {
    val type: StoryAreaTypeKind
}


private object StoryAreaTypeSerializer : JsonContentPolymorphicSerializer<StoryAreaType>(StoryAreaType::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<StoryAreaType> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "location" -> StoryAreaTypeLocation.serializer()
            "suggested_reaction" -> StoryAreaTypeSuggestedReaction.serializer()
            "link" -> StoryAreaTypeLink.serializer()
            "weather" -> StoryAreaTypeWeather.serializer()
            "unique_gift" -> StoryAreaTypeUniqueGift.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



