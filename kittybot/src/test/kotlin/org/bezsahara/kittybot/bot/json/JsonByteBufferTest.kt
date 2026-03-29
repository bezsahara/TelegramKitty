package org.bezsahara.kittybot.bot.json

import io.vertx.core.buffer.Buffer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
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
            putJsonObject(key("item"), TestPayload.serializer(), Json, TestPayload(1, "alpha"))
            putListOfJsonObjects(
                key("items"),
                TestPayload.serializer(),
                Json,
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

    @Test
    fun toBufferReturnsEncodedJsonForWrittenData() {
        val actual = (JsonByteBuffer(1).apply {
            putStringUnsafe(key("kind"), "ping")
            putListOfLongUnsafe(key("ids"), listOf(1L, 2L))
        }.toBuffer() as Buffer).toString(Charsets.UTF_8)

        assertEquals("""{"kind":"ping","ids":[1,2]}""", actual)
    }

    private fun render(
        initialCapacity: Int = 8,
        body: JsonByteBuffer.() -> Unit
    ): String = JsonByteBuffer(initialCapacity).apply(body).toByteArray().decodeToString()

    private fun key(value: String): ByteArray = value.encodeToByteArray()

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
}
