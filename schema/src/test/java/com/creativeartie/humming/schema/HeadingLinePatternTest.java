package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.HeadingLinePattern.*;

class HeadingLinePatternTest extends PatternTestBase<HeadingLinePattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(HeadingLinePattern.matcher("!=abc"));
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
        Assertions.assertNull(HeadingLinePattern.matcher(text));
    }

    @Test
    void testFormatedHeading() {
        final Matcher match = HeadingLinePattern.matcher("==abc*abc*{!todo}");
        assertGroup("==", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("abc*abc*{!todo}", match, HeadingLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testOutline() {
        final Matcher match = HeadingLinePattern.matcher("!==\\=*Hello* `World`!!!! #stub 1");
        assertGroup("!", match, HeadingLinePattern.OUTLINE, 1);
        assertGroup("==", match, HeadingLinePattern.LEVEL, 2);
        assertGroup("\\=*Hello* `World`!!!! ", match, HeadingLinePattern.TEXT, 3);
        assertGroup("#stub", match, HeadingLinePattern.STATUS, 4);
        assertGroup(" 1", match, HeadingLinePattern.DETAILS, 5);
        assertEnd(match);
    }

    @Test
    void testHeading() {
        final Matcher match = HeadingLinePattern.matcher("==*Hello* `World`!!!! #OUtLine");
        assertGroup("==", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("*Hello* `World`!!!! ", match, HeadingLinePattern.TEXT, 2);
        assertGroup("#OUtLine", match, HeadingLinePattern.STATUS, 3);
        assertEnd(match);
    }

    @Test
    void testNoStatus() {
        final Matcher match = HeadingLinePattern.matcher("=====Hello World");
        assertGroup("=====", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("Hello World", match, HeadingLinePattern.TEXT, 2);
    }

    @Test
    void testStatusOnly() {
        final Matcher match = HeadingLinePattern.matcher("==#final");
        assertGroup("==", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("#final", match, HeadingLinePattern.STATUS, 2);
        assertEnd(match);
    }

    @Test
    void testJustHeading() {
        final Matcher match = HeadingLinePattern.matcher("!==");
        assertGroup("!", match, HeadingLinePattern.OUTLINE, 1);
        assertGroup("==", match, HeadingLinePattern.LEVEL, 2);
        assertEnd(match);
    }

    @Test
    void testTooLong() {
        final Matcher match = HeadingLinePattern.matcher("=======");
        assertGroup("======", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("=", match, HeadingLinePattern.TEXT, 2);
        assertEnd(match);
    }
}
