package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.ParaHeadingPattern.*;

class ParaHeadingPatternTest extends PatternTestBase<ParaHeadingPattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ParaHeadingPattern.matcher("!=abc"));
    }

    @ParameterizedTest
    @CsvSource({ "#stub,STUB", "#outLine,OUTLINE", "#DRAFT 12,DRAFT", "#random add,OTHERS" })
    void testStatus(String input, String pattern) {
        final StatusPattern expect = StatusPattern.valueOf(pattern);
        Assertions.assertEquals(expect, StatusPattern.getStatus(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "!todo text", "normal text", "!*footnote=note data" })
    void testNonHeading(String text) {
        Assertions.assertNull(ParaHeadingPattern.matcher(text));
    }

    @Test
    void testFormatedHeading() {
        final Matcher match = ParaHeadingPattern.matcher("==abc*abc*{!todo}");
        assertGroup("==", match, ParaHeadingPattern.LEVEL, 1);
        assertGroup("abc*abc*{!todo}", match, ParaHeadingPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testOutline() {
        final Matcher match = ParaHeadingPattern.matcher("!==\\=*Hello* `World`!!!! #stub 1");
        assertGroup("!", match, ParaHeadingPattern.OUTLINE, 1);
        assertGroup("==", match, ParaHeadingPattern.LEVEL, 2);
        assertGroup("\\=*Hello* `World`!!!! ", match, ParaHeadingPattern.TEXT, 3);
        assertGroup("#stub", match, ParaHeadingPattern.STATUS, 4);
        assertGroup(" 1", match, ParaHeadingPattern.DETAILS, 5);
        assertEnd(match);
    }

    @Test
    void testHeading() {
        final Matcher match = ParaHeadingPattern.matcher("==*Hello* `World`!!!! #OUtLine");
        assertGroup("==", match, ParaHeadingPattern.LEVEL, 1);
        assertGroup("*Hello* `World`!!!! ", match, ParaHeadingPattern.TEXT, 2);
        assertGroup("#OUtLine", match, ParaHeadingPattern.STATUS, 3);
        assertEnd(match);
    }

    @Test
    void testNoStatus() {
        final Matcher match = ParaHeadingPattern.matcher("=====Hello World");
        assertGroup("=====", match, ParaHeadingPattern.LEVEL, 1);
        assertGroup("Hello World", match, ParaHeadingPattern.TEXT, 2);
    }

    @Test
    void testStatusOnly() {
        final Matcher match = ParaHeadingPattern.matcher("==#final");
        assertGroup("==", match, ParaHeadingPattern.LEVEL, 1);
        assertGroup("#final", match, ParaHeadingPattern.STATUS, 2);
        assertEnd(match);
    }

    @Test
    void testJustHeading() {
        final Matcher match = ParaHeadingPattern.matcher("!==");
        assertGroup("!", match, ParaHeadingPattern.OUTLINE, 1);
        assertGroup("==", match, ParaHeadingPattern.LEVEL, 2);
        assertEnd(match);
    }

    @Test
    void testTooLong() {
        final Matcher match = ParaHeadingPattern.matcher("=======");
        assertGroup("======", match, ParaHeadingPattern.LEVEL, 1);
        assertGroup("=", match, ParaHeadingPattern.TEXT, 2);
        assertEnd(match);
    }
}
