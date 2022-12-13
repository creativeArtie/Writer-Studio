package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.HeadingLinePattern.*;
import com.google.common.base.*;

class HeadingLinePatternTest extends PatternTestBase<HeadingLinePattern> {

    @BeforeAll
    static void displayPattern() throws Exception {
        String pattern =
            HeadingLinePattern.matcher("!=abc").pattern().pattern();
        pattern = Joiner.on("\n(?").join(Splitter.on("(?").split(pattern));
        System.out.println(pattern);

    }

    @ParameterizedTest
    @CsvSource(
        { "#stub,STUB", "#outLine,OUTLINE", "#DRAFT 12,DRAFT",
            "#random add,OTHERS" }
    )
    void testStatus(String input, String pattern) {
        StatusPattern expect = StatusPattern.valueOf(pattern);
        Assertions.assertEquals(expect, StatusPattern.getStatus(input));
    }

    @Test
    void testOutline() {
        Matcher match =
            HeadingLinePattern.matcher("!==\\=*Hello* `World`!!!! #stub 1");
        assertGroup("!", match, HeadingLinePattern.OUTLINE, 1);
        assertGroup("==", match, HeadingLinePattern.LEVEL, 2);
        assertGroup(
            "\\=*Hello* `World`!!!! ", match, HeadingLinePattern.TEXT, 3
        );
        assertGroup("#stub", match, HeadingLinePattern.STATUS, 4);
        assertGroup(" 1", match, HeadingLinePattern.DETAILS, 5);
        assertEnd(match);
    }

    @Test
    void testHeading() {
        Matcher match =
            HeadingLinePattern.matcher("==*Hello* `World`!!!! #OUtLine");
        assertGroup("==", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("*Hello* `World`!!!! ", match, HeadingLinePattern.TEXT, 2);
        assertGroup("#OUtLine", match, HeadingLinePattern.STATUS, 3);
        assertEnd(match);
    }

    @Test
    void testNoStatus() {
        Matcher match = HeadingLinePattern.matcher("=====Hello World");
        assertGroup("=====", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("Hello World", match, HeadingLinePattern.TEXT, 2);
    }

    @Test
    void testStatusOnly() {
        Matcher match = HeadingLinePattern.matcher("==#final");
        assertGroup("==", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("#final", match, HeadingLinePattern.STATUS, 2);
        assertEnd(match);
    }

    @Test
    void testJustHeading() {
        Matcher match = HeadingLinePattern.matcher("!==");
        assertGroup("!", match, HeadingLinePattern.OUTLINE, 1);
        assertGroup("==", match, HeadingLinePattern.LEVEL, 2);
        assertEnd(match);
    }

    @Test
    void testTooLong() {
        Matcher match = HeadingLinePattern.matcher("=======");
        assertGroup("======", match, HeadingLinePattern.LEVEL, 1);
        assertGroup("=", match, HeadingLinePattern.TEXT, 2);
        assertEnd(match);
    }

}
