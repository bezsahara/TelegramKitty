package org.bezsahara.kittybot.other;

import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonDecoder;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.internal.AbstractJsonLexer;
import kotlinx.serialization.json.internal.StreamingJsonDecoder;
import kotlinx.serialization.json.internal.StringJsonLexerKt;
import kotlinx.serialization.json.internal.WriteMode;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class KotlinxJsonAccessors {
    private static final MethodHandle LEXER_SOURCE_HANDLE = findLexerSourceHandle();

    private KotlinxJsonAccessors() {
    }

    public static AbstractJsonLexer decoderLexer(Decoder decoder) {
        if (decoder instanceof StreamingJsonDecoder) {
            return ((StreamingJsonDecoder) decoder).lexer;
        }
        return null;
    }

    public static String decoderSource(Decoder decoder) {
        AbstractJsonLexer lexer = decoderLexer(decoder);
        if (lexer == null) {
            return null;
        }

        CharSequence source = lexerSource(lexer);
        return source instanceof String ? (String) source : source.toString();
    }

    public static CharSequence lexerSource(AbstractJsonLexer lexer) {
        try {
            return (CharSequence) LEXER_SOURCE_HANDLE.invoke(lexer);
        } catch (RuntimeException | Error exception) {
            throw exception;
        } catch (Throwable exception) {
            throw new IllegalStateException("Could not read AbstractJsonLexer.source.", exception);
        }
    }

    public static void lexerCurrentPosition(AbstractJsonLexer lexer, int value) {
        lexer.currentPosition = value;
    }

    public static int getPos(Decoder decoder) {
        if (decoder instanceof StreamingJsonDecoder) {
            return ((StreamingJsonDecoder) decoder).lexer.currentPosition;
        }
        return Integer.MIN_VALUE;
    }

    public static boolean decoderCurrentPosition(Decoder decoder, int value) {
        AbstractJsonLexer lexer = decoderLexer(decoder);
        if (lexer == null) {
            return false;
        }

        lexerCurrentPosition(lexer, value);
        return true;
    }

    public static AbstractJsonLexer createFreshLexer(Json json, String source) {
        return StringJsonLexerKt.StringJsonLexer(json, source);
    }

    public static JsonDecoder createFreshDecoder(
            Json json,
            AbstractJsonLexer lexer,
            SerialDescriptor descriptor
    ) {
        return new StreamingJsonDecoder(json, WriteMode.OBJ, lexer, descriptor, null);
    }

    public static JsonObject parseToJsonObject(
            Json json,
            Decoder decoder,
            int startPos,
            SerialDescriptor descriptor
    ) {
        String source = decoderSource(decoder);
        if (source == null) {
            return null;
        }

        AbstractJsonLexer lexer = createFreshLexer(json, source);
        lexerCurrentPosition(lexer, startPos);

        JsonDecoder freshDecoder = createFreshDecoder(json, lexer, descriptor);
        JsonElement element = freshDecoder.decodeJsonElement();
        decoderCurrentPosition(decoder, lexer.currentPosition);

        return element instanceof JsonObject ? (JsonObject) element : null;
    }

    private static MethodHandle findLexerSourceHandle() {
        try {
            return MethodHandles
                    .privateLookupIn(AbstractJsonLexer.class, MethodHandles.lookup())
                    .findVirtual(
                            AbstractJsonLexer.class,
                            "getSource",
                            MethodType.methodType(CharSequence.class)
                    );
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
}
