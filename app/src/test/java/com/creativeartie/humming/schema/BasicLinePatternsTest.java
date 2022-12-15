package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.BasicLinePatterns.*;

class BasicLinePatternsTest extends PatternTestBase<BasicLinePart> {

    @BeforeAll
    static void displayPattern() throws Exception {
        System.out.println(
            BasicLinePatterns.BREAK.matcher("***").pattern().pattern()
        );
        System.out
            .println(BasicLinePatterns.TEXT.matcher("abc").pattern().pattern());
        System.out.println(
            BasicLinePatterns.AGENDA.matcher("!avd").pattern().pattern()
        );
        System.out.println(
            BasicLinePatterns.QUOTE.matcher(">add").pattern().pattern()
        );
    }

    @Test
    void testBreak() {
        Matcher match = BasicLinePatterns.BREAK.matcher("***");
        assertGroup("***", match, BasicLinePart.BREAKER, 1);
        assertEnd(match);
    }

    @Test
    void testNormal() {
        Matcher match = BasicLinePatterns.TEXT.matcher("\\a{aded}");
        assertGroup("\\a{aded}", match, BasicLinePart.FORMATTED, 1);
        assertEnd(match);
    }

    @Test
    void testNormalEmpty() {
        Matcher match = BasicLinePatterns.TEXT.matcher("");
        assertEnd(match);
    }

    @Test
    void testAgenda() {
        Matcher match = BasicLinePatterns.AGENDA.matcher("!aded");
        assertGroup("!", match, BasicLinePart.TODOER, 1);
        assertGroup("aded", match, BasicLinePart.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testAgendaEmpty() {
        Matcher match = BasicLinePatterns.AGENDA.matcher("!");
        assertGroup("!", match, BasicLinePart.TODOER, 1);
        assertEnd(match);
    }

    @Test
    void testQuote() {
        Matcher match = BasicLinePatterns.QUOTE.matcher(">aded");
        assertGroup(">", match, BasicLinePart.QUOTER, 1);
        assertGroup("aded", match, BasicLinePart.FORMATTED, 2);
        assertEnd(match);
    }

    @Test
    void testQuoteEmpty() {
        Matcher match = BasicLinePatterns.QUOTE.matcher(">");
        assertGroup(">", match, BasicLinePart.QUOTER, 1);
        assertEnd(match);
    }

    @ParameterizedTest
    @CsvSource({ "add,BREAK", "!ddd,QUOTE", ">quote,AGENDA" })
    void testError(String text, String linePattern) {
        BasicLinePatterns pattern = BasicLinePatterns.valueOf(linePattern);
        Assertions.assertNull(pattern.matcher(text));

    }

}
