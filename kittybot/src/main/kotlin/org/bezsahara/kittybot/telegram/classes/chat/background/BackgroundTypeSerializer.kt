package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.json.opt.IntMask
import org.bezsahara.kittybot.telegram.classes.media.Document


internal object BackgroundTypeSerializer : KSerializer<BackgroundType> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BackgroundType") {
        element<String>("type")
        element<BackgroundFill>("fill", isOptional = true)
        element<Long>("dark_theme_dimming", isOptional = true)
        element<Document>("document", isOptional = true)
        element<Boolean?>("is_blurred", isOptional = true)
        element<Boolean?>("is_moving", isOptional = true)
        element<Long>("intensity", isOptional = true)
        element<Boolean?>("is_inverted", isOptional = true)
        element<String>("theme_name", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: BackgroundType) {
        when (value) {
            is BackgroundTypeFill -> BackgroundTypeFill.serializer().serialize(encoder, value)
            is BackgroundTypeWallpaper -> BackgroundTypeWallpaper.serializer().serialize(encoder, value)
            is BackgroundTypePattern -> BackgroundTypePattern.serializer().serialize(encoder, value)
            is BackgroundTypeChatTheme -> BackgroundTypeChatTheme.serializer().serialize(encoder, value)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): BackgroundType {
        var nativeMask = IntMask.EMPTY
        var type: String? = null
        var fill: BackgroundFill? = null
        var darkThemeDimming: Long = 0L
        var document: Document? = null
        var isBlurred: Boolean? = null
        var isMoving: Boolean? = null
        var intensity: Long = 0L
        var isInverted: Boolean? = null
        var themeName: String? = null
        val dec = decoder.beginStructure(descriptor)
        decodeLoop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> type = dec.decodeStringElement(descriptor, 0)
                1 -> fill = dec.decodeSerializableElement(descriptor, 1, BackgroundFill.serializer())
                2 -> {
                    darkThemeDimming = dec.decodeLongElement(descriptor, 2)
                    nativeMask = nativeMask.setBit(0)
                }
                3 -> document = dec.decodeSerializableElement(descriptor, 3, Document.serializer())
                4 -> isBlurred = dec.decodeNullableSerializableElement(descriptor, 4, Boolean.serializer(), null)
                5 -> isMoving = dec.decodeNullableSerializableElement(descriptor, 5, Boolean.serializer(), null)
                6 -> {
                    intensity = dec.decodeLongElement(descriptor, 6)
                    nativeMask = nativeMask.setBit(1)
                }
                7 -> isInverted = dec.decodeNullableSerializableElement(descriptor, 7, Boolean.serializer(), null)
                8 -> themeName = dec.decodeStringElement(descriptor, 8)
                CompositeDecoder.DECODE_DONE -> break@decodeLoop
                else -> throw SerializationException("Unexpected index $index while deserializing BackgroundType")
            }
        }
        dec.endStructure(descriptor)
        return when (type ?: throwMissingField("type")) {
            "fill" -> BackgroundTypeFill(
                fill = fill ?: throwMissingField("fill"),
                darkThemeDimming = if (nativeMask.has(0)) darkThemeDimming else throwMissingField("dark_theme_dimming")
            )
            "wallpaper" -> BackgroundTypeWallpaper(
                document = document ?: throwMissingField("document"),
                darkThemeDimming = if (nativeMask.has(0)) darkThemeDimming else throwMissingField("dark_theme_dimming"),
                isBlurred = isBlurred,
                isMoving = isMoving
            )
            "pattern" -> BackgroundTypePattern(
                document = document ?: throwMissingField("document"),
                fill = fill ?: throwMissingField("fill"),
                intensity = if (nativeMask.has(1)) intensity else throwMissingField("intensity"),
                isInverted = isInverted,
                isMoving = isMoving
            )
            "chat_theme" -> BackgroundTypeChatTheme(
                themeName = themeName ?: throwMissingField("theme_name")
            )
            else -> throw SerializationException("Serializer wasn't found for BackgroundType with type $type")
        }
    }

    private inline fun throwMissingField(name: String): Nothing {
        throw SerializationException("Missing required field '$name' while deserializing BackgroundType")
    }
}
