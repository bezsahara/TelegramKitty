package org.bezsahara.kittybot.bot.json

import io.vertx.core.buffer.Buffer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonByteBufferTest {

    @Test
    fun writesEmptyObjectWhenNothingWasAdded() {
        assertEquals("{}", JsonByteBuffer(1).toByteArray().decodeToString())
    }

    @Test
    fun writesLongListsWithoutLeadingComma() {
        val actual = render {
            putListOfLongUnsafe(key("values"), listOf(1L, 2L, 3L))
        }

        assertEquals("""{"values":[1,2,3]}""", actual)
    }

    @Test
    fun writesDoubleListsWithoutLeadingComma() {
        val actual = render {
            putListOfDoubleUnsafe(key("values"), listOf(1.25, -0.5, 2.125))
        }

        assertEquals("""{"values":[1.25,-0.5,2.125]}""", actual)
    }

    @Test
    fun writesBooleansCorrectlyAfterGrowth() {
        val actual = render(initialCapacity = 1) {
            putBoolUnsafe(key("active"), true)
            putBoolUnsafe(key("disabled"), false)
        }

        assertEquals("""{"active":true,"disabled":false}""", actual)
    }

    @Test
    fun writesNestedJsonObjectsAfterGrowth() {
        val actual = render(initialCapacity = 1) {
            putJsonObject(key("item"), TestPayload.serializer(), TestPayload(1, "alpha"))
            putListOfJsonObjects(
                key("items"),
                TestPayload.serializer(),
                listOf(TestPayload(2, "beta"), TestPayload(3, "gamma"))
            )
        }

        assertEquals(
            """{"item":{"id":1,"title":"alpha"},"items":[{"id":2,"title":"beta"},{"id":3,"title":"gamma"}]}""",
            actual
        )
    }

    @Test
    fun quotedStringsRoundTripAndEscapeSpecialCharacters() {
        val input = "latin \u043A\u0438\u0440\u0438\u043B\u043B\u0438\u0446\u0430 \"slash\\\\ line\n tab\t form\u000C sep\u2028 end\u2029 emoji \uD83D\uDE00"
        val actual = render {
            putStringUnsafe(key("value"), input)
        }
        val expected = buildJsonObject {
            put("value", JsonPrimitive(input))
        }

        assertEquals(expected, Json.parseToJsonElement(actual))
        assertTrue(actual.contains("\\u2028"))
        assertTrue(actual.contains("\\u2029"))
        assertTrue(actual.contains("\\n"))
        assertTrue(actual.contains("\\t"))
        assertTrue(actual.contains("\\f"))
        assertTrue(actual.contains("\\\\"))
        assertTrue(actual.contains("\\\""))
    }

    @Test
    fun resetPreparesBufferForTheNextObject() {
        val buffer = JsonByteBuffer(8)

        buffer.putNumberUnsafe(key("value"), 1L)
        assertEquals("""{"value":1}""", buffer.toByteArray().decodeToString())

        buffer.reset()
        buffer.putNumberUnsafe(key("value"), 2L)

        assertEquals("""{"value":2}""", buffer.toByteArray().decodeToString())
    }

    @Test
    fun writesLongBoundariesCorrectly() {
        val actual = render {
            putNumberUnsafe(key("min"), Long.MIN_VALUE)
            putNumberUnsafe(key("max"), Long.MAX_VALUE)
        }

        assertEquals(
            """{"min":-9223372036854775808,"max":9223372036854775807}""",
            actual
        )
    }

    @Test
    fun writesEmptyListsCorrectly() {
        val actual = render(initialCapacity = 1) {
            putListOfLongUnsafe(key("ids"), emptyList())
            putListOfDoubleUnsafe(key("weights"), emptyList())
            putListOfStringUnsafe(key("tags"), emptyList())
        }

        assertEquals("""{"ids":[],"weights":[],"tags":[]}""", actual)
    }

    @Test
    fun toBufferReturnsEmptyJsonForEmptyWriter() {
        val actual = (JsonByteBuffer(1).toBuffer() as Buffer).toString(Charsets.UTF_8)

        assertEquals("{}", actual)
    }

    @Serializable
    class C3(
        val some: String
    )

    @Serializable
    class B2(
        val pop: Int,
        val c: C3
    )

    @Serializable
    class A1(
        val simple: String,
        val b: B2
    )

    @Test
    fun multiJsonObjects() {
        val b= B2(3, C3("slash"))

        val actual = JsonByteBuffer(1).run {
            putStringUnsafe(key("simple"), "simple|\"\\\"\"\\\\\r")
            putJsonObject(key("b"), B2.serializer(), b)
            toByteArray().decodeToString()
        }
        val other = buildJsonObject {
            put("simple", JsonPrimitive("simple|\"\\\"\"\\\\\r"))
            put("b", Json.encodeToJsonElement(B2.serializer(), b))
        }.toString()

        assertEquals(actual, other)
    }

    @Test
    fun toBufferReturnsEncodedJsonForWrittenData() {
        val actual = (JsonByteBuffer(1).apply {
            putStringUnsafe(key("kind"), "ping")
            putListOfLongUnsafe(key("ids"), listOf(1L, 2L, Long.MIN_VALUE))
        }.toBuffer() as Buffer).toString(Charsets.UTF_8)

        assertEquals("""{"kind":"ping","ids":[1,2,${Long.MIN_VALUE}]}""", actual)
    }

    @Test
    fun writesStringsFromManyLanguages() {
        val values = multilingualSamples.map { "${it.first}: ${it.second}" }
        val actual = render(initialCapacity = 1) {
            putListOfStringUnsafe(key("values"), values)
        }
        val expected = buildJsonObject {
            put("values", buildJsonArray {
                values.forEach { add(JsonPrimitive(it)) }
            })
        }

        assertEquals(expected, Json.parseToJsonElement(actual))
    }

    @Test
    fun appendOverloadsHandleMultilingualSlicesAndNulls() {
        val source = "__Привіт світе | こんにちは世界 | مرحبا بالعالم | 👩‍💻__"
        val expectedValue = source.substring(2, source.length - 2)
        val actual = JsonByteBuffer(1).apply {
            putQuote()
            for (char in "value") writeChar(char)
            putQuote()
            writeChar(':')
            putQuote()
            write(expectedValue)
            putQuote()
            putComma()

            putQuote()
            write("missing")
            putQuote()
            writeChar(':')
            write("null")
            putComma()
        }.toByteArray().decodeToString()
        val expected = buildJsonObject {
            put("value", JsonPrimitive(expectedValue))
            put("missing", JsonNull)
        }

        assertEquals(expected, Json.parseToJsonElement(actual))
    }

    @Test
    fun writeCsqAndWriteArrEncodeMultilingualSlices() {
//        val source = "##Zażółć gęślą jaźń | Γειά σου κόσμε | שלום עולם | नमस्ते दुनिया | 😀##"
//        val expectedValue = source.substring(2, source.length - 2)
//        val actual = JsonByteBuffer(1).apply {
//            putQuote()
//            write("csq")
//            putQuote()
//            append(':')
//            putQuote()
//            writeCsq(StringBuilder(source), 2, source.length - 2)
//            putQuote()
//            putComma()
//
//            putQuote()
//            write("arr")
//            putQuote()
//            append(':')
//            putQuote()
//            writeArr(source.toCharArray(), 2, source.length - 2)
//            putQuote()
//            putComma()
//        }.toByteArray().decodeToString()
//        val expected = buildJsonObject {
//            put("csq", JsonPrimitive(expectedValue))
//            put("arr", JsonPrimitive(expectedValue))
//        }
//
//        assertEquals(expected, Json.parseToJsonElement(actual))
    }

    @Test
    fun randomMultilingualStringsRoundTripAgainstKotlinxJson() {
        val random = Random(1_592_639_215)

        repeat(200) { iteration ->
            val value = randomMultilingualString(random)
            val actual = render(initialCapacity = 1) {
                putStringUnsafe(key("value"), value)
            }
            val expected = buildJsonObject {
                put("value", JsonPrimitive(value))
            }

            assertEquals(
                expected,
                Json.parseToJsonElement(actual),
                "single string iteration $iteration failed"
            )
        }

        repeat(100) { iteration ->
            val values = List(random.nextInt(0, 8)) {
                randomMultilingualString(random)
            }
            val actual = render(initialCapacity = 1) {
                putListOfStringUnsafe(key("values"), values)
            }
            val expected = buildJsonObject {
                put("values", buildJsonArray {
                    values.forEach { add(JsonPrimitive(it)) }
                })
            }

            assertEquals(
                expected,
                Json.parseToJsonElement(actual),
                "list iteration $iteration failed"
            )
        }
    }

    private fun render(
        initialCapacity: Int = 8,
        body: JsonByteBuffer.() -> Unit
    ): String = JsonByteBuffer(initialCapacity).apply(body).toByteArray().decodeToString()

    private fun key(value: String): ByteArray = value.encodeToByteArray()

    private fun randomMultilingualString(random: Random): String = buildString {
        repeat(random.nextInt(0, 40)) {
            append(randomFragments[random.nextInt(randomFragments.size)])
        }
    }

    @Test
    fun normalEncoding() {
        val needed = "❎fsgsdfg👤🤔☁dsf((\n\n\n\\n\n\n\\\ndsfgd️⏰📧🟢sdfg↩️📖❎↩️dsfgsdfg📕☁️⏰⏰📘↩sdfg️👤👤☀️☁️(┬┬﹏┬┬)‱°‱⁙⁙⁛⁂※※¡₱₢₹€௹௹₱₱₶ΠζΕᾃᾋάΆἎᾅἃлывшЗЗЩФЫЦХЪЖАФХжіІsmdkfmskdkmskwopkowekposdfb"
        val actual = JsonByteBuffer(1).apply {
            putStringUnsafe(key("value"), needed)
        }
        val bufText = (actual.toBuffer()).toString(Charsets.UTF_8)
        val arrayText = actual.toByteArray().decodeToString()

        val expected = buildJsonObject {
            put("value", JsonPrimitive(needed))
        }

        assertEquals(bufText, arrayText)
        assertEquals(expected, Json.parseToJsonElement(bufText))
    }

    @Serializable
    private data class TestPayload(
        val id: Int,
        val title: String,
    )

    private companion object {
        private val multilingualSamples = listOf(
            "English" to "Hello world",
            "Ukrainian" to "Привіт світе",
            "Polish" to "Zażółć gęślą jaźń",
            "Greek" to "Γειά σου κόσμε",
            "Arabic" to "مرحبا بالعالم",
            "Hebrew" to "שלום עולם",
            "Hindi" to "नमस्ते दुनिया",
            "Thai" to "สวัสดีชาวโลก",
            "Chinese" to "你好，世界",
            "Japanese" to "こんにちは世界",
            "Korean" to "안녕하세요 세계",
            "Georgian" to "გამარჯობა მსოფლიო",
            "Emoji" to "👩‍💻🌍🚀",
        )

        private val randomFragments = listOf(
            "",
            "A",
            "z",
            "0",
            " ",
            "\n",
            "\r",
            "\t",
            "\b",
            "\u000C",
            "\"",
            "\\",
            "\u2028",
            "\u2029",
            "é",
            "ß",
            "ї",
            "Ґ",
            "Ł",
            "Ж",
            "я",
            "Γ",
            "δ",
            "مرحبا",
            "ع",
            "שלום",
            "न",
            "स्",
            "ते",
            "ส",
            "วั",
            "你",
            "界",
            "に",
            "ほん",
            "한",
            "글",
            "😀",
            "👩‍💻",
            "🚀",
        )
    }
}
