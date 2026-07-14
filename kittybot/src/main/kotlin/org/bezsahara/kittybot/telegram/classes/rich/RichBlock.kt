package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Serializable(with = RichBlockSerializer::class)
sealed interface RichBlock {
    val type: String
}


private object RichBlockSerializer : JsonContentPolymorphicSerializer<RichBlock>(RichBlock::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<RichBlock> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "paragraph" -> RichBlockParagraph.serializer()
            "heading" -> RichBlockSectionHeading.serializer()
            "pre" -> RichBlockPreformatted.serializer()
            "footer" -> RichBlockFooter.serializer()
            "divider" -> RichBlockDivider.serializer()
            "mathematical_expression" -> RichBlockMathematicalExpression.serializer()
            "anchor" -> RichBlockAnchor.serializer()
            "list" -> RichBlockList.serializer()
            "blockquote" -> RichBlockBlockQuotation.serializer()
            "pullquote" -> RichBlockPullQuotation.serializer()
            "collage" -> RichBlockCollage.serializer()
            "slideshow" -> RichBlockSlideshow.serializer()
            "table" -> RichBlockTable.serializer()
            "details" -> RichBlockDetails.serializer()
            "map" -> RichBlockMap.serializer()
            "animation" -> RichBlockAnimation.serializer()
            "audio" -> RichBlockAudio.serializer()
            "photo" -> RichBlockPhoto.serializer()
            "video" -> RichBlockVideo.serializer()
            "voice_note" -> RichBlockVoiceNote.serializer()
            "thinking" -> RichBlockThinking.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}
