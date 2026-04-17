package org.bezsahara.kittybot.bot.action.comms

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BotCallParserTest {

    @Test
    fun parsesWhitespaceSeparatedArguments() {
        val parsed = assertIs<ParsedBotCall>(ParsedBotCall.parse("echo 42 \"hello world\""))

        assertEquals("echo", parsed.method.name)
        assertEquals(
            listOf(BotParam(BotType.Long), BotParam(BotType.String)),
            parsed.method.args
        )
        assertContentEquals(arrayOf(42L, "hello world"), parsed.arguments)
    }

    @Test
    fun parsesCommaSeparatedArguments() {
        val parsed = assertIs<ParsedBotCall>(ParsedBotCall.parse("sum 1, 2.5, value"))

        assertEquals("sum", parsed.method.name)
        assertEquals(
            listOf(BotParam(BotType.Long), BotParam(BotType.Double), BotParam(BotType.String)),
            parsed.method.args
        )
        assertContentEquals(arrayOf(1L, 2.5, "value"), parsed.arguments)
    }

    @Test
    fun normalizesMixedNumericListItemsToCommonType() {
        val parsed = assertIs<ParsedBotCall>(ParsedBotCall.parse("values [1, 2.5, 3]"))

        assertEquals("values", parsed.method.name)
        assertEquals(
            listOf(BotParam(BotType.List(BotType.Double))),
            parsed.method.args
        )
        assertContentEquals(arrayOf(listOf(1.0, 2.5, 3.0)), parsed.arguments)
    }

    @Test
    fun returnsErrorWhenTopLevelCommaIsMissingAValue() {
        val parsed = assertIs<ParsedBotError>(ParsedBotCall.parse("cmd 1, ]"))

        assertEquals("Expected argument after ','", parsed.error)
        assertEquals(7, parsed.atIndex)
    }

    @Test
    fun returnsErrorWhenListCommaIsMissingAValue() {
        val parsed = assertIs<ParsedBotError>(ParsedBotCall.parse("cmd [1, ]"))

        assertEquals("Expected list item after ','", parsed.error)
        assertEquals(8, parsed.atIndex)
    }

    @Test
    fun returnsErrorWhenMixingCommaAndWhitespaceSeparators() {
        val parsed = assertIs<ParsedBotError>(ParsedBotCall.parse("cmd 1, 2 3"))

        assertEquals(
            "After using ',' once, all later top-level arguments must also use ','",
            parsed.error
        )
        assertEquals(9, parsed.atIndex)
    }

    @Test
    fun returnsErrorForIncompatibleListItemTypes() {
        val parsed = assertIs<ParsedBotError>(ParsedBotCall.parse("cmd [1, hello]"))

        assertEquals("Incompatible list item types: Long and String", parsed.error)
        assertEquals(14, parsed.atIndex)
    }
}
