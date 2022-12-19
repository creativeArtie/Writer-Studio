package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.BasicLinePatterns.*;

class BasicLinePatternsTest extends PatternTestBase<BasicLinePart> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern("Break", BasicLinePatterns.BREAK.matcher("***"));
        splitPrintPattern("Text", BasicLinePatterns.TEXT.matcher("abc"));
        splitPrintPattern("Agenda", BasicLinePatterns.AGENDA.matcher("!avd"));
        splitPrintPattern("Quote", BasicLinePatterns.QUOTE.matcher(">add"));
    }

    @Test
    void testBreak() {
        final Matcher match = BasicLinePatterns.BREAK.matcher("***");
        assertGroup("***", match, BasicLinePart.BREAKER, 1);
        assertEnd(match);
    }

    @Test
    void testNonBreak() {
        Assertions.assertNull(BasicLinePatterns.BREAK.matcher("abc"));
    }

    @Test
    void testNormal() {
        final Matcher match = BasicLinePatterns.TEXT.matcher("\\a{aded}");
        assertGroup("\\a{aded}", match, BasicLinePart.FORMATTED, 1);
        assertEnd(match);
    }

    @Test
    void testNormalEmpty() {
        final Matcher match = BasicLinePatterns.TEXT.matcher("");
        assertEnd(match);
    }

    @Test
    void testAgenda() {
        final Matcher match = BasicLinePatterns.AGENDA.matcher("!aded");
        assertGroup("!", match, BasicLinePart.TODOER, 1);
        assertGroup("aded", match, BasicLinePart.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testAgendaEmpty() {
        final Matcher match = BasicLinePatterns.AGENDA.matcher("!");
        assertGroup("!", match, BasicLinePart.TODOER, 1);
        assertEnd(match);
    }

    @Test
    void testQuote() {
        final Matcher match = BasicLinePatterns.QUOTE.matcher(">aded");
        assertGroup(">", match, BasicLinePart.QUOTER, 1);
        assertGroup("aded", match, BasicLinePart.FORMATTED, 2);
        assertEnd(match);
    }

    @Test
    void testQuoteEmpty() {
        final Matcher match = BasicLinePatterns.QUOTE.matcher(">");
        assertGroup(">", match, BasicLinePart.QUOTER, 1);
        assertEnd(match);
    }

    @ParameterizedTest
    @CsvSource({ "add,BREAK", "!ddd,QUOTE", ">quote,AGENDA" })
    void testError(String text, String linePattern) {
        final BasicLinePatterns pattern = BasicLinePatterns.valueOf(linePattern);
        Assertions.assertNull(pattern.matcher(text));
    }
}
