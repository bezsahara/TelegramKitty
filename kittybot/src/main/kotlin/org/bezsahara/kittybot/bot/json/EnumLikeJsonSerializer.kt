package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bezsahara.kittybot.bot.errors.KittyException
import java.util.EnumMap

abstract class EnumLike<E: Enum<*>> {
    abstract val value: String
    abstract val known: E?

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other === null) return false
        if (other.javaClass !== this.javaClass) return false
        return value.equals((other as EnumLike<*>).value)
    }
}

abstract class ResolveEnumLike<T : EnumLike<*>> {
    abstract fun resolve(value: String): T
}

abstract class ResolveEnumLikeBig<T : EnumLike<E>, E: Enum<E>>(enumClass: Class<E>) : ResolveEnumLike<T>() {
    private val map = hashMapOf<String, T>()
    private val enumMap = EnumMap<E, T>(enumClass)

    protected fun T.register(): T {
        map[value] = this
        enumMap[requireNotNull(known)] = this
        return this
    }

    internal abstract fun create(value: String): T

    internal fun mapGet(e: E): T {
        return enumMap[e] ?: throw KittyException("Unknown enum value $e")
    }

    override fun resolve(value: String): T {
        return map[value] ?: create(value)
    }
}

internal abstract class EnumLikeJsonSerializer<T : EnumLike<*>>(
    name: String,
    private val resolution: ResolveEnumLike<T>,
) : KSerializer<T> {
    final override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)

    final override fun serialize(
        encoder: Encoder,
        value: T,
    ) {
        encoder.encodeString(value.value)
    }

    final override fun deserialize(decoder: Decoder): T {
        return resolution.resolve(decoder.decodeString())
    }
}
