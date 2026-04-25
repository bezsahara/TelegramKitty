package org.bezsahara.kittybot.telegram.classes.core.update

import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType


// In case telegram decides to create new update type
data class UnknownUpdate(
    override val updateId: Long,
    val jsonObject: JsonObject
) : Update() {
    override val ordinal: Int
        get() = 24
    override fun chatIdOrNull(): ChatId? = null
    override fun userIdOrNull(): ChatId? = null

    fun asJsonString(): String {
        return jsonObject.toString()
    }

    companion object : UpdateKind<UnknownUpdate>(
        24, UnknownUpdate::class.java, "UnknownUpdate"
    )
}


internal object DecoderHandles {
    private val lookup = MethodHandles.lookup()

    @JvmField
    val success: Boolean

    private val decoderClass: Class<*>
    private val lexerClass: Class<*>
    private val stringLexerClass: Class<*>
    private val stringLexerFactoryHolderClass: Class<*>
    private val writeModeClass: Class<*>
    private val discriminatorHolderClass: Class<*>

    private object None {
        val c = this::class.java
    }

    init {
        val a1 = loadClass("kotlinx.serialization.json.internal.StreamingJsonDecoder")
        val a2 = loadClass("kotlinx.serialization.json.internal.AbstractJsonLexer")
        val a3 = loadClass("kotlinx.serialization.json.internal.StringJsonLexer")
        val a4 = loadClass("kotlinx.serialization.json.internal.StringJsonLexerKt")
        val a5 = loadClass("kotlinx.serialization.json.internal.WriteMode")
        val a6 = loadClass("kotlinx.serialization.json.internal.StreamingJsonDecoder\$DiscriminatorHolder")

        if (a1 == null || a2 == null || a3 == null || a4 == null || a5 == null || a6 == null) {
            success = false
            lexerClass = None.c
            decoderClass = None.c
            stringLexerClass = None.c
            stringLexerFactoryHolderClass = None.c
            writeModeClass = None.c
            discriminatorHolderClass = None.c
        } else {
            decoderClass = a1
            lexerClass = a2
            stringLexerClass = a3
            stringLexerFactoryHolderClass = a4
            writeModeClass = a5
            discriminatorHolderClass = a6
            success = true
        }
    }

    private val decoderLexerHandle: MethodHandle = decoderClass.let { target ->
        MethodHandles.privateLookupIn(target, lookup).findGetter(
            target,
            "lexer",
            lexerClass
        )
    }

    private val lexerSourceHandle: MethodHandle = lexerClass.let { target ->
        MethodHandles.privateLookupIn(target, lookup).findVirtual(
            target,
            "getSource",
            MethodType.methodType(CharSequence::class.java)
        )
    }

    private val lexerCurrentPositionSetterHandle: MethodHandle = lexerClass.let { target ->
        MethodHandles.privateLookupIn(target, lookup).findSetter(
            target,
            "currentPosition",
            Int::class.javaPrimitiveType
        )
    }

    private val lexerCurrentPositionHandle: MethodHandle = lexerClass.let { target ->
        MethodHandles.privateLookupIn(target, lookup).findGetter(
            target,
            "currentPosition",
            Int::class.javaPrimitiveType
        )
    }

    private val freshLexerFactoryHandle: MethodHandle = MethodHandles.privateLookupIn(stringLexerFactoryHolderClass, lookup).findStatic(
        stringLexerFactoryHolderClass,
        "StringJsonLexer",
        MethodType.methodType(stringLexerClass, Json::class.java, String::class.java)
    )

    private val writeModeObjHandle: MethodHandle = MethodHandles.privateLookupIn(writeModeClass, lookup).findStaticGetter(
        writeModeClass,
        "OBJ",
        writeModeClass
    )

    private val freshDecoderCtorHandle: MethodHandle = MethodHandles.privateLookupIn(decoderClass, lookup).findConstructor(
        decoderClass,
        MethodType.methodType(
            Void.TYPE,
            Json::class.java,
            writeModeClass,
            lexerClass,
            SerialDescriptor::class.java,
            discriminatorHolderClass
        )
    )

    fun decoderLexer(decoder: Decoder): Any? {
        return if (decoderClass.isInstance(decoder)) {
            decoderLexerHandle.invoke(decoder)
        } else {
            null
        }
    }

    fun decoderSource(decoder: Decoder): String? {
        val lexer = decoderLexer(decoder) ?: return null
        return lexerSource(lexer) as? String
    }

    fun lexerSource(lexer: Any): CharSequence {
        return lexerSourceHandle.invoke(lexer) as CharSequence
    }

    fun lexerCurrentPosition(lexer: Any): Int {
        return lexerCurrentPositionHandle.invoke(lexer) as Int
    }

    fun lexerCurrentPosition(lexer: Any, value: Int) {
        lexerCurrentPositionSetterHandle.invoke(lexer, value)
    }

    @JvmStatic
    @Suppress("INVISIBLE_REFERENCE")
    fun decoderCurrentPosition(decoder: Decoder): Int {
        if (!success) return Int.MIN_VALUE
//        val lexer = decoderLexer(decoder) ?: return Int.MIN_VALUE
//        return lexerCurrentPosition(lexer)
        if (decoder is kotlinx.serialization.json.internal.StreamingJsonDecoder) {
            return decoder.lexer.currentPosition
        }
        return Int.MIN_VALUE
    }

    fun decoderCurrentPosition(decoder: Decoder, value: Int): Boolean {
        val lexer = decoderLexer(decoder) ?: return false
        lexerCurrentPosition(lexer, value)
        return true
    }

    fun createFreshLexer(json: Json, source: String): Any? {
        if (!success) return null
        return freshLexerFactoryHandle.invoke(json, source)
    }

    fun createFreshDecoder(
        json: Json,
        lexer: Any,
        descriptor: SerialDescriptor
    ): JsonDecoder? {
        if (!success) return null
        val writeModeObj = writeModeObjHandle.invoke()
        return freshDecoderCtorHandle.invoke(json, writeModeObj, lexer, descriptor, null) as? JsonDecoder
    }

    private fun loadClass(name: String): Class<*>? {
        return try {
            Class.forName(name)
        } catch (_: Throwable) {
            null
        }
    }
}
