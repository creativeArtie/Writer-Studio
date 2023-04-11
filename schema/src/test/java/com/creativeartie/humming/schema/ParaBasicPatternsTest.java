package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.ParaBasicPatterns.*;

@SuppressWarnings("nls")
class ParaBasicPatternsTest extends PatternTestBase<LineSpanParts> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern("Break", ParaBasicPatterns.BREAK.matcher("***"));
        splitPrintPattern("Text", ParaBasicPatterns.TEXT.matcher("abc"));
        splitPrintPattern("Agenda", ParaBasicPatterns.AGENDA.matcher("!avd"));
        splitPrintPattern("Quote", ParaBasicPatterns.QUOTE.matcher(">add"));
    }

    @Test
    void testBreak() {
        final Matcher match = ParaBasicPatterns.BREAK.matcher("***");
        assertGroup("***", match, LineSpanParts.BREAKER, 1);
        assertEnd(match);
    }

    @Test
    void testFullBreak() {
        final Matcher match = ParaBasicPatterns.BREAK.matcher("***\n");
        assertGroup("***", match, LineSpanParts.BREAKER, 1);
        assertGroup("\n", match, LineSpanParts.ENDER, 2);
        assertEnd(match);
    }

    @Test
    void testNonBreak() {
        Assertions.assertNull(ParaBasicPatterns.BREAK.matcher("abc"));
    }

    @Test
    void testNormal() {
        final Matcher match = ParaBasicPatterns.TEXT.matcher("\\a{aded}");
        assertGroup("\\a{aded}", match, LineSpanParts.FORMATTED, 1);
        assertEnd(match);
    }

    @Test
    void testNormalEmpty() {
        final Matcher match = ParaBasicPatterns.TEXT.matcher("");
        assertEnd(match);
    }

    @Test
    void testAgenda() {
        final Matcher match = ParaBasicPatterns.AGENDA.matcher("!aded");
        assertGroup("!", match, LineSpanParts.TODOER, 1);
        assertGroup("aded", match, LineSpanParts.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testAgendaEmpty() {
        final Matcher match = ParaBasicPatterns.AGENDA.matcher("!");
        assertGroup("!", match, LineSpanParts.TODOER, 1);
        assertEnd(match);
    }

    @Test
    void testQuote() {
        final Matcher match = ParaBasicPatterns.QUOTE.matcher(">aded");
        assertGroup(">", match, LineSpanParts.QUOTER, 1);
        assertGroup("aded", match, LineSpanParts.FORMATTED, 2);
        assertEnd(match);
    }

    @Test
    void testQuoteEmpty() {
        final Matcher match = ParaBasicPatterns.QUOTE.matcher(">");
        assertGroup(">", match, LineSpanParts.QUOTER, 1);
        assertEnd(match);
    }

    @ParameterizedTest
    @CsvSource({ "add,BREAK", "!ddd,QUOTE", ">quote,AGENDA" })
    void testError(String text, String linePattern) {
        final ParaBasicPatterns pattern = ParaBasicPatterns.valueOf(linePattern);
        Assertions.assertNull(pattern.matcher(text));
    }
}
